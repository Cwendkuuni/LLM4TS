package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private Node root;
  private DeadAssignmentsElimination eliminator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    root = NodeFactory.createProgram();
    eliminator = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testSimpleDeadAssignment() {
    Node n1 = NodeFactory.createName("x");
    Node n2 = NodeFactory.createName("y");
    Node assignX = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createNumber(1));
    Node assignY = NodeFactory.createAssign(Token.ASSIGN, n2, NodeFactory.createNumber(1));
    Node assignXToDead = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createName("x"));

    root.appendChild(assignX);
    root.appendChild(assignY);
    root.appendChild(assignXToDead);

    eliminator.process(NodeUtil.getExterns(root), root);

    assertEquals(2, root.getChildCount());
    assertFalse(root.getChild(0).hasChild(assignXToDead));
  }

  @Test
  public void testDeadAssignmentInCondition() {
    Node n1 = NodeFactory.createName("x");
    Node assignX = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createNumber(1));
    Node condition = NodeFactory.createBinaryOp(Token.AND,
        NodeFactory.createName("y"), NodeFactory.createName("x"));
    Node assignXToDead = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createName("x"));

    root.appendChild(NodeFactory.createIfStatement(condition, null, null));
    root.appendChild(assignX);
    root.appendChild(assignXToDead);

    eliminator.process(NodeUtil.getExterns(root), root);

    assertEquals(2, root.getChildCount());
    assertFalse(root.getChild(0).hasChild(assignXToDead));
  }

  @Test
  public void testDeadAssignmentInWhileLoop() {
    Node n1 = NodeFactory.createName("x");
    Node assignX = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createNumber(1));
    Node condition = NodeFactory.createBinaryOp(Token.AND,
        NodeFactory.createName("y"), NodeFactory.createName("x"));
    Node whileStatement = NodeFactory.createWhileStatement(condition, null);
    Node assignXToDead = NodeFactory.createAssign(Token.ASSIGN, n1, NodeFactory.createName("x"));

    root.appendChild(whileStatement);
    root.getLastChild().appendChild(assignX);
    root.getLastChild().appendChild(assignXToDead);

    eliminator.process(NodeUtil.getExterns(root), root);

    assertEquals(2, root.getChildCount());
    assertFalse(root.getChild(0).hasChild(assignXToDead));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExterns() {
    eliminator.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    eliminator.process(NodeUtil.getExterns(root), null);
  }
}