import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1, 2});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{0, 1, 2});
    Node parentStatement = Node.newNode(Token.BLOCK, new int[]{});
    parentStatement.addChild(nodeStatement);

    assertTrue(isStatement(nodeStatement));
    assertTrue(isStatement(parentStatement));

    Node nodeNotStatement = Node.newNode(Token.IDENT, new int[]{0, "id"});
    Node parentExpression = Node.newNode(Token.ASSIGN, new int[]{0, 1, 2});
    parentExpression.addChild(nodeNotStatement);

    assertFalse(isStatement(nodeNotStatement));
    assertFalse(isStatement(parentExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1, 2, 3});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2});
    parentScript.addChild(nodeFunctionDeclaration);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(Node.newNode(Token.IDENT, new int[]{0, "id"})));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1, 2, 3});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{0, 1, 2});
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1, 2, 3});
    functionNode.setName("foo");
    parentScript.addChild(functionNode);
    parentScript.addChild(nodeFunctionDeclaration);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1, 2, 3});
    parentFunction.addChild(nodeFunctionDeclaration);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}