package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[]{});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{});
    Node nodeIf = Node.newNode(Token.IF, new int[]{}, new Node[]{});
    Node nodeLabel = Node.newNode(Token.LABEL, new int[]{}, new Node[]{});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeIf));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeExpression = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{Node.newNode(Token.NUMBER, new int[]{}, new int[]{0})});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.LEFT_PAREN, new int[]{}, new Node[]{})
    });

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.LEFT_PAREN, new int[]{}, new Node[]{Node.newNode(Token.RIGHT_PAREN, new int[]{}, new Node[]{})})
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.LEFT_PAREN, new int[]{}, new Node[]{})
    });
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.LEFT_PAREN, new int[]{}, new Node[]{Node.newNode(Token.RIGHT_PAREN, new int[]{}, new Node[]{})}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{})
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationNotInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.LEFT_PAREN, new int[]{}, new Node[]{})
    });
    Node parentOfFunctionDeclarationNotInFunction = Node.newNode(Token.VAR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new int[]{}, new int[]{"name"}),
      Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{nodeFunctionDeclarationNotInFunction})
    });

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentOfFunctionDeclarationNotInFunction));
  }

  @Test(expected = NullPointerException.class)
  public void testIsFunctionDeclarationNullNode() {
    checkNotNull(NodeUtil.isFunctionDeclaration(null));
  }

  @Test(expected = NullPointerException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    checkNotNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }
}