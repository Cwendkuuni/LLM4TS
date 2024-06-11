package com.google.javascript.jscomp.deadassignmentselimination;

import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Node root;
  private DeadAssignmentsElimination eliminator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    cfg = new ControlFlowGraph<>();
    root = new Node(Token.PROGRAM, null);
    eliminator = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testProcess() {
    eliminator.process(null, root);
  }

  @Test
  public void testEnterScope() {
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
  }

  @Test
  public void testExitScope() {
    eliminator.exitScope(new NodeTraversal(cfg, root) {});
  }

  @Test
  public void testVisit() {
    Node n = new Node(Token.IF, new Node(Token.EQ,
        new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)),
        new Node(Token.NAME, "y"));
    eliminator.visit(new NodeTraversal(cfg, root) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        this.n = n;
      }
    }, n, root);
    assertTrue(n.equals(this.n));
  }

  @Test
  public void testTryRemoveDeadAssignmentsNullConfigGraph() {
    eliminator = new DeadAssignmentsElimination(compiler);
    try {
      eliminator.tryRemoveDeadAssignments(new NodeTraversal(cfg, root) {}, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testTryRemoveDeadAssignmentsNullState() {
    eliminator = new DeadAssignmentsElimination(compiler);
    ControlFlowGraph<Node> cfgNode = new ControlFlowGraph.Node<>(null, root);
    FlowState<LiveVariableLattice> state = null;
    eliminator.tryRemoveDeadAssignments(new NodeTraversal(cfg, root) {}, cfgNode);
  }

  @Test
  public void testTryRemoveAssignmentOutOfScope() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    eliminator.exitScope(new NodeTraversal(cfg, root) {});

    Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
        new Node(Token.INT_LITERAL, 1));
    eliminator.visit(new NodeTraversal(cfg, root) {}, n, root);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, n);
    assertTrue(n.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentDeadVariable() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariable() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(new Node(Token.NAME, "y"));
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariableBeforeAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(read);
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() == null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariableAfterAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    Node read = new Node(Token.NAME, "x");
    root.addChild(assignment);
    root.addChild(read);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentDeadVariableReadBeforeAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(read);
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, read);
    assertTrue(read.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentDeadVariableReadAfterAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(assignment);
    root.addChild(read);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, read);
    assertTrue(read.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariableReadBeforeAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read1 = new Node(Token.NAME, "x");
    Node read2 = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(read1);
    root.addChild(read2);
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() == null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariableReadAfterAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read1 = new Node(Token.NAME, "x");
    Node read2 = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(assignment);
    root.addChild(read1);
    root.addChild(read2);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, assignment);
    assertTrue(assignment.getParent() != null);
  }

  @Test
  public void testTryRemoveAssignmentLiveVariableReadBeforeAndAfterAssignment() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node lhs = new Node(Token.NAME, "x");
    Node rhs = new Node(Token.INT_LITERAL, 1);
    Node read1 = new Node(Token.NAME, "x");
    Node read2 = new Node(Token.NAME, "x");
    Node assignment = new Node(Token.ASSIGN, lhs, rhs);
    root.addChild(read1);
    root.addChild(read2);
    root.addChild(assignment);
    eliminator.tryRemoveAssignment(new NodeTraversal(cfg, root) {}, read1);
    assertTrue(assignment.getParent() == null);
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.enterScope(new NodeTraversal(cfg, root) {});
    Node x = new Node(Token.NAME, "x");
    Node assignment1 = new Node(Token.ASSIGN, x, new Node(Token.INT_LITERAL, 1));
    Node read = new Node(Token.NAME, "x");
    Node assignment2 = new Node(Token.ASSIGN, x, new Node(Token.INT_LITERAL, 2));
    root.addChild(x);
    root.addChild(assignment1);
    root.addChild(read);
    root.addChild(assignment2);

    assertTrue(eliminator.isVariableStillLiveWithinExpression(assignment1, root, "x"));
    assertTrue(eliminator.isVariableStillLiveWithinExpression(assignment2, root, "x"));
    assertFalse(eliminator.isVariableStillLiveWithinExpression(read, root, "x"));
  }

  private Node n;
}