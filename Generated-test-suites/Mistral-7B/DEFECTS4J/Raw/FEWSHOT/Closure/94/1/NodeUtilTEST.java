package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = Node.newForIn();
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
 assertTrue(NodeUtil.isStatement(nodeNotForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeScript = Node.newScript();
 assertTrue(NodeUtil.isStatement(nodeScript));

 Node nodeFunction = Node.newFunction("myFunc", new Node[0], null);
 assertTrue(NodeUtil.isStatement(nodeFunction));

 Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
 assertTrue(NodeUtil.isStatement(nodeExpression));

 Node nodeNotStatement = null;
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = Node.newFunction("myFunc", new Node[0], null);
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("myFunc", new Node[0], null));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = Node.newScript()
 .addChild(Node.newName("myFunc"))
 .addChild(Node.newFunction("myFunc", new Node[0], null));
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration.getChild(1)));

 Node nodeNotHoistedFunctionDeclaration = Node.newFunction("myFunc", new Node[0], null);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
 }

 @Test(expected = IllegalStateException.class)
 public void testCheckStateThrowsWhenParentIsNull() {
 Node node = Node.newFunction("myFunc", new Node[0], null);
 NodeUtil.isStatement(node);
 }
}