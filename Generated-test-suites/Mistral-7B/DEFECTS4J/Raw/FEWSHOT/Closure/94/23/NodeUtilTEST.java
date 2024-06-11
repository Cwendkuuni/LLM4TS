package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeChildStatement = Node.newNode(Token.RETURN, new int[] {1});

    checkState(parentScript != null);
    parentScript.appendChild(NodeUtil.isStatement(nodeStatement) ? nodeStatement : Node.newNode(Token.NOP, new int[] {}));
    parentScript.appendChild(nodeChildStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeChildStatement));

    Node nodeNotStatement = Node.newNode(Token.IDENT, new int[] {"x"});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new int[] {3});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new int[] {3, 4});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, new int[] {3}, Node.newNode(Token.IDENT, new int[] {"foo"}, null));
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});

    checkState(nodeScript != null);
    nodeScript.appendChild(NodeUtil.isFunctionDeclaration(nodeHoistedFunctionDeclaration)
        ? nodeHoistedFunctionDeclaration
        : Node.newNode(Token.NOP, new int[] {}));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationOutsideScript = Node.newNode(
        Token.FUNCTION, new int[] {1, 2}, new int[] {3}, Node.newNode(Token.IDENT, new int[] {"foo"}, null));
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1}, new int[] {3});
    Node nodeParentFunction = Node.newNode(Token.FUNCTION, new int[] {0}, new int[] {});

    checkState(nodeFunction != null);
    checkState(nodeParentFunction != null);
    nodeFunction.appendChild(nodeFunctionDeclarationOutsideScript);
    nodeParentFunction.appendChild(nodeFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideScript));
  }
}