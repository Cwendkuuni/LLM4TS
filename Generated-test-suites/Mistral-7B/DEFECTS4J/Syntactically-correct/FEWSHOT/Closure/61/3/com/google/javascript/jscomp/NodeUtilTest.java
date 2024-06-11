package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node node = new Node(Token.FOR, new int[] { 1, 2, 3 }, null);
 assertTrue(NodeUtil.isForIn(node));
 }

 @Test
 public void testIsForInFalse() {
 Node node = new Node(Token.IF, new int[] { 1, 2 }, null);
 assertFalse(NodeUtil.isForIn(node));
 }

 @Test
 public void testIsStatement() {
 Node node = new Node(Token.FUNCTION, new int[] { 1 }, new int[] { 2, 3 });
 checkState(NodeUtil.isStatement(node), "Expected statement node, found function node");
 }

 @Test
 public void testIsStatementFalse() {
 Node node = new Node(Token.IDENT, new int[] { 1 }, null);
 assertFalse(NodeUtil.isStatement(node));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node node = new Node(Token.FUNCTION, new int[] { 1, 2 }, new int[] { 3, 4 });
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationFalse() {
 Node node = new Node(Token.IDENT, new int[] { 1 }, null);
 assertFalse(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node functionNode = new Node(Token.FUNCTION, new int[] { 1, 2 }, new int[] { 3, 4 });
 Node scriptNode = new Node(Token.SCRIPT, new int[] { 1 }, null);
 scriptNode.addChild(functionNode);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclarationFalse() {
 Node functionNode = new Node(Token.FUNCTION, new int[] { 1, 2 }, new int[] { 3, 4 });
 Node blockNode = new Node(Token.BLOCK, new int[] { 1 }, null);
 blockNode.addChild(functionNode);
 Node parentFunctionNode = new Node(Token.FUNCTION, new int[] { 1 }, new int[] { 2, 3 });
 blockNode.setParent(parentFunctionNode);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
 }
}