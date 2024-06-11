package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"for"}),
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"in"}),
        Node.newNode(Token.LBRACKET, new int[]{}, null),
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"i"}),
        Node.newNode(Token.RBRACKET, new int[]{}, null),
        Node.newNode(Token.SEMICOLON, new int[]{}, null),
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"foo"}),
        Node.newNode(Token.SEMICOLON, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"for"}),
        Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{
          Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.SEMICOLON, new int[]{}, null),
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"foo"}),
        Node.newNode(Token.SEMICOLON, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
        Node.newNode(Token.COMPARE, new int[]{}, new Node[]{
          Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RETURN, new int[]{}, new Node[]{
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
        Node.newNode(Token.PLUS, new int[]{}, new Node[]{
          Node.newNode(Token.IDENT, new int[]{}, new String[]{"y"}),
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        })
    });

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"myFunction"}),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"myFunction"}),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RETURN, new int[]{}, new Node[]{
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"myFunction"}),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    Node parentNode = Node.newNode(Token.SCRIPT, new int[]{}, null);
    Node grandParentNode = Node.newNode(Token.FUNCTION, new int[]{}, null);
    grandParentNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"myFunction"}),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    parentNode = Node.newNode(Token.SCRIPT, new int[]{}, null);
    parentNode.addChildToEnd(nodeNotHoistedFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test(expected = NullPointerException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"myFunction"}),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNotFunction() {
    Node nodeNotFunction = Node.newNode(Token.IF, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
        Node.newNode(Token.COMPARE, new int[]{}, new Node[]{
          Node.newNode(Token.IDENT, new int[]{}, new String[]{"x"}),
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.LBRACE, new int[]{}, null),
        Node.newNode(Token.RETURN, new int[]{}, new Node[]{
          Node.newNode(Token.NUMBER, new int[]{}, new String[]{"42"})
        }),
        Node.newNode(Token.RBRACE, new int[]{}, null)
    });

    NodeUtil.isHoistedFunctionDeclaration(nodeNotFunction);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }
}