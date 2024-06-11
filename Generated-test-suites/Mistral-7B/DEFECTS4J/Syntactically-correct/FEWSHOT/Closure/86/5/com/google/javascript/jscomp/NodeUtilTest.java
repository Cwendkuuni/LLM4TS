package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"i"}),
      Node.newNode(Token.KEYWORD, new String[]{"in"}),
      Node.newNode(Token.IDENT, new String[]{"arr"})
    });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, new int[]{1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"i"}),
      Node.newNode(Token.ASSIGN, new String[]{"="}),
      Node.newNode(Token.NUMBER, new String[]{"1"}),
      Node.newNode(Token.KEYWORD, new String[]{";"}),
      Node.newNode(Token.STATEMENT, new String[]{}, Node.newNode(Token.PRINT, new String[]{"i"})),
      Node.newNode(Token.KEYWORD, new String[]{";"})
    });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{1, 2}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"x"}),
      Node.newNode(Token.COMPARE, new String[]{"<"}, Node.newNode(Token.NUMBER, new String[]{"1"}), Node.newNode(Token.IDENT, new String[]{"x"})),
      Node.newNode(Token.STATEMENT, new String[]{}, Node.newNode(Token.PRINT, new String[]{"Hello World"})),
      Node.newNode(Token.KEYWORD, new String[]{";"})
    });
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IDENT, new String[]{"x"});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"myFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{0}, new Node[0])
    });
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.IDENT, new String[]{"myFunctionName"});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"myFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{0}, new Node[0])
    });
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{0}, new Node[]{nodeFunctionDeclarationInScript});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"outerFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{0}, new Node[]{
        Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
          Node.newNode(Token.IDENT, new String[]{"myFunctionName"}),
          Node.newNode(Token.BLOCK, new int[]{0}, new Node[0])
        })
      })
    });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationNotInScriptOrFunction = Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"myFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{0}, new Node[0])
    });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInScriptOrFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 1}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"myFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{0}, new Node[0])
    });
    Node nullParent = null;
    checkArgument(nullParent != null, "NodeUtilTest: Parent node cannot be null.");
    checkState(nodeFunctionDeclaration.getParent() != null, "NodeUtilTest: Node must have a parent.");
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}