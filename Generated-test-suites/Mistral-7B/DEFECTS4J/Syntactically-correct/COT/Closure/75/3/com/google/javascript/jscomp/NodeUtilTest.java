import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF);
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.setChild(0, nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION);
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node nodeStatement = new Node(Token.IF);
    Preconditions.checkArgument(nodeStatement.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    Node parentBlock = new Node(Token.BLOCK);
    parentBlock.setChild(0, nodeFunctionDeclaration);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION);
    Node parentExpression = new Node(Token.ASSIGN);
    parentExpression.setLeft(nodeFunctionExpression);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    Preconditions.checkArgument(nodeFunctionDeclaration.getParent() != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    nodeHoistedFunctionDeclaration.setProperty("id", "myFunction");
    Node parentScript = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setProperty("id", "innerFunction");
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);
    parentScript.setChild(1, functionNode);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION);
    nodeFunctionDeclarationInFunction.setProperty("id", "innerFunction");
    Node parentFunction = new Node(Token.FUNCTION);
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    Preconditions.checkArgument(nodeFunctionDeclaration.getParent() != null);
  }
}