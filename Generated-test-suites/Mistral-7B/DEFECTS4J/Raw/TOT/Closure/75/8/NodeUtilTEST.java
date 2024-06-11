import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new Node[]{
      new Node(Token.IDENT, "i"),
      new Node(Token.KEYWORD, Token.IN),
      new Node(Token.IDENT, "array")
    });
    assertTrue(isForIn(forNode));

    Node notForNode = new Node(Token.FUNCTION, new Node[]{});
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node[]{
      new Node(Token.STRING_LITERAL, "Hello World")
    });
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.PLUS, new Node[]{
      new Node(Token.NUMBER_LITERAL, 1),
      new Node(Token.NUMBER_LITERAL, 2)
    });
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{
      new Node(Token.IDENT, "myFunction"),
      new Node(Token.BLOCK, new Node[]{
        new Node(Token.KEYWORD, Token.RETURN),
        new Node(Token.NUMBER_LITERAL, 42)
      })
    });
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[]{
      new Node(Token.IDENT, "myFunction"),
      new Node(Token.BLOCK, new Node[]{
        new Node(Token.KEYWORD, Token.RETURN),
        new Node(Token.NUMBER_LITERAL, 42)
      })
    }, new Node[]{new Node(Token.IDENT, "someVariable")});
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node[]{
      new Node(Token.FUNCTION, new Node[]{
        new Node(Token.IDENT, "myFunction"),
        new Node(Token.BLOCK, new Node[]{
          new Node(Token.KEYWORD, Token.RETURN),
          new Node(Token.NUMBER_LITERAL, 42)
        })
      })
    });
    Node functionDeclarationNode = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));

    Node blockNode = new Node(Token.BLOCK, new Node[]{
      new Node(Token.FUNCTION, new Node[]{
        new Node(Token.IDENT, "myFunction"),
        new Node(Token.BLOCK, new Node[]{
          new Node(Token.KEYWORD, Token.RETURN),
          new Node(Token.NUMBER_LITERAL, 42)
        })
      })
    });
    Node functionDeclarationNodeInBlock = blockNode.getChild(0);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNodeInBlock));
  }
}