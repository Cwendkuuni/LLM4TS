package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = Node.newNode(Token.FOR, 0, new Node[] { null, null, null });
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = Node.newNode(Token.IF, 0, new Node[] { Node.newNode(Token.IDENT, 0, new String[] { "if" }) });
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeScript = Node.newNode(Token.SCRIPT, 0, new Node[] {});
 assertTrue(NodeUtil.isStatement(nodeScript));

 Node nodeExpression = Node.newNode(Token.IDENT, 0, new String[] { "expression" });
 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, 0, new String[] { "functionName" }, new Node[] { Node.newNode(Token.BLOCK, 0, new Node[] {}) });
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = Node.newNode(Token.IDENT, 0, new String[] { "functionName" });
 nodeFunctionExpression.setParent(Node.newNode(Token.ASSIGN, 0, new Node[] { Node.newNode(Token.IDENT, 0, new String[] { "variableName" }), nodeFunctionExpression }));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeFunctionDeclarationInRoot = Node.newNode(Token.FUNCTION, 0, new String[] { "functionName" }, new Node[] { Node.newNode(Token.BLOCK, 0, new Node[] {}) });
 Node scriptNode = Node.newNode(Token.SCRIPT, 0, new Node[] { nodeFunctionDeclarationInRoot });
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));

 Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, 0, new String[] { "outerFunctionName" }, new Node[] {
 Node.newNode(Token.VAR, 0, new Node[] { Node.newNode(Token.IDENT, 0, new String[] { "innerFunctionName" }) }, new Node[] { Node.newNode(Token.FUNCTION, 0, new String[] { "innerFunctionName" }, new Node[] { Node.newNode(Token.BLOCK, 0, new Node[] { nodeFunctionDeclarationInRoot }) }) })
 });
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithEmptyParent() {
 Node nodeFunctionDeclarationInRoot = Node.newNode(Token.FUNCTION, 0, new String[] { "functionName" }, new Node[] { Node.newNode(Token.BLOCK, 0, new Node[] {}) });
 Node scriptNode = Node.newNode(Token.SCRIPT, 0, new Node[] {});
 scriptNode.setParent(null); // Empty parent
 nodeFunctionDeclarationInRoot.setParent(scriptNode);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithVoidParent() {
 Node nodeFunctionDeclarationInRoot = Node.newNode(Token.FUNCTION, 0, new String[] { "functionName" }, new Node[] { Node.newNode(Token.BLOCK, 0, new Node[] {}) });
 Node voidParentNode = Node.newNode(Token.VOID, 0, new Node[] {});
 nodeFunctionDeclarationInRoot.setParent(voidParentNode);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
 }
}