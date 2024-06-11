package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, 0, null);
 nodeForIn.setChildCount(3);
 Node init = new Node(Token.VAR, 0, null);
 Node condition = new Node(Token.EQUALS, 0, null);
 Node increment = new Node(Token.ASSIGN, 0, null);
 nodeForIn.setChild(0, init);
 nodeForIn.setChild(1, condition);
 nodeForIn.setChild(2, increment);

 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, 0, null);
 Node conditionNotForIn = new Node(Token.EQUALS, 0, null);
 Node trueBranch = new Node(Token.PRINT, 0, null);
 nodeNotForIn.setChild(0, conditionNotForIn);
 nodeNotForIn.setChild(1, trueBranch);

 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.PRINT, 0, null);
 Node parentScript = new Node(Token.SCRIPT, 0, null);
 parentScript.setChild(0, nodeStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.ADD, 0, null);
 parentScript.setChild(1, nodeNotStatement);

 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 Node parentScript = new Node(Token.SCRIPT, 0, null);
 parentScript.setChild(0, nodeFunctionDeclaration);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = new Node(Token.FUNCTION, 0, null);
 Node bodyFunctionExpression = new Node(Token.RETURN, 0, null);
 nodeFunctionExpression.setChild(0, bodyFunctionExpression);
 Node parentExpression = new Node(Token.ASSIGN, 0, null);
 parentExpression.setChild(0, nodeFunctionExpression);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeHoistedFunctionDeclaration.setChild(0, body);
 Node parentScript = new Node(Token.SCRIPT, 0, null);
 parentScript.setChild(0, nodeHoistedFunctionDeclaration);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 body.setChild(0, new Node(Token.RETURN, 0, null));
 nodeFunctionDeclaration.setChild(0, body);
 Node parentFunction = new Node(Token.FUNCTION, 0, null);
 parentFunction.setChild(0, nodeFunctionDeclaration);
 parentScript.setChild(0, parentFunction);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithEmptyParentFunction() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeHoistedFunctionDeclaration.setChild(0, body);
 Node parentFunction = new Node(Token.FUNCTION, 0, null);
 parentFunction.setChild(0, nodeHoistedFunctionDeclaration);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithEmptyParentScript() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 Node parentScript = new Node(Token.SCRIPT, 0, null);
 parentScript.setChild(0, nodeFunctionDeclaration);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithNoBody() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithEmptyBody() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 body.setChildCount(0);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithNoName() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, null);
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithParentScriptAsFunction() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 Node parentFunction = new Node(Token.FUNCTION, 0, null);
 parentFunction.setChild(0, nodeFunctionDeclaration);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithParentBlock() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 Node parentBlock = new Node(Token.BLOCK, 0, null);
 parentBlock.setChild(0, nodeFunctionDeclaration);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationWithParentLabel() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, "myFunction");
 Node body = new Node(Token.BLOCK, 0, null);
 nodeFunctionDeclaration.setChild(0, body);
 Node parentLabel = new Node(Token.LABEL, 0, "label");
 parentLabel.setChild(0, nodeFunctionDeclaration);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }
}