package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, new Node[] {
      Node.newNode(Token.IDENT, "i"),
      Node.newNode(Token.KEYWORD, "in"),
      Node.newNode(Token.IDENT, "array")
    });

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, null, new Node[] {
      Node.newNode(Token.IDENT, "i"),
      Node.newNode(Token.ASSIGN, null, new Node[] {
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.NUMBER, 1)
      }),
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, new Node[] {
      Node.newNode(Token.IDENT, "condition"),
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.NUMBER, 1);

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "name", new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });

    Node parentScript = Node.newNode(Token.SCRIPT, null, new Node[] {});
    Node parentBlock = Node.newNode(Token.BLOCK, null, new Node[] {});

    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, "name", new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });
    Node nodeFunctionInBlock = Node.newNode(Token.FUNCTION, "name", new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionInBlock));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    checkState(parentScript != null);
    checkState(parentBlock != null);

    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, "name", new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {})
    });
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, "outerName", new Node[] {
      Node.newNode(Token.BLOCK, null, new Node[] {
        nodeFunctionDeclarationInScript
      })
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
  }
}