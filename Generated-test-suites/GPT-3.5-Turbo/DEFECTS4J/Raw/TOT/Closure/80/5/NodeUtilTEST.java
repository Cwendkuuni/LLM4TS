import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.VAR, new Node(Token.NAME)));
    forInNode.addChildToBack(new Node(Token.TRUE));
    forInNode.addChildToBack(new Node(Token.NULL));

    boolean result = NodeUtil.isForIn(forInNode);

    assertTrue(result);
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    Node notForInNode = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isForIn(notForInNode);

    assertFalse(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.NAME);

    scriptNode.addChildToBack(statementNode);

    boolean result = NodeUtil.isStatement(statementNode);

    assertTrue(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
    Node notStatementNode = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.NOT, notStatementNode);

    boolean result = NodeUtil.isStatement(notStatementNode);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(functionDeclarationNode);

    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    Node notFunctionDeclarationNode = new Node(Token.TRUE);

    boolean result = NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionDeclarationNode);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(scriptNode);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(notHoistedFunctionDeclarationNode);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode);

    assertFalse(result);
  }

}