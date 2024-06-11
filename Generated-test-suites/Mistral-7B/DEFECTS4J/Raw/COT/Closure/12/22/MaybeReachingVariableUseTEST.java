import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Initialize the control flow graph, scope, and analysis.
    controlFlowGraph = new ControlFlowGraph<Node>(Node.class);
    jsScope = new Scope();
    analysis = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  /**
   * Test method for {@link MaybeReachingVariableUse#MaybeReachingVariableUse(ControlFlowGraph, Scope, AbstractCompiler)}.
   */
  @Test
  public void testConstructor() {
    assertNotNull(analysis);
  }

  /**
   * Test method for {@link MaybeReachingVariableUse#flowThrough(Node, ReachingUses)}.
   */
  @Test
  public void testFlowThrough() {
    Node node = Node.newIntLiteral(1);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(node, input);

    assertNotNull(output);
    assertTrue(output.mayUseMap.isEmpty());
  }

  /**
   * Test method for {@link MaybeReachingVariableUse#getUses(String, Node)}.
   */
  @Test
  public void testGetUses() {
    // Create a simple control flow graph and set up the scope.
    Node entryNode = Node.newASTExpression(new Token(Token.BLOCK));
    Node assignmentNode = Node.newAssignment(Node.newName("x"), Node.newIntLiteral(1));
    Node useNode = Node.newPrintStatement(Node.newName("x"));

    Node conditionalNode = Node.newConditionalExpression(
        Node.newBooleanLiteral(true), Node.newName("x"), Node.newIntLiteral(0));
    Node anotherUseNode = Node.newPrintStatement(Node.newName("x"));

    Node exitNode = Node.newASTExpression(new Token(Token.BLOCK));

    controlFlowGraph.addEdge(new DiGraphEdge<>(entryNode, assignmentNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode, useNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode, conditionalNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(conditionalNode, useNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(conditionalNode, anotherUseNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode, exitNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(useNode, exitNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(anotherUseNode, exitNode));

    jsScope.declareLocal("x", Node.UNDEFINED);

    analysis.initialize(entryNode);

    // Test getting uses for a local variable.
    String name = "x";
    Node definitionNode = assignmentNode;

    ReachingUses reachingUses = new ReachingUses();
    analysis.flowThrough(definitionNode, reachingUses);

    Collection<Node> uses = analysis.getUses(name, definitionNode);
    assertNotNull(uses);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useNode));
    assertTrue(uses.contains(anotherUseNode));

    // Test getting uses for an undefined variable.
    name = "undefinedVariable";
    definitionNode = Node.UNDEFINED;

    reachingUses = new ReachingUses();
    analysis.flowThrough(definitionNode, reachingUses);

    uses = analysis.getUses(name, definitionNode);
    assertNull(uses);
  }

  /**
   * Test method for {@link MaybeReachingVariableUse#computeMayUse(Node, Node, ReachingUses, boolean)}.
   */
  @Test
  public void testComputeMayUse() {
    // Create a simple control flow graph and set up the scope.
    Node entryNode = Node.newASTExpression(new Token(Token.BLOCK));
    Node assignmentNode1 = Node.newAssignment(Node.newName("x"), Node.newIntLiteral(1));
    Node useNode1 = Node.newPrintStatement(Node.newName("x"));
    Node assignmentNode2 = Node.newAssignment(Node.newName("x"), Node.newIntLiteral(2));
    Node useNode2 = Node.newPrintStatement(Node.newName("x"));

    Node exitNode = Node.newASTExpression(new Token(Token.BLOCK));

    controlFlowGraph.addEdge(new DiGraphEdge<>(entryNode, assignmentNode1));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode1, useNode1));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode1, assignmentNode2));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode2, useNode2));
    controlFlowGraph.addEdge(new DiGraphEdge<>(assignmentNode2, exitNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(useNode1, exitNode));
    controlFlowGraph.addEdge(new DiGraphEdge<>(useNode2, exitNode));

    jsScope.declareLocal("x", Node.UNDEFINED);

    analysis.initialize(entryNode);

    // Test computing may use for a local variable.
    String name = "x";
    Node definitionNode1 = assignmentNode1;
    Node definitionNode2 = assignmentNode2;

    ReachingUses reachingUses1 = new ReachingUses();
    analysis.computeMayUse(definitionNode1, definitionNode1, reachingUses1, false);

    Collection<Node> uses1 = analysis.getUses(name, definitionNode1);
    assertNotNull(uses1);
    assertFalse(uses1.isEmpty());
    assertTrue(uses1.contains(useNode1));

    ReachingUses reachingUses2 = new ReachingUsages();
    analysis.computeMayUse(definitionNode2, definitionNode1, reachingUses2, false);

    Collection<Node> uses2 = analysis.getUses(name, definitionNode2);
    assertNotNull(uses2);
    assertFalse(uses2.isEmpty());
    assertTrue(uses2.contains(useNode1));
    assertTrue(uses2.contains(useNode2));

    // Test computing may use for a non-local variable.
    name = "undefinedVariable";
    Node nonLocalDefinitionNode = Node.UNDEFINED;

    ReachingUses reachingUses3 = new ReachingUsages();
    analysis.computeMayUse(nonLocalDefinitionNode, definitionNode1, reachingUses3, false);

    uses2 = analysis.getUses(name, nonLocalDefinitionNode);
    assertNull(uses2);

    // Test computing may use for a variable that is already defined.
    name = "x";
    Node existingDefinitionNode = useNode1;

    ReachingUses reachingUses4 = new ReachingUsages();
    analysis.computeMayUse(existingDefinitionNode, definitionNode1, reachingUses4, false);

    uses1 = analysis.getUses(name, existingDefinitionNode);
    assertNotNull(uses1);
    assertFalse(uses1.isEmpty());
    assertTrue(uses1.contains(useNode1));
    assertTrue(uses1.contains(useNode2));
  }

  /**
   * Test method for {@link MaybeReachingVariableUse#hasExceptionHandler(Node)}.
   */
  @Test
  public void testHasExceptionHandler() {
    Node node = Node.newCatch(null, Node.UNDEFINED, Node.UNDEFINED);

    assertFalse(analysis.hasExceptionHandler(node));

    node = Node.newTryStatement(Node.UNDEFINED, Node.UNDEFINED, Node.UNDEFINED);
    Node handlerNode = Node.newCatch(Node.UNDEFINED, Node.UNDEFINED, Node.UNDEFINED);
    controlFlowGraph.addEdge(new DiGraphEdge<>(node, handlerNode));

    analysis.initialize(node);

    assertTrue(analysis.hasExceptionHandler(node));
    assertTrue(analysis.hasExceptionHandler(handlerNode));
  }
}