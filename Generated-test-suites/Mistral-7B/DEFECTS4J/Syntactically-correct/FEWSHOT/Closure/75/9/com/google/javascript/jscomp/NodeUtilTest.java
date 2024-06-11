package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeBlock = Node.newBlock("{}");
    Node nodeLabel = Node.newNode(Token.IDENT, new String[] {"label"});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeStatementFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {
      Node.newStatement(Token.RETURN, new int[] {}, new Node[] {Node.newNode(Token.NUMBER, new int[] {1})})
    });

    Node nodeExpressionFunction = Node.newNode(Token.FUNCTION, new int[] {1}, new String[] {}, new Node[] {
      Node.newExpression(Node.newName("myFunction"), new Node[] {Node.newName("arg")})
    });

    Node nodeIf = Node.newNode(Token.IF, new int[] {1, 2});

    // It is a statement if it is part of a script, a block or a label
    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    // A function declaration is a statement if it is part of a script or a block
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeStatementFunction));
    // An function expression is not a statement
    assertFalse(NodeUtil.isStatement(nodeExpressionFunction));
    // An if statement is a statement
    assertTrue(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1}, new String[] {}, new Node[] {
      Node.newExpression(Node.newName("myFunction"), new Node[] {Node.newName("arg")})
    });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeRootFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeRootFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeNestedFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeNestedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeNestedBlock = Node.newBlock("{function innerFunction() {}}");
    Node nodeNestedBlockFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});
    Node nodeNestedBlockFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new String[] {"arg"}, new Node[] {});

    // A hoisted function declaration is a function declaration that is part of the scope root
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeRootFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    // A function declaration inside a block is not hoisted
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunctionDeclaration));
    // A function expression inside a block is not hoisted
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunction));
    // A function declaration inside a nested block is not hoisted
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedBlockFunctionDeclaration));
    // A function expression inside a nested block is not hoisted
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedBlockFunction));
  }

  private void assertFalse(boolean condition) {
    checkState(condition == false);
  }
}