import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {
  @Test
  public void testEmptyMethod() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    NodeTraversal.traverse(null, root, elimination);
    assertNull(NodeUtil.getFirstChild(root));
  }

  @Test
  public void testFunctionWithReturnStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    Node body = Node.newBlock(new Node[0]);
    Node returnStatement = Node.newReturnStatement(Node.newLiteral(42));
    body.addChildToEnd(returnStatement);
    root.addChildToEnd(body);

    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    elimination.traverse(NodeTraversal.empty, root, null);

    assertNull(NodeUtil.getFirstChild(returnStatement));
    assertEquals(42, ((NodeLiteral) NodeUtil.getFirstChild(body)).getValue().intValue());
  }

  @Test
  public void testFunctionWithDeadCode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    Node body = Node.newBlock(new Node[0]);
    Node deadCode = Node.newExpressionStatement(Node.newName("x").setType(IDENTIFIER), Node.newLiteral(42));
    body.addChildToEnd(Node.newExpressionStatement(Node.newName("y").setType(IDENTIFIER), Node.newNumberLiteral(42)));
    body.addChildToEnd(deadCode);
    root.addChildToEnd(body);

    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    elimination.traverse(NodeTraversal.empty, root, null);

    assertNull(NodeUtil.getFirstChild(deadCode));
  }

  @Test(expected = RuntimeException.class)
  public void testFunctionWithExceptionThrown() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    Node body = Node.newBlock(new Node[0]);
    Node throwStatement = Node.newThrowStatement(Node.newName("e").setType(IDENTIFIER));
    body.addChildToEnd(Node.newExpressionStatement(Node.newName("y").setType(IDENTIFIER), Node.newNumberLiteral(42)));
    body.addChildToEnd(throwStatement);
    root.addChildToEnd(body);

    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    elimination.traverse(NodeTraversal.empty, root, null);
  }

  @Test
  public void testFunctionWithBreakStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    Node body = Node.newBlock(new Node[0]);
    Node loop = Node.newForStatement(Node.newName("i").setType(IDENTIFIER), Node.newLiteral(0), Node.newLiteral(1), Node.newExpressionStatement(Node.newAssignStatement(Node.newName("i").setType(IDENTIFIER), Node.newNumberLiteral(1))));
    Node breakStatement = Node.newBreakStatement();
    body.addChildToEnd(loop);
    body.addChildToEnd(breakStatement);
    root.addChildToEnd(body);

    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    elimination.traverse(NodeTraversal.empty, root, null);

    assertEquals(1, ((NodeConstantExpression) NodeUtil.getFirstChild(loop)).getIntValue().intValue());
    assertNull(NodeUtil.getFirstChild(breakStatement));
  }

  @Test
  public void testFunctionWithContinueStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newFunction(null, new Node[0], null);
    Node body = Node.newBlock(new Node[0]);
    Node loop = Node.newForStatement(Node.newName("i").setType(IDENTIFIER), Node.newLiteral(0), Node.newLiteral(1), Node.newExpressionStatement(Node.newAssignStatement(Node.newName("i").setType(IDENTIFIER), Node.newNumberLiteral(1))));
    Node continueStatement = Node.newContinueStatement();
    body.addChildToEnd(loop);
    body.addChildToEnd(continueStatement);
    root.addChildToEnd(body);

    NodeTraversal traversal = new NodeTraversal(root);
    elimination.process(null, root);
    elimination.traverse(NodeTraversal.empty, root, null);

    assertEquals(1, ((NodeConstantExpression) NodeUtil.getFirstChild(loop)).getIntValue().intValue());
    assertNull(NodeUtil.getFirstChild(continueStatement));
  }
}