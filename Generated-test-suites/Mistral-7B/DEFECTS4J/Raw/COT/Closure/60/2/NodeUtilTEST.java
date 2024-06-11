import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node nodeWithThreeChildren = new Node(Token.FOR, new Node[3]);

    assertTrue(isForIn(nodeWithThreeChildren));
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    Node functionNode = new Node(Token.FUNCTION, new Node("testFunction"));
    Node expressionNode = new Node(Token.IDENT, "testVariable");

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(blockNode));
    assertTrue(isStatement(labelNode));
    assertFalse(isStatement(functionNode));
    assertFalse(isStatement(expressionNode));
  }

  @Test(expected = RuntimeException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(Token.SCRIPT);
    Preconditions.checkState(node.getParent() != null);
    node.getParent() = null;
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new Node("testFunction"));
    Node functionExpression = new Node(Token.FUNCTION, new Node("testFunction"), new Node("{}"));

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new Node("testFunction"), new Node("{}"), null, null);
    Node functionDeclaration = new Node(Token.FUNCTION, new Node("testFunction"), new Node("{}"));
    Node functionDeclarationInFunction = new Node(
        Token.FUNCTION, new Node("outerFunction"), new Node("{}"), functionDeclaration, null);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}