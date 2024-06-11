package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[] { 1, 3 });
 Node nodeNotForIn = new Node(Token.IF, new int[] { 1 });

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[] { 1 });
 Node nodeExpression = new Node(Token.FUNCTION, new int[] { 1 });
 Node nodeScript = new Node(Token.SCRIPT, new int[] {});

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertTrue(NodeUtil.isStatement(nodeScript));
 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 });
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] { 1, 2, 3 });

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node(Token.ID, new int[] { 1, 2 }));
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node(Token.ID, new int[] { 1, 2 }));
 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node(Token.FUNCTION, new int[] { 1 }, new Node(Token.ID, new int[] { 1, 2 })));

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }

 @Test
 public void testIsHoistedFunctionDeclarationNullParent() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] { 1, 2 }, new Node(Token.ID, new int[] { 1, 2 }));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationNull() {
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
 }

 @Test
 public void testIsFunctionDeclarationNull() {
 assertFalse(NodeUtil.isFunctionDeclaration(null));
 }

 @Test
 public void testIsStatementNull() {
 Node nodeNull = new Node(Token.NULL, new int[] {});

 assertFalse(NodeUtil.isStatement(nodeNull));
 }

 @Test
 public void testIsForInNull() {
 Node nodeNull = new Node(Token.NULL, new int[] {});

 assertFalse(NodeUtil.isForIn(nodeNull));
 }
}