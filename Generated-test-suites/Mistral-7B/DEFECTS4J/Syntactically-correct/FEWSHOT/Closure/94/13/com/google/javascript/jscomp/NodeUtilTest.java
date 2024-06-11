package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR);
 nodeForIn.setChildCount(3);
 assertTrue(NodeUtil.isForIn(nodeForIn));
 }

 @Test
 public void testIsForInFalse() {
 Node nodeNotForIn = new Node(Token.IF);
 assertTrue(NodeUtil.isForIn(nodeNotForIn) == false);
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.PRINT, new Object[]{"foo"});
 Node parentScript = new Node(Token.SCRIPT);
 parentScript.addChildToStatementList(nodeStatement);
 assertTrue(NodeUtil.isStatement(nodeStatement));
 }

 @Test
 public void testIsStatementFalse() {
 Node nodeNotStatement = new Node(Token.IDENT, "foo");
 assertTrue(NodeUtil.isStatement(nodeNotStatement) == false);
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Object[]{"foo"});
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setProperty(Node.IS_DECLARE, true);
 Node parentBlock = new Node(Token.BLOCK);
 parentBlock.addChildToStatementList(nodeFunctionDeclaration);
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationFalse() {
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new Object[]{"foo"});
 nodeFunctionExpression.setType(Token.FUNCTION);
 Node parentExpression = new Node(Token.ASSIGN, new Object[]{new Node(Token.IDENT, "x"), nodeFunctionExpression});
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionExpression) == false);
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Object[]{"foo"});
 nodeHoistedFunctionDeclaration.setType(Token.FUNCTION);
 nodeHoistedFunctionDeclaration.setProperty(Node.IS_DECLARE, true);
 Node parentScript = new Node(Token.SCRIPT);
 parentScript.addChildToStatementList(nodeHoistedFunctionDeclaration);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationFalse() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Object[]{"foo"});
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setProperty(Node.IS_DECLARE, true);
 Node parentFunction = new Node(Token.FUNCTION, new Object[]{"bar"});
 parentFunction.setProperty(Node.IS_DECLARE, true);
 parentFunction.addChildToStatementList(nodeFunctionDeclaration);
 Node parentScript = new Node(Token.SCRIPT);
 parentScript.addChildToStatementList(parentFunction);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration) == false);
 }
}