import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    // Set up the control flow graph and scope for testing.
    // Replace this with the actual setup code for your class under test.

    jsScope = new Scope();
    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 1);

    Node root = Node.newBlock(0, null);
    cfg = new ControlFlowGraph<>();
    cfg.addNode(root);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice entryLattice =
        liveVariablesAnalysis.createEntryLattice();
    assertEquals(Arrays.toString(jsScope.getAllVarNames()), entryLattice.toString());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice =
        liveVariablesAnalysis.createInitialEstimateLattice();
    assertEquals(Arrays.toString(jsScope.getAllVarNames()), initialEstimateLattice.toString());
  }

  @Test
  public void testSimpleAssignment() {
    Node assignmentNode = Node.newAssign(0, Node.newName("x"), Node.newName("y"));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice result =
        liveVariablesAnalysis.flowThrough(assignmentNode, expectedResult);

    assertEquals(expectedResult, result);
  }

  @Test
  public void testSimpleAssignmentWithKill() {
    Node assignmentNode = Node.newAssign(0, Node.newName("x"), Node.newName("y"));
    Node killNode = Node.newAssign(0, Node.newName("x"), Node.newNumber(0));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice result =
        liveVariablesAnalysis.flowThrough(assignmentNode, expectedResult);

    LiveVariablesAnalysis.LiveVariableLattice killResult =
        liveVariablesAnalysis.flowThrough(killNode, result);

    LiveVariablesAnalysis.LiveVariableLattice expectedKillResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedKillResult.liveSet.clear(jsScope.getVar("x").index);

    assertEquals(expectedKillResult, killResult);
  }

  @Test
  public void testSimpleConditionalAssignment() {
    Node conditionNode = Node.newConditional(0, Node.newBool(true), Node.newName("x"), Node.newName("y"));
    Node assignmentNode = Node.newAssign(0, Node.newName("x"), Node.newName("y"));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice result =
        liveVariablesAnalysis.flowThrough(conditionNode, expectedResult);

    LiveVariablesAnalysis.LiveVariableLattice conditionalResult =
        liveVariablesAnalysis.flowThrough(assignmentNode, result);

    LiveVariablesAnalysis.LiveVariableLattice expectedConditionalResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedConditionalResult.liveSet.set(jsScope.getVar("x").index);

    assertEquals(expectedConditionalResult, conditionalResult);
  }

  @Test
  public void testSimpleConditionalAssignmentWithKill() {
    Node conditionNode = Node.newConditional(0, Node.newBool(true), Node.newName("x"), Node.newName("y"));
    Node killNode = Node.newAssign(0, Node.newName("x"), Node.newNumber(0));
    Node assignmentNode = Node.newAssign(0, Node.newName("x"), Node.newName("y"));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice result =
        liveVariablesAnalysis.flowThrough(conditionNode, expectedResult);

    LiveVariablesAnalysis.LiveVariableLattice conditionalResult =
        liveVariablesAnalysis.flowThrough(assignmentNode, result);

    LiveVariablesAnalysis.LiveVariableLattice killResult =
        liveVariablesAnalysis.flowThrough(killNode, conditionalResult);

    LiveVariablesAnalysis.LiveVariableLattice expectedKillResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedKillResult.liveSet.clear(jsScope.getVar("x").index);

    assertEquals(expectedKillResult, killResult);
  }

  @Test
  public void testSimpleWhileLoop() {
    Node whileNode = Node.newWhile(0, Node.newName("x"), Node.newAssign(0, Node.newName("x"), Node.newNumber(1)));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice result =
        liveVariablesAnalysis.flowThrough(whileNode, expectedResult);

    LiveVariablesAnalysis.LiveVariableLattice expectedWhileResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedWhileResult.liveSet.set(jsScope.getVar("x").index);

    List<DiGraphEdge<Node, Branch>> edges = cfg.getOutEdges(whileNode);
    for (DiGraphEdge<Node, Branch> edge : edges) {
      result = liveVariablesAnalysis.flowThrough(edge.getValue().getTarget(), result);
    }

    assertEquals(expectedWhileResult, result);
  }

  @Test
  public void testSimpleForLoop() {
    Node forNode = Node.newFor(0, Node.newName("i"), Node.newAssign(0, Node.newName("i"), Node.newNumber(0)), Node.newName("i"), Node.newNumber(1), Node.newName("array"), Node.newName("array"), Node.newName("i"), Node.newName("array.length"), Node.newAssign(0, Node.newName("i"), Node.newBinaryOp(Token.ADD, Node.newName("i"), Node.newNumber(1))), Node.newName("array[i]"));

    LiveVariablesAnalysis.LiveVariableLattice expectedResult =
        liveVariablesAnalysis.createEntryLattice();
    expectedResult.liveSet.setAll(jsScope.getVarCount());

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(forNode, expectedResult);

    LiveVariablesAnalysis.LiveVariableLattice expectedForResult = new LiveVariablesAnalysis.LiveVariableLattice(expectedResult);
    expectedForResult.liveSet.clear(jsScope.getVar("i").index);

    List<DiGraphEdge<Node, Branch>> edges = cfg.getOutEdges(forNode);
    for (DiGraphEdge<Node, Branch> edge : edges) {
      result = liveVariablesAnalysis.flowThrough(edge.getValue().getTarget(), result);
    }

    assertEquals(expectedForResult, result);
  }

  @Test
  public void testEscapedVariables() {
    Node root = Node.newBlock(0, null);
    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 1);
    jsScope.declareVar("z", 2);

    Node functionNode = Node.newFunction(0, Node.newName("f"), Collections.emptyList(), Node.newBlock(0, null));
    Node argumentNode = functionNode.getFirstChild().getNext();
    jsScope.declareVar(argumentNode.getString(), 3);

    cfg.addNode(root);
    cfg.addNode(functionNode);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    Set<Var> escaped = liveVariablesAnalysis.getEscapedLocals();

    LiveVariablesAnalysis.LiveVariableLattice functionEntryLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(functionNode, entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice functionResult = liveVariablesAnalysis.flowThrough(argumentNode, result);

    assertTrue(escaped.contains(jsScope.getVar("x")));
    assertTrue(escaped.contains(jsScope.getVar("y")));
    assertTrue(escaped.contains(jsScope.getVar("z")));
    assertTrue(escaped.contains(jsScope.getVar(argumentNode.getString())));

    assertEquals(functionEntryLattice, functionResult);
  }

  @Test
  public void testSimpleFunctionCall() {
    Node functionDefinitionNode = Node.newFunction(0, Node.newName("f"), Collections.emptyList(), Node.newBlock(0, null));
    Node functionCallNode = Node.newName("f");

    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 1);

    cfg.addNode(functionDefinitionNode);
    cfg.addNode(functionCallNode);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionEntryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionResultLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice callResultLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(functionDefinitionNode, entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice functionResult = liveVariablesAnalysis.flowThrough(Node.newName("x"), result);
    LiveVariablesAnalysis.LiveVariableLattice functionCallResult = liveVariablesAnalysis.flowThrough(functionCallNode, functionResult);

    LiveVariablesAnalysis.LiveVariableLattice expectedFunctionResult = new LiveVariablesAnalysis.LiveVariableLattice(functionEntryLattice);
    expectedFunctionResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice expectedCallResult = new LiveVariablesAnalysis.LiveVariableLattice(entryLattice);
    expectedCallResult.liveSet.set(jsScope.getVar("x").index);

    assertEquals(expectedFunctionResult, functionResult);
    assertEquals(expectedCallResult, functionCallResult);
  }

  @Test
  public void testSimpleFunctionCallWithKill() {
    Node functionDefinitionNode = Node.newFunction(0, Node.newName("f"), Collections.emptyList(), Node.newBlock(0, null));
    Node functionCallNode = Node.newName("f");
    Node killNode = Node.newAssign(0, Node.newName("x"), Node.newNumber(0));

    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 1);

    cfg.addNode(functionDefinitionNode);
    cfg.addNode(functionCallNode);
    cfg.addNode(killNode);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionEntryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionResultLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice callResultLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice killResultLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(functionDefinitionNode, entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice functionResult = liveVariablesAnalysis.flowThrough(Node.newName("x"), result);
    LiveVariablesAnalysis.LiveVariableLattice functionCallResult = liveVariablesAnalysis.flowThrough(functionCallNode, functionResult);
    LiveVariablesAnalysis.LiveVariableLattice killResult = liveVariablesAnalysis.flowThrough(killNode, functionCallResult);

    LiveVariablesAnalysis.LiveVariableLattice expectedFunctionResult = new LiveVariablesAnalysis.LiveVariableLattice(functionEntryLattice);
    expectedFunctionResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice expectedCallResult = new LiveVariablesAnalysis.LiveVariableLattice(entryLattice);
    expectedCallResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice expectedKillResult = liveVariablesAnalysis.createEntryLattice();
    expectedKillResult.liveSet.clear(jsScope.getVar("x").index);

    assertEquals(expectedFunctionResult, functionResult);
    assertEquals(expectedCallResult, functionCallResult);
    assertEquals(expectedKillResult, killResult);
  }

  @Test
  public void testSimpleFunctionCallWithEscape() {
    Node functionDefinitionNode = Node.newFunction(0, Node.newName("f"), Collections.emptyList(), Node.newBlock(0, null));
    Node functionCallNode = Node.newName("f");

    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 1);
    jsScope.declareVar("z", 2);

    cfg.addNode(functionDefinitionNode);
    cfg.addNode(functionCallNode);

    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionEntryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice functionResultLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice callResultLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(functionDefinitionNode, entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice functionResult = liveVariablesAnalysis.flowThrough(Node.newName("x"), result);
    LiveVariablesAnalysis.LiveVariableLattice functionCallResult = liveVariablesAnalysis.flowThrough(functionCallNode, functionResult);

    Set<Var> escaped = liveVariablesAnalysis.getEscapedLocals();

    assertTrue(escaped.contains(jsScope.getVar("x")));

    LiveVariablesAnalysis.LiveVariableLattice expectedFunctionResult = new LiveVariablesAnalysis.LiveVariableLattice(functionEntryLattice);
    expectedFunctionResult.liveSet.set(jsScope.getVar("x").index);

    LiveVariablesAnalysis.LiveVariableLattice expectedCallResult = new LiveVariablesAnalysis.LiveVariableLattice(entryLattice);
    expectedCallResult.liveSet.set(jsScope.getVar("x").index);

    assertEquals(expectedFunctionResult, functionResult);
    assertEquals(expectedCallResult, functionCallResult);
  }
}