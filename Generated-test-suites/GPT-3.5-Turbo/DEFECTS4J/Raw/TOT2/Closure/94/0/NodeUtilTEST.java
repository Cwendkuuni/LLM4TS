import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsNotForIn() {
    Node notForInNode = new Node(Token.IF);

    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node parentScript = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    parentScript.addChildToBack(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsNotStatement() {
    Node parentExpression = new Node(Token.EXPR_RESULT);
    Node expressionNode = new Node(Token.EMPTY);
    parentExpression.addChildToBack(expressionNode);

    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    Node parentStatement = new Node(Token.SCRIPT);
    parentStatement.addChildToBack(functionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node functionExpression = new Node(Token.FUNCTION);
    Node parentExpression = new Node(Token.EXPR_RESULT);
    parentExpression.addChildToBack(functionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.addChildToBack(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    Node parentFunction = new Node(Token.FUNCTION);
    parentFunction.addChildToBack(functionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }
}