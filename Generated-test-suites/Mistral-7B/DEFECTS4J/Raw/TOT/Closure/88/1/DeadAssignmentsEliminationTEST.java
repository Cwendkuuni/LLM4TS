package com.google.javascript.jscomp. DeadAssignmentsEliminationTest;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.javascript.rhino.NodeUtil.makeString;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private CompilerPass deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testSimpleAssignment() {
    String source = "function foo() { var x = 1; x = 2; }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(1, cfg.size());

    Node node = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node.getType()), "assign");
    assertEquals(node.getFirstChild().getString(), "x");
    assertEquals(node.getFirstChild(1).getString(), "2");
    assertEquals(node.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node.getParent()));
  }

  @Test
  public void testSimpleAssignmentWithDeadVariable() {
    String source = "function foo() { var x = 1; var y = x; x = 2; }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(2, cfg.size());

    Node node1 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node1.getType()), "assign");
    assertEquals(node1.getFirstChild().getString(), "y");
    assertEquals(node1.getFirstChild(1).getString(), "1");
    assertEquals(node1.getParent().getLastChild().getString(), "y");

    Node node2 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node2.getType()), "assign");
    assertEquals(node2.getFirstChild().getString(), "x");
    assertEquals(node2.getFirstChild(1).getString(), "2");
    assertEquals(node2.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node1.getParent()));
    assertEquals(1, NodeTraversal.getChildrenCount(node2.getParent()));
  }

  @Test
  public void testDeadAssignmentInFunctionBody() {
    String source = "function foo() { var x = 1; x = x; }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(1, cfg.size());

    Node node = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node.getType()), "assign");
    assertEquals(node.getFirstChild().getString(), "x");
    assertEquals(node.getFirstChild(1).getString(), "1");
    assertEquals(node.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node.getParent()));
  }

  @Test
  public void testDeadAssignmentInInnerFunction() {
    String source = "function foo() {" +
        "  function bar() { var x = 1; x = x; } }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(1, cfg.size());

    Node node = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node.getType()), "function");
    assertEquals(node.getLastChild().getString(), "bar");

    Node barNode = Lists.newArrayList(node.getChildren()).get(0);

    assertEquals(1, barNode.getChildrenCount());
    Node barChild = barNode.getChildren().get(0);

    assertEquals(NodeUtil.getOpString(barChild.getType()), "assign");
    assertEquals(barChild.getFirstChild().getString(), "x");
    assertEquals(barChild.getFirstChild(1).getString(), "1");
    assertEquals(barChild.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(barNode));
  }

  @Test
  public void testDeadAssignmentInIfStatement() {
    String source = "function foo() {" +
        "  if (true) {" +
        "    var x = 1; x = x; } }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(2, cfg.size());

    Node node1 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node1.getType()), "if");

    Node node2 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node2.getType()), "assign");
    assertEquals(node2.getFirstChild().getString(), "x");
    assertEquals(node2.getFirstChild(1).getString(), "1");
    assertEquals(node2.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node1.getParent()));
    assertEquals(1, NodeTraversal.getChildrenCount(node2.getParent()));
  }

  @Test
  public void testDeadAssignmentInForLoop() {
    String source = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var x = 1; x = x; } }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(2, cfg.size());

    Node node1 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node1.getType()), "for");

    Node node2 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node2.getType()), "assign");
    assertEquals(node2.getFirstChild().getString(), "x");
    assertEquals(node2.getFirstChild(1).getString(), "1");
    assertEquals(node2.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node1.getParent()));
    assertEquals(1, NodeTraversal.getChildrenCount(node2.getParent()));
  }

  @Test
  public void testDeadAssignmentInWhileLoop() {
    String source = "function foo() {" +
        "  while (true) {" +
        "    var x = 1; x = x; } }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(1, cfg.size());

    Node node = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node.getType()), "while");

    assertEquals(1, NodeTraversal.getChildrenCount(node.getParent()));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInReturnStatement() {
    String source = "function foo() {" +
        "  return var x = 1; x = x; }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentInSwitchStatement() {
    String source = "function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      var x = 1; x = x; break;" +
        "  } }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);
  }

  @Test
  public void testDeadAssignmentInFunctionCallExpression() {
    String source = "function foo() {" +
        "  var x = 1; x = x; " +
        "  print(x); }";
    Node root = RhinoCompilerTestUtil.compileToNode(source, compiler);

    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    assertEquals(3, cfg.size());

    Node node1 = cfg.getGraphNodes().iterator().next().getValue();
    assertEquals(NodeUtil.getOpString(node1.getType()), "assign");
    assertEquals(node1.getFirstChild().getString(), "x");
    assertEquals(node1.getFirstChild(1).getString(), "1");
    assertEquals(node1.getParent().getLastChild().getString(), "x");

    Node node2 = cfg.getGraphNodes().get(1).getValue();
    assertEquals(NodeUtil.getOpString(node2.getType()), "call");
    assertEquals(node2.getLastChild().getString(), "print");
    assertEquals(1, NodeTraversal.getChildrenCount(node2.getParent()));
    assertEquals(1, NodeTraversal.getChildrenCount(node1.getParent()));

    Node node3 = cfg.getGraphNodes().get(2).getValue();
    assertEquals(NodeUtil.getOpString(node3.getType()), "assign");
    assertEquals(node3.getFirstChild().getString(), "x");
    assertEquals(node3.getFirstChild(1).getString(), "1");
    assertEquals(node3.getParent().getLastChild().getString(), "x");

    assertEquals(1, NodeTraversal.getChildrenCount(node2.getParent()));
    assertEquals(1, NodeTraversal.getChildrenCount(node3.getParent()));
  }
}