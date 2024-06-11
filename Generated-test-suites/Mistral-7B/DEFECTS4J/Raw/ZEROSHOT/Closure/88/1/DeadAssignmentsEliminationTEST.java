import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeOp.ASSIGN;
import static com.google.javascript.rhino.NodeOp.INC;
import static com.google.javascript.rhino.NodeOp.DEC;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Scope scope = new Scope();
    Node root = new Node(TOKEN_PROGRAM, null);

    Node n1 = new Node(IF, new Node(TOKEN_IF, new Node(TOKEN_NAME, "x"), null),
            new Node(Token.BLOCK, null));
    Node n2 = new Node(ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.NUMBER, 1));
    n1.setFirstChild(n2);
    n1.getLastChild().setNext(new Node(TOKEN_NAME, "y"));
    n1.getLastChild().setParent(n1);

    Node n3 = new Node(ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.NAME, "y"));
    Node n4 = new Node(Token.BLOCK, null);
    n4.setNext(n3);

    Node n5 = new Node(TOKEN_RETURN, null);
    n4.setLastChild(n5);
    n1.setLastChild(n4);

    root.setNext(n1);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == TOKEN_IF) {
          n.setFirstChild(null);
          n.setLastChild(null);
          n.getParent().replaceChild(n, n.getFirstChild());
        }
      }
    }, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == n1) {
          deadAssignmentsElimination.enterScope(t);
        }
        if (n == n4) {
          deadAssignmentsElimination.exitScope(t);
        }
      }
    });
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    // The dead assignment elmination should have removed the if statement
    // and return the value of y instead.
    assertTrue(root.getType() == TOKEN_NAME);
    assertTrue(root.getString().equals("y"));

    // Check that the variable x is still there
    Node x = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_NAME && node.getString().equals("x")) {
        x = node;
        break;
      }
    }
    assertTrue(x != null);

    // Check that the assignments to x are still there
    Node xAssignments = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == ASSIGN &&
          node.getFirstChild().getType() == TOKEN_NAME &&
          node.getFirstChild().getString().equals("x")) {
        xAssignments = node;
        break;
      }
    }
    assertTrue(xAssignments != null);

    // Check that the dead assignment elimination did not remove the
    // assignments to x
    assertFalse(xAssignments.getLastChild() == null);

    // Check that the dead assignment elimination did not remove the
    // variable y
    Node y = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_NAME &&
          node.getString().equals("y")) {
        y = node;
        break;
      }
    }
    assertTrue(y != null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithIncrementDecrement() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Scope scope = new Scope();
    Node root = new Node(TOKEN_PROGRAM, null);

    Node n1 = new Node(IF, new Node(Token.IF, new Node(TOKEN_NAME, "x"), null),
            new Node(Token.BLOCK, null));
    Node n2 = new Node(ASSIGN, new Node(Token.NAME, "x"), new Node(INC, null));
    Node n3 = new Node(ASSIGN, new Node(Token.NAME, "x"), new Node(DEC, null));
    n1.setFirstChild(n2);
    n1.getLastChild().setNext(n3);

    Node n4 = new Node(Token.BLOCK, null);
    Node n5 = new Node(Token.NAME, "y");
    n4.setNext(n5);
    n1.setLastChild(n4);

    Node n6 = new Node(Token.RETURN, null);
    n4.setLastChild(n6);

    root.setNext(n1);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == TOKEN_IF) {
          n.setFirstChild(null);
          n.setLastChild(null);
          n.getParent().replaceChild(n, n.getFirstChild());
        }
      }
    }, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == n1) {
          deadAssignmentsElimination.enterScope(t);
        }
        if (n == n4) {
          deadAssignmentsElimination.exitScope(t);
        }
      }
    });
    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    // The dead assignment elimination should have removed the if statement
    // and return the value of y instead.
    assertTrue(root.getType() == TOKEN_NAME);
    assertTrue(root.getString().equals("y"));

    // Check that the variable x is still there
    Node x = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_NAME &&
          node.getString().equals("x")) {
        x = node;
        break;
      }
    }
    assertTrue(x != null);

    // Check that the increment and decrement assignments to x are still there
    Node xIncrement = null;
    Node xDecrement = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == ASSIGN &&
          node.getFirstChild().getType() == INC) {
        xIncrement = node;
      } else if (node.getType() == ASSIGN &&
          node.getFirstChild().getType() == DEC) {
        xDecrement = node;
      }
      if (xIncrement != null && xDecrement != null) {
        break;
      }
    }
    assertTrue(xIncrement != null && xDecrement != null);

    // Check that the dead assignment elimination did not remove the
    // increment and decrement assignments to x
    assertFalse(xIncrement.getLastChild() == null);
    assertFalse(xDecrement.getLastChild() == null);

    // Check that the variable y is still there
    Node y = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_NAME &&
          node.getString().equals("y")) {
        y = node;
        break;
      }
    }
    assertTrue(y != null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Scope scope = new Scope();
    Node root = new Node(TOKEN_PROGRAM, null);

    Node n1 = new Node(Token.VAR, new Node(Token.NAME, "x"), null);
    Node n2 = new Node(ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 1));
    Node n3 = new Node(Token.FUNCTION, null, new Node(TOKEN_BLOCK, null));
    Node n4 = new Node(Token.NAME, "x");
    Node n5 = new Node(ASSIGN, n4, new Node(Token.NUMBER, 2));
    Node n6 = new Node(TOKEN_RETURN, n4);
    n3.setFirstChild(n2);
    n3.setNext(n4);
    n3.getLastChild().setNext(n5);
    n3.getLastChild().setParent(n3);
    n3.getLastChild().getNext().setParent(n3);
    n2.setParent(n3);
    n1.setLastChild(n3);
    root.setNext(n1);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == TOKEN_FUNCTION) {
          n.setFirstChild(null);
          n.setLastChild(null);
          n.getParent().replaceChild(n, n.getFirstChild());
        }
      }
    }, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == n1) {
          deadAssignmentsElimination.enterScope(t);
        }
        if (n == n3) {
          deadAssignmentsElimination.exitScope(t);
        }
      }
    });

    deadAssignmentsElimination.process(null, root);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    // The dead assignment elimination should have removed the function and
    // returned the variable x instead.
    assertTrue(root.getType() == TOKEN_NAME);
    assertTrue(root.getString().equals("x"));

    // Check that the variable x is still there
    Node x = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_NAME &&
          node.getString().equals("x")) {
        x = node;
        break;
      }
    }
    assertTrue(x != null);

    // Check that the assignment to x is still there
    Node xAssignment = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == ASSIGN &&
          node.getFirstChild().getType() == TOKEN_NAME &&
          node.getFirstChild().getString().equals("x")) {
        xAssignment = node;
        break;
      }
    }
    assertTrue(xAssignment != null);

    // Check that the function definition is no longer present
    Node functionDefinition = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == TOKEN_FUNCTION) {
        functionDefinition = node;
        break;
      }
    }
    assertTrue(functionDefinition == null);
  }
}