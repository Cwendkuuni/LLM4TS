package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = Node.newForIn();
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeIf = Node.newIf();
 assertFalse(NodeUtil.isForIn(nodeIf));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = Node.newExpressionStatement(Node.newIdent("x"));
 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeExpression = Node.newExpression(Node.newIntLiteral(1));
 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = Node.newFunction(
 Node.newIdentifier("foo"), Node.newArray(), Node.newBlock(null));
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = Node.newExpressionStatement(
 Node.newFunctionExpression(Node.newIdentifier("bar"), Node.newArray(), Node.newBlock(null)));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeScript = Node.newScript();
 Node nodeFunctionDeclarationInScript = Node.newFunction(
 Node.newIdentifier("foo"), Node.newArray(), Node.newBlock(null));
 nodeFunctionDeclarationInScript.setLine(1);
 nodeScript.addChildToEnd(nodeFunctionDeclarationInScript);

 Node nodeFunctionDeclarationInFunction = Node.newFunction(
 Node.newIdentifier("bar"), Node.newArray(), Node.newBlock(null));
 nodeFunctionDeclarationInFunction.setLine(5);
 Node nodeFunction = Node.newFunction(Node.newIdentifier("baz"), Node.newArray(), nodeFunctionDeclarationInFunction);
 nodeScript.addChildToEnd(nodeFunction);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }
}