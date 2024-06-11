package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
 Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2, 3});

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3});
 Node nodeExpression = new Node(Token.ADD, new int[] {1, 2, 3});
 Node nodeScript = new Node(Token.SCRIPT, new int[] {1, 2, 3});
 Node nodeBlock = new Node(Token.BLOCK, new int[] {1, 2, 3});
 Node nodeLabel = new Node(Token.LABEL, new int[] {1, 2, 3});
 Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3});
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);

 checkState(nodeScript != null);
 checkState(nodeBlock != null);
 checkState(nodeLabel != null);
 checkState(nodeFunction != null);
 checkState(nodeFunctionExpression != null);

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertTrue(NodeUtil.isStatement(nodeScript));
 assertTrue(NodeUtil.isStatement(nodeBlock));
 assertTrue(NodeUtil.isStatement(nodeLabel));
 assertFalse(NodeUtil.isStatement(nodeExpression));
 assertFalse(NodeUtil.isStatement(nodeFunction));
 assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
 Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[0]);
 Node nodeFunctionExpressionInBlock = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[0]);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationInBlock));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpressionInBlock));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
 Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[0]);
 Node nodeFunctionDeclarationInBlockInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {nodeFunctionDeclarationInBlock});

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlockInFunction));
 }
}