import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node("init"));
    forNode.setChild(1, new Node("condition"));
    forNode.setChild(2, new Node("next"));

    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF);
    Node scriptNode = new Node(SCRIPT);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node expressionNode = new Node(Token.ADD);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node statementNode = new Node(Token.IF);
    statementNode.setParent(null);

    NodeUtil.isStatement(statementNode);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(FunctionDeclaration, "myFunction");
    Node functionExpression = new Node(FunctionExpression, "myFunction");
    Node functionStatement = new Node(Function, "myFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(functionStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Script, null, new Node(FunctionDeclaration, "myFunction"));
    Node functionDeclarationInFunction = new Node(FunctionDeclaration, "myFunction");
    Node functionDeclarationOutsideFunction = new Node(FunctionDeclaration, "myFunction");

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getChild(0)));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationOutsideFunction));
  }
}