import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
    Node parentStatement = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    parentStatement.addChildToEnd(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENT, new int[]{}, new int[0]);
    Node parentExpression = new Node(Token.ADD, new int[]{}, new int[2]);
    parentExpression.addChildToEnd(nodeNotStatement);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeFunctionDeclaration.addChildToEnd(new Node(Token.IDENT, new int[]{"identifier"}, new int[0]));
    nodeFunctionDeclaration.addChildToEnd(new Node(Token.BLOCK, new int[]{}, new int[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeHoistedFunctionDeclaration.addChildToEnd(new Node(Token.IDENT, new int[]{"identifier"}, new int[0]));
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    scriptNode.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeNotHoistedFunctionDeclaration.addChildToEnd(new Node(Token.IDENT, new int[]{"identifier"}, new int[0]));
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    functionNode.addChildToEnd(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  // Add edge cases and exception handling tests here

  // For isForIn()
  @org.junit.Test
  public void testIsForInWithLessThanThreeChildren() {
    Node nodeForInWithLessThanThreeChildren = new Node(Token.FOR, new int[]{}, new int[2]);
    assertFalse(NodeUtil.isForIn(nodeForInWithLessThanThreeChildren));
  }

  @org.junit.Test
  public void testIsForInWithInvalidType() {
    Node nodeInvalidTypeForIn = new Node(Token.IF, new int[]{}, new int[3]);
    assertFalse(NodeUtil.isForIn(nodeInvalidTypeForIn));
  }

  // For isStatement()
  @org.junit.Test
  public void testIsStatementWithNullParent() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isStatement(nodeStatement));
  }

  @org.junit.Test
  public void testIsStatementWithInvalidParentType() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
    Node parentInvalidType = new Node(Token.IF, new int[]{}, new int[0]);
    nodeStatement.setParent(parentInvalidType);
    assertFalse(NodeUtil.isStatement(nodeStatement));
  }

  // For isFunctionDeclaration()
  @org.junit.Test
  public void testIsFunctionDeclarationWithNoName() {
    Node nodeFunctionDeclarationNoName = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNoName));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithNoBlock() {
    Node nodeFunctionDeclarationNoBlock = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeFunctionDeclarationNoBlock.setLastChild(null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNoBlock));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithInvalidType() {
    Node nodeInvalidTypeFunctionDeclaration = new Node(Token.VAR, new int[]{"identifier"}, new int[1]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeInvalidTypeFunctionDeclaration));
  }

  // For isHoistedFunctionDeclaration()
  @org.junit.Test
  public void testIsHoistedFunctionDeclarationWithNoParentStatement() {
    Node nodeHoistedFunctionDeclarationNoParentStatement = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeHoistedFunctionDeclarationNoParentStatement.addChildToEnd(new Node(Token.IDENT, new int[]{"identifier"}, new int[0]));
    nodeHoistedFunctionDeclarationNoParentStatement.addChildToEnd(new Node(Token.BLOCK, new int[]{}, new int[0]));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclarationNoParentStatement));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationWithInvalidGrandParentType() {
    Node nodeHoistedFunctionDeclarationWithInvalidGrandParentType = new Node(Token.FUNCTION, new int[]{"identifier"}, new int[1]);
    nodeHoistedFunctionDeclarationWithInvalidGrandParentType.addChildToEnd(new Node(Token.IDENT, new int[]{"identifier"}, new int[0]));
    Node grandParentInvalidType = new Node(Token.IF, new int[]{}, new int[0]);
    nodeHoistedFunctionDeclarationWithInvalidGrandParentType.setParent(grandParentInvalidType);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclarationWithInvalidGrandParentType));
  }
}