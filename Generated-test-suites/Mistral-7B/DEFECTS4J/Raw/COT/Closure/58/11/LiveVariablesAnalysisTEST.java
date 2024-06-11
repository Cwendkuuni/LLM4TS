import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.List;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testEmptyScope() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.forwardPropagate(null);

    assertFalse(result.isLive(0));
    assertFalse(result.isLive(1));
  }

  @Test
  public void testSingleAssignment() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node node = Node.newScopeNode(Token.BLOCK);
    Node assignment = Node.newName("x");
    Node value = Node.newNumber(1);
    Node op = Node.newOperator("=", assignment, value);
    Node next = Node.newName("y");

    node.setFirstChild(assignment);
    node.setLastChild(next);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, entry);

    assertTrue(result.isLive(0));
  }

  @Test
  public void testConditionalAssignment() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node node = Node.newIfStatement(Token.OP_ELSE, Node.newName("x"), Node.newName("y"));
    Node assignment = Node.newName("x");
    Node value = Node.newNumber(1);
    Node op = Node.newOperator("=", assignment, value);

    node.setFirstChild(Node.newName("x"));
    node.setLastChild(op);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, entry);

    assertTrue(result.isLive(0));
  }

  @Test
  public void testConditionalAssignmentWithElse() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node node = Node.newIfStatement(Token.OP_ELSE, Node.newName("x"), Node.newName("y"));
    Node assignment = Node.newName("x");
    Node value = Node.newNumber(1);
    Node op = Node.newOperator("=", assignment, value);

    node.setFirstChild(Node.newName("x"));
    node.setLastChild(op);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result1 = analysis.flowThrough(node, entry);
    LiveVariablesAnalysis.LiveVariableLattice result2 = analysis.flowThrough(node.getLastChild(), entry);

    assertTrue(result1.isLive(0));
    assertFalse(result2.isLive(0));
  }

  @Test
  public void testLoop() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node node = Node.newWhileStatement(null, Node.newName("x"), Node.newName("x"));
    Node assignment = Node.newName("x");
    Node value = Node.newNumber(1);
    Node op = Node.newOperator("=", assignment, value);

    node.setFirstChild(assignment);
    node.setLastChild(op);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.forwardPropagate(node);

    assertTrue(result.isLive(0));
  }

  @Test (expected = AssertionError.class)
  public void testFunction() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node node = Node.newFunctionStatement(Token.FUNCTION, null, null, null);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, entry);

    fail("Function should not be supported.");
  }

  @Test
  public void testEscapedVariables() {
    ControlFlowGraph cfg = new ControlFlowGraph(null);
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);
   LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    Node root = Node.newScopeNode(Token.SCRIPT);
    Node function = Node.newFunctionStatement(Token.FUNCTION, null, null, null);
    Node name = Node.newName("x");
    Node assign = Node.newOperator("=", name, Node.newName("arguments"));

    function.setFirstChild(name);
    function.setLastChild(assign);
    root.setFirstChild(function);

    LiveVariablesAnalysis.LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.forwardPropagate(root);

    assertTrue(result.isLive(0));
  }
}