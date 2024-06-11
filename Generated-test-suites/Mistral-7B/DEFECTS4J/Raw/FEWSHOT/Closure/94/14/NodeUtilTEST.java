package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 3, 3}, new int[] {1, 1, 1}, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[3]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[3]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {});
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeNotStatement = new Node(Token.IDENT, new int[] {1}, new int[] {1}, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
        new Node(Token.IDENT, new int[] {1}, new int[] {1}, null),
        new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.LCURLY, new int[] {1}, new int[] {1}, null),
        new Node(Token.RCURLY, new int[] {1}, new int[] {1}, null),
        new Node(Token.SEMICOLON, new int[] {1}, new int[] {1}, null)
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
        new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.LCURLY, new int[] {1}, new int[] {1}, null),
        new Node(Token.RCURLY, new int[] {1}, new int[] {1}, null),
        new Node(Token.SEMICOLON, new int[] {1}, new int[] {1}, null)
    });
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunctionDeclaration = new Node(Token.SCRIPT, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
        new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
            new Node(Token.IDENT, new int[] {1}, new int[] {1}, null),
            new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
            new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
            new Node(Token.LCURLY, new int[] {1}, new int[] {1}, null),
            new Node(Token.RCURLY, new int[] {1}, new int[] {1}, null),
            new Node(Token.SEMICOLON, new int[] {1}, new int[] {1}, null)
        })
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunctionDeclaration.getChildren()[0]));

    Node nodeNestedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
        new Node(Token.IDENT, new int[] {1}, new int[] {1}, null),
        new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.BLOCK, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[0])
    });

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node nodeFunctionWithParentFunction = new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
        new Node(Token.IDENT, new int[] {1}, new int[] {1}, null),
        new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
        new Node(Token.BLOCK, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
            new Node(Token.FUNCTION, new int[] {1, 1, 1}, new int[] {1, 1, 1}, new Node[] {
                new Node(Token.IDENT, new int[] {1}, new int[] {1}, null),
                new Node(Token.LPAREN, new int[] {1}, new int[] {1}, null),
                new Node(Token.RPAREN, new int[] {1}, new int[] {1}, null),
                new Node(Token.LCURLY, new int[] {1}, new int[] {1}, null),
                new Node(Token.RCURLY, new int[] {1}, new int[] {1}, null),
                new Node(Token.SEMICOLON, new int[] {1}, new int[] {1}, null)
            })
        })
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionWithParentFunction.getChildren()[0].getChildren()[0]));
  }
}