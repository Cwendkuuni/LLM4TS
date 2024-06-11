import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().mkNode(
        Token.FOR,
        new Node[]{
          Context.getCurrentContext().mkNode(Token.IDENT, "i"),
          Context.getCurrentContext().mkNode(Token.KEYWORD, "in"),
          Context.getCurrentContext().mkNode(Token.IDENT, "array")
        }
    );
    assertTrue(isForIn(node));

    Node invalidNode = Context.getCurrentContext().mkNode(Token.IF, null);
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Context.getCurrentContext().mkNode(Token.NUMBER, 10);
    assertTrue(isStatement(statementNode));

    Node expressionNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName")
    });
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName"),
      Context.getCurrentContext().mkNode(Token.BLOCK, null)
    });
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName")
    });
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().mkNode(Token.SCRIPT, null);
    Node functionDeclarationNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName"),
      Context.getCurrentContext().mkNode(Token.BLOCK, null)
    });
    Node hoistedFunctionDeclarationNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName"),
      Context.getCurrentContext().mkNode(Token.BLOCK, null)
    });
    Node functionNode = Context.getCurrentContext().mkNode(Token.FUNCTION, null, new Node[]{
      Context.getCurrentContext().mkNode(Token.IDENT, "funcName")
    });

    scriptNode.addChild(functionDeclarationNode);
    scriptNode.addChild(hoistedFunctionDeclarationNode);
    scriptNode.addChild(functionNode);

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}