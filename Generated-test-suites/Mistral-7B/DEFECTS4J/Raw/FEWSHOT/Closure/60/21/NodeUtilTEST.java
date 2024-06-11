package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, 1, new Node[3]);
    Node nodeNotFor = new Node(Token.IF, 1, new Node[0]);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeNotFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new Node[0]);
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Node[0]);
    Node nodeExpression = new Node(Token.IF, 1, new Node[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Node[0]);
    Node nodeFunctionWithName = new Node(Token.FUNCTION, 1, new Node[] {
      new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
      new Node(Token.LEFT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.RIGHT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.LEFT_BRACE, 1, new Node[0])
    });
    Node nodeFunctionExpression = new Node(Token.ASSIGN, 1, new Node[] {
      new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
      new Node(Token.FUNCTION, 1, new Node[] {
        new Node(Token.IDENTIFIER, 1, new String[]{"arg"}),
        new Node(Token.LEFT_PARENTHESIS, 1, new Node[0]),
        new Node(Token.RIGHT_PARENTHESIS, 1, new Node[0]),
        new Node(Token.LEFT_BRACE, 1, new Node[0]),
        new Node(Token.RETURN, 1, new Node[0])
      })
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionWithName));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new Node[] {
      new Node(Token.FUNCTION, 1, new Node[] {
        new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
        new Node(Token.LEFT_PARENTHESIS, 1, new Node[0]),
        new Node(Token.RIGHT_PARENTHESIS, 1, new Node[0]),
        new Node(Token.LEFT_BRACE, 1, new Node[0])
      })
    });
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Node[] {
      new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
      new Node(Token.LEFT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.RIGHT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.LEFT_BRACE, 1, new Node[0])
    });
    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, 1, new Node[] {
      new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
      new Node(Token.LEFT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.RIGHT_PARENTHESIS, 1, new Node[0]),
      new Node(Token.LEFT_BRACE, 1, new Node[] {
        new Node(Token.RETURN, 1, new Node[0])
      })
    });

    Node nodeInsideScript = new Node(Token.SCRIPT, 1, new Node[] {
      nodeFunctionInsideFunction
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Node[] {
      new Node(Token.IDENTIFIER, 1, new String[]{"myFunction"}),
      new Node(Token.LEFT_PAREN, 1, new Node[0]),
      new Node(Token.RIGHT_PAREN, 1, new Node[0]),
      new Node(Token.LEFT_BRACE, 1, new Node[0])
    });
    Node nodeParentFunction = new Node(Token.FUNCTION, 1, new Node[0]);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction.getParent()));
  }
}