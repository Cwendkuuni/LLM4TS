import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  private Node createNode(int type, Node... children) {
    Node n = new Node();
    n.setType(type);
    n.setChildren(children);
    return n;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, createNode(Token.IDENT, "for"), createNode(Token.ASSIGN, Token.IDENT, "i"), createNode(Token.KEYWORD, "in"), createNode(Token.IDENT, "array"));
    assertTrue(isForIn(forNode));

    Node notForNode = createNode(Token.FUNCTION);
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(Token.SCRIPT);
    Node statementNode = createNode(Token.PRINT, createNode(Token.STRING, "hello world"));
    Node expressionNode = createNode(Token.ASSIGN, createNode(Token.IDENT, "x"), createNode(Token.NUMBER, 5));

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "MyFunction"), createNode(Token.BLOCK, createNode(Token.RETURN, createNode(Token.NUMBER, 42))));
    assertTrue(isFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "MyFunction"), createNode(Token.BLOCK, createNode(Token.RETURN, createNode(Token.NUMBER, 42))));
    assertFalse(isFunctionDeclaration(functionExpressionNode));

    Node notFunctionNode = createNode(Token.IF);
    assertFalse(isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Token.SCRIPT, createNode(Token.FUNCTION, createNode(Token.IDENT, "MyFunction"), createNode(Token.BLOCK, createNode(Token.RETURN, createNode(Token.NUMBER, 42)))));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node nonHoistedFunctionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "MyFunction"), createNode(Token.BLOCK, createNode(Token.RETURN, createNode(Token.NUMBER, 42))), createNode(Token.VAR, createNode(Token.IDENT, "MyFunction"), createNode(Token.FUNCTION, createNode(Token.IDENT, "MyFunction"), createNode(Token.BLOCK, createNode(Token.RETURN, createNode(Token.NUMBER, 42))))));
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionNode));

    Node notFunctionNode = createNode(Token.IF);
    assertFalse(isHoistedFunctionDeclaration(notFunctionNode));
  }
}