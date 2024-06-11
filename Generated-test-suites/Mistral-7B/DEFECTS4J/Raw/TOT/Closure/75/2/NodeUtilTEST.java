import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] { 0, 1, 2 }, new Node[] {
        Node.newNode(Token.IDENT, "forVar"),
        Node.newNode(Token.ASSIGN, new int[] { 0 }, new Node[] {
          Node.newNode(Token.IDENT, "array"),
          Node.newNode(Token.KEYWORD, Token.IN)
        }),
        Node.newNode(Token.STATEMENT, new int[] {}, null)
    });

    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
      Node.newNode(Token.IDENT, "functionName"),
      Node.newNode(Token.BLOCK, new int[] {}, new Node[] {})
    });

    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] { 0, 1 }, new Node[] {
        Node.newNode(Token.LITERAL, 1),
        Node.newNode(Token.STATEMENT, new int[] {}, null)
    });

    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.IDENT, "expressionVar");

    Node parentScriptNode = Node.newNode(Token.SCRIPT, new int[] {}, null);

    Node parentBlockNode = Node.newNode(Token.BLOCK, new int[] {}, null);

    Node functionNode = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName"),
        Node.newNode(Token.BLOCK, new int[] {}, null)
    });

    assertFalse(NodeUtil.isStatement(expressionNode));

    assertFalse(NodeUtil.isStatement(parentScriptNode));

    assertFalse(NodeUtil.isStatement(parentBlockNode));

    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName"),
        Node.newNode(Token.BLOCK, new int[] {}, new Node[] {})
    });

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName"),
        Node.newNode(Token.ASSIGN, new int[] { 0 }, new Node[] {
          Node.newNode(Token.IDENT, "functionName"),
          Node.newNode(Token.COLON)
        })
    });

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, null);

    Node functionDeclarationNode1 = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName1"),
        Node.newNode(Token.BLOCK, new int[] {}, null)
    });

    Node functionDeclarationNode2 = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName2"),
        Node.newNode(Token.BLOCK, new int[] {}, null)
    });

    Node functionDeclarationNode3 = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName3"),
        Node.newNode(Token.BLOCK, new int[] {}, null)
    });

    scriptNode.setFirstChild(functionDeclarationNode1);
    scriptNode.getLastChild().setNext(functionDeclarationNode2);
    functionDeclarationNode2.getLastChild().setNext(functionDeclarationNode3);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode2));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode3));

    Node functionNode = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] {
        Node.newNode(Token.IDENT, "functionName"),
        Node.newNode(Token.BLOCK, new int[] {}, null)
    });

    Node functionParentBlockNode = Node.newNode(Token.BLOCK, new int[] {}, null);

    functionParentBlockNode.setFirstChild(functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}