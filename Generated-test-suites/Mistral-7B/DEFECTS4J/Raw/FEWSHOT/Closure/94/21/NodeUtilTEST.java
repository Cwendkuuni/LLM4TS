package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{0});
    Node nodeExpression = new Node(Token.IF, new int[]{}, new int[]{1});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction)); // Function can be statement or expression
    assertFalse(NodeUtil.isStatement(nodeExpression)); // Expression can't be a statement
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{1, 2, 3});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{1, 2, 3});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{1, 2, 3});
    Node nodeScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunctionNode = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNestedFunction() {
    Node nodeScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeOuterFunction = new Node(Token.FUNCTION, new int[]{"outerId"}, new int[]{1, 2, 3});
    Node nodeInnerFunction = new Node(Token.FUNCTION, new int[]{"innerId"}, new int[]{4, 5, 6});

    nodeOuterFunction.setParent(nodeScriptNode);
    nodeInnerFunction.setParent(nodeOuterFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeOuterFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeInnerFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithGrandFatherFunction() {
    Node nodeScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeGrandfatherFunction = new Node(Token.FUNCTION, new int[]{"grandfatherId"}, new int[]{1, 2, 3});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{4, 5, 6});

    nodeGrandfatherFunction.setParent(nodeScriptNode);
    nodeFunction.setParent(nodeGrandfatherFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithCurrentFunction() {
    Node nodeScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{1, 2, 3});

    nodeFunction.setParent(nodeScriptNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}