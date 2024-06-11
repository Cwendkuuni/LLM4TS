import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractSyntaxTree;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalType;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    String code = "function foo() {" +
        "  var a = 1, b = 2;" +
        "  if (true) {" +
        "    var c = 3;" +
        "    a = b;" +
        "  } else {" +
        "    a = 4;" +
        "  }" +
        "  b = 3;" +
        "  return a;";
    root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    assertTrue(cfg.getDirectedGraphNodes().size() > 0);
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);

    assertFalse(isAssign(root.getFirstChild().getNext()));
    assertFalse(isAssign(findNode("b=3", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithException() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  try {" +
        "    throw new Error(\"test error\");" +
        "  } catch (e) {" +
        "    var a = e.message;" +
        "  }" +
        "  return a;";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    try {
      deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null),
          deadAssignmentsElimination.liveness.getCfg());
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturn() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  return a;" +
        "  a = 2;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertFalse(isAssign(findNode("a=2", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithIf() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  if (true) {" +
        "    a = 2;" +
        "  }" +
        "  return a;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertFalse(isAssign(findNode("a=2", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithWhile() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  while (true) {" +
        "    a = 2;" +
        "  }" +
        "  return a;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertFalse(isAssign(findNode("a=2", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFor() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    a = 2;" +
        "  }" +
        "  return a;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertFalse(isAssign(findNode("a=2", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithForIn() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  for (var i in [1, 2, 3]) {" +
        "    a = 2;" +
        "  }" +
        "  return a;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertTrue(isAssign(findNode("a=2", root)));
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableScoped() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    String code = "function foo() {" +
        "  var a = 1;" +
        "  function bar() {" +
        "    var b = a;" +
        "  }" +
        "  bar();" +
        "  return a;" +
        "}";
    Node root = NodeOperations.parseJsonToAbstractSyntaxTree(code, null);

    traverse(compiler, root, deadAssignmentsElimination, TraversalType.POST_ORDER);
    assertNotNull(deadAssignmentsElimination.liveness);
    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    deadAssignmentsElimination.tryRemoveDeadAssignments(traverse(compiler, root, null), cfg);
    assertFalse(isAssign(findNode("b=a", findNode("function bar", root))));
  }
}