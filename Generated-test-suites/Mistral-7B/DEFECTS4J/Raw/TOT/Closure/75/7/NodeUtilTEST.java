import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(1, Node.newIdentifier("i"), Node.newExpressionStatement(Node.newExpression(1)), Node.newBlock(3));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x").setProperty(Node.newIdentifier("y"), Node.newNumber(5)));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x").set(Node.newNumber(5)));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newIdentifier("x").add(Node.newIdentifier("y")));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, Node.newIdentifier("myFunction"), Node.newBlock(2), Node.newStatementList());
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newFunction(1, Node.newIdentifier("y"), Node.newBlock(2), Node.newStatementList())));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, Node.newIdentifier("myFunction"), Node.newBlock(2), Node.newStatementList())
        .setParent(Node.newScript(Node.newTopLevelDeclarationList(Node.newFunctionDeclaration(Node.newIdentifier("myFunction"), Node.newBlock(0)))));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(1, Node.newIdentifier("myFunction"), Node.newBlock(2), Node.newStatementList())
        .setParent(Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newFunction(1, Node.newIdentifier("y"), Node.newBlock(2), Node.newStatementList()))));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}