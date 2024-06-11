import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {
  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setup() {
    controlFlowGraph = new ControlFlowGraph<Node>(null);
    jsScope = new Scope(null);
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testEmptyGraph() {
    ReachingUses result = maybeReachingVariableUse.analyze(null);
    assertNull(result);
  }

  @Test
  public void testSimpleBlock() {
    Node blockNode = createNode(Token.BLOCK);

    ReachingUses result = maybeReachingVariableUse.analyze(blockNode);
    assertEquals(new ReachingUses(), result);
  }

  @Test
  public void testFunctionWithNoDefinitions() {
    Node functionNode = createNode(Token.FUNCTION);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    assertEquals(new ReachingUses(), result);
  }

  @Test
  public void testFunctionWithOneDefinition() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUses expected = new ReachingUses();
    expected.mayUseMap.put(jsScope.getVar("x"), assignmentNode);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithMultipleDefinitions() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definition1Node = createNode(Token.VAR, "x");
    Node definition2Node = createNode(Token.VAR, "x");
    Node assignment1Node = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node assignment2Node = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "2"));
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definition1Node);
    addEdge(functionNode, definition2Node);
    addEdge(definition1Node, assignment1Node);
    addEdge(definition2Node, assignment2Node);
    addEdge(assignment1Node, returnNode);
    addEdge(assignment2Node, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUses expected = new ReachingUses();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignment1Node);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignment2Node);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithUses() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node useNode1 = createNode(Token.NAME, "x");
    Node useNode2 = createNode(Token.NAME, "x");
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, useNode1);
    addEdge(assignmentNode, useNode2);
    addEdge(useNode1, returnNode);
    addEdge(useNode2, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUses expected = new ReachingUses();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignmentNode);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode1);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode2);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithUsesAndEscapedVariables() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node useNode1 = createNode(Token.NAME, "x");
    Node useNode2 = createNode(Token.NAME, "y");
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, useNode1);
    addEdge(assignmentNode, useNode2);
    addEdge(useNode1, returnNode);

    Set<Var> escapedVariables = Sets.newHashSet(jsScope.getVar("y"));
    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null) {
      @Override
      Set<Var> escaped() {
        return escapedVariables;
      }
    };

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUsages expected = new ReachingUsages();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.remove(jsScope.getVar("y"));
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithUsesAndExceptionHandlers() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node useNode1 = createNode(Token.NAME, "x");
    Node useNode2 = createNode(Token.NAME, "x");
    Node exceptionHandlerNode = createNode(Token.TRY);
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, useNode1);
    addEdge(assignmentNode, useNode2);
    addEdge(useNode1, returnNode);
    addEdge(useNode2, returnNode);

    addEdge(functionNode, exceptionHandlerNode);
    addEdge(exceptionHandlerNode, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUsages expected = new ReachingUsages();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignmentNode);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode1);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode2);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithUsesAndCatchBlocks() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node useNode1 = createNode(Token.NAME, "x");
    Node useNode2 = createNode(Token.NAME, "x");
    Node catchNode = createNode(Token.CATCH);
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, useNode1);
    addEdge(assignmentNode, useNode2);
    addEdge(useNode1, returnNode);
    addEdge(useNode2, returnNode);

    addEdge(functionNode, catchNode);
    addEdge(catchNode, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUsages expected = new ReachingUsages();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignmentNode);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode1);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode2);
    assertEquals(expected, result);
  }

  @Test
  public void testFunctionWithUsesAndLoops() {
    Node functionNode = createNode(Token.FUNCTION);
    Node definitionNode = createNode(Token.VAR, "x");
    Node assignmentNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "1"));
    Node useNode1 = createNode(Token.NAME, "x");
    Node useNode2 = createNode(Token.NAME, "x");
    Node loopNode = createNode(Token.WHILE);
    Node conditionNode = createNode(Token.NAME, "condition");
    Node updateNode = createNode(Token.ASSIGN, "x", createNode(Token.NAME, "x"));
    Node returnNode = createNode(Token.RETURN);

    addEdge(functionNode, definitionNode);
    addEdge(definitionNode, assignmentNode);
    addEdge(assignmentNode, useNode1);
    addEdge(assignmentNode, useNode2);
    addEdge(useNode1, loopNode);
    addEdge(conditionNode, loopNode);
    addEdge(updateNode, loopNode);
    addEdge(loopNode, returnNode);

    ReachingUses result = maybeReachingVariableUse.analyze(functionNode);
    ReachingUsages expected = new ReachingUsages();
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), assignmentNode);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode1);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.putAll(result.mayUseMap);
    expected.mayUseMap.put(jsScope.getVar("x"), useNode2);
    assertEquals(expected, result);
  }

  private Node createNode(int type) {
    return createNode(type, null);
  }

  private Node createNode(int type, String value) {
    Node node = new Node();
    node.setType(type);
    if (value != null) {
      node.setString(value);
    }
    return node;
  }

  private Node createNode(int type, Node... children) {
    Node node = new Node();
    node.setType(type);
    for (Node child : children) {
      node.appendChild(child);
    }
    return node;
  }

  private void addEdge(Node sourceNode, Node targetNode) {
    DiGraphEdge edge = new DiGraphEdge(sourceNode, targetNode);
    controlFlowGraph.addEdge(edge);
  }

  private static class ReachingUsages {
    ReachingUsages() {
    }

    ReachingUsages(ReachingUsages other) {
      mayUseMap = new HashMap<>(other.mayUseMap);
    }

    Multimap<Var, Node> mayUseMap;
  }
}