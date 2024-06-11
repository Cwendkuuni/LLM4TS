import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, "i"),
      Node.newNode(Token.KEYWORD, "in"),
      Node.newNode(Token.IDENT, "array")
    });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, "i"),
      Node.newNode(Token.ASSIGN, null, new Node[]{
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.LITERAL, 1)
      }),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{
        Node.newNode(Token.STATEMENT, null, new Node[]{
          Node.newNode(Token.PRINT, null, new Node[]{
            Node.newNode(Token.STRING_LITERAL, "Hello, world!")
          })
        })
      })
    });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, null, new Node[]{
      Node.newNode(Token.STRING_LITERAL, "Hello, world!")
    });
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, new int[]{}, new Node[]{
      Node.newNode(Token.NUMBER_LITERAL, 1),
      Node.newNode(Token.NUMBER_LITERAL, 2)
    });
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, "myFunction"),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}),
      Node.newNode(Token.RBRACE)
    });
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, "myFunction"),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}),
      Node.newNode(Token.RBRACE),
      Node.newNode(Token.IDENT, "myFunction")
    });
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{
      Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}),
        Node.newNode(Token.RBRACE)
      }),
      Node.newNode(Token.PRINT, null, new Node[]{
        Node.newNode(Token.IDENT, "myFunction")
      })
    });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration.getFirstChild()));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.BLOCK, new int[]{}, new Node[]{
      Node.newNode(Token.VAR, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
          Node.newNode(Token.IDENT, "myFunction"),
          Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}),
          Node.newNode(Token.RBRACE)
        })
      }),
      Node.newNode(Token.PRINT, null, new Node[]{
        Node.newNode(Token.IDENT, "myFunction")
      })
    });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration.getFirstChild().getFirstChild()));
  }
}