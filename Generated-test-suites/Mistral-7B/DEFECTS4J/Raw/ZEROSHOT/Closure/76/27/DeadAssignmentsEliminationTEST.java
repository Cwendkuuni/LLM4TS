package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilationResult;
import com.google.javascript.jscomp.AbstractCompiler.SourceFile;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for DeadAssignmentsElimination class
 */
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  /**
   * Test for empty function.
   */
  @Test
  public void testEmptyFunction() {
    String sourceCode = "function foo() {}";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);
  }

  /**
   * Test for dead assignment elimination in case of function call with
   * dead arguments.
   */
  @Test
  public void testDeadAssignmentEliminationFunctionCall() {
    String sourceCode = "function foo(x, y) { x = y; } function bar() { foo(1, 2); }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("bar.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertFalse(scope.getVar("x") != null);
  }

  /**
   * Test for dead assignment elimination in case of dead variable assignment.
   */
  @Test
  public void testDeadVariableAssignment() {
    String sourceCode = "function foo() { var x = 1; x = 2; }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertFalse(scope.getVar("x") != null);
  }

  /**
   * Test for dead assignment elimination in case of dead local variable assignment.
   */
  @Test
  public void testDeadLocalVariableAssignment() {
    String sourceCode = "function foo() { var x = 1; if (x) { x = 2; } }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertFalse(scope.getVar("x") != null);
  }

  /**
   * Test for dead assignment elimination in case of dead global variable assignment.
   */
  @Test
  public void testDeadGlobalVariableAssignment() {
    String sourceCode = "var x = 1; x = 2;";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("DeadAssignmentsEliminationTest.java")
        .getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope globalScope = NodeTraversal.getScopeAt(root, 0);
    assertFalse(globalScope.getVar("x") != null);
  }

  /**
   * Test for dead assignment elimination in case of dead assignment
   * within conditional statement.
   */
  @Test
  public void testDeadAssignmentWithinConditionalStatement() {
    String sourceCode = "function foo() { var x = 1; if (x) { x = 2; } }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node ifNode = NodeTraversal.findNode(root, Node.IF);
    DiGraphNode<Node, Branch> ifNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(ifNode);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        ifNodeInGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment
   * within loop statement.
   */
  @Test
  public void testDeadAssignmentWithinLoopStatement() {
    String sourceCode = "function foo() { var x = 1; for (var i = 0; i < 10; i++) { x = i; } }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node forNode = NodeTraversal.findNode(root, Node.FOR);
    DiGraphNode<Node, Branch> forNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(forNode);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        forNodeInGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertFalse(lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment within
   * switch statement.
   */
  @Test
  public void testDeadAssignmentWithinSwitchStatement() {
    String sourceCode = "function foo() { var x = 1; switch (x) { case 1: x = 2; break; } }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node switchNode = NodeTraversal.findNode(root, Node.SWITCH);
    DiGraphNode<Node, Branch> switchNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(switchNode);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        switchNodeInGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment within
   * return statement.
   */
  @Test
  public void testDeadAssignmentWithinReturnStatement() {
    String sourceCode = "function foo() { var x = 1; return x = 2; }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node returnNode = NodeTraversal.findNode(root, Node.RETURN);
    DiGraphNode<Node, Branch> returnNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(returnNode);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        returnNodeInGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertTrue(lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment within
   * multiple statements.
   */
  @Test
  public void testDeadAssignmentWithinMultipleStatements() {
    String sourceCode = "function foo() { var x = 1; x = 2; x = 3; }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node xAssignment1 = NodeTraversal.findNode(root, NodeUtil.matchRemovableAssigns);
    Node xAssignment2 = xAssignment1.getNext();

    DiGraphNode<Node, Branch> xAssignment1InGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(xAssignment1);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        xAssignment1InGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertTrue(lattice.isLive(scope.getVar("x")));

    DeadAssignmentsElimination.LiveVariableLattice previousLattice = lattice;
    lattice = null;
    for (Node currentNode = xAssignment2; currentNode != null;
         currentNode = currentNode.getNext()) {
      DiGraphNode<Node, Branch> currentNodeInGraph =
          NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(currentNode);
      state = currentNodeInGraph.getAnnotation();
      lattice = state != null ? state.getOut() : null;

      if (lattice != null && previousLattice != null) {
        assertFalse(lattice.isLive(scope.getVar("x")) ||
            previousLattice.isLive(scope.getVar("x")));
        break;
      }

      previousLattice = lattice;
    }

    assertTrue(lattice == null || lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment within
   * multiple nested statements.
   */
  @Test
  public void testDeadAssignmentWithinMultipleNestedStatements() {
    String sourceCode = "function foo() { var x = 1; if (true) { x = 2; } else if (true) { x = 3; } }";
    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);
    assertTrue(scope.getVar("x") != null);

    Node ifNode = NodeTraversal.findNode(root, Node.IF);

    DiGraphNode<Node, Branch> ifNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(ifNode);
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        ifNodeInGraph.getAnnotation();
    DeadAssignmentsElimination.LiveVariableLattice lattice = state.getOut();

    assertTrue(lattice.isLive(scope.getVar("x")));

    Node elseIfNode = NodeTraversal.findNode(ifNode.getChildNodes(), Node.IF);

    DiGraphNode<Node, Branch> elseIfNodeInGraph =
        NodeTraversal.getControlFlowGraph(root).getDirectedGraphNode(elseIfNode);
    state = elseIfNodeInGraph.getAnnotation();
    lattice = state != null ? state.getOut() : null;

    assertTrue(lattice == null || lattice.isLive(scope.getVar("x")));
  }

  /**
   * Test for dead assignment elimination in case of dead assignment within
   * complex nested statements using `let` and `const` variables.
   */
  @Test
  public void testDeadAssignmentWithComplexNestedStatements() {
    String sourceCode = "function foo() {" +
        "  let x = 1;" +
        "  if (true) {" +
        "    const y = 2;" +
        "    let z = 3;" +
        "    if (true) {" +
        "      z = 4;" +
        "    } else {" +
        "      x = y;" +
        "    }" +
        "  } else {" +
        "    const y = 5;" +
        "    let z = 6;" +
        "    if (true) {" +
        "      z = 7;" +
        "    } else {" +
        "      x = y;" +
        "    }" +
        "  }" +
        "  console.log(x);" +
        "}";

    CompilationResult result = compile(sourceCode);
    assertFalse(result.getErrorCount() > 0);

    Node root = result.getSourceFile("foo.js").getTree();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    Scope scope = NodeTraversal.getScopeAt(root, 0);

    // The variable 'x' should be alive in the end of the function.
    assertTrue(scope.getVar("x") != null);

    Node xAssignment =
        NodeTraversal.findNode(NodeTraversal.getScopeRoot(root).getLastChild(),
            NodeUtil.matchRemovableAssigns);

    // The dead assignment 'x = y' before the final assignment should be removed.
    assertFalse(xAssignment != null && xAssignment.getParent() != null);
  }

  private CompilationResult compile(String sourceCode) {
    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    return compiler.compile(new SourceFile[] { sourceFile });
  }
}