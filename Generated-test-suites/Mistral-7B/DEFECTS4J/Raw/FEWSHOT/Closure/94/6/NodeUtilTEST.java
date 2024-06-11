package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[] { 1, 2, 3 });
 Node nodeNotForIn = new Node(Token.IF, new int[] { 1, 2 });

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[] { 1, 2 });
 Node nodeNotStatement = new Node(Token.ADD, new int[] { 1, 2, 3 });
 Node nodeFunction = new Node(Token.FUNCTION, new int[] { 1, 2 });
 Node nodeFunctionInExpression = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) });

 Node parentScript = new Node(Token.SCRIPT, new int[] {});
 Node parentBlock = new Node(Token.BLOCK, new int[] {});
 Node parentLabel = new Node(Token.LABEL, new int[] { 1 });

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertTrue(NodeUtil.isStatement(parentScript.getFirstChild()));
 assertTrue(NodeUtil.isStatement(parentBlock.getFirstChild()));
 assertTrue(NodeUtil.isStatement(parentLabel.getFirstChild()));

 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 assertFalse(NodeUtil.isStatement(nodeFunction));
 assertFalse(NodeUtil.isStatement(nodeFunctionInExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) });
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) }, new Node[] { new Node(Token.ASSIGN, new int[] { 1, 2, 3 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }), new Node(Token.IDENT, new int[] { 1, 3, 5 }) }) });

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) }, new Node[] {});
 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) }, new Node[] { new Node(Token.RETURN, new int[] { 1 }) });
 Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { new Node(Token.IDENT, new int[] { 1, 3, 4 }) }, new Node[] {});

 Node parentFunction = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] {});

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

 Node parentFunctionWithFunction = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node[] { nodeFunctionDeclarationInFunction });

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentFunctionWithFunction.getFirstChild()));
 }

 @Test
 public void testCheckStateWithNullParent() {
 checkState(NodeUtil.isStatement(null), "Parent node cannot be null.");
 }
}