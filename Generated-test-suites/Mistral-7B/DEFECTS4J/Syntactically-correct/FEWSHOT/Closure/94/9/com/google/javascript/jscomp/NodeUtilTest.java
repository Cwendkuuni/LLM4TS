package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node();
 nodeForIn.setType(Token.FOR);
 nodeForIn.setChildCount(3);

 assertTrue(NodeUtil.isForIn(nodeForIn));
 }

 @Test
 public void testIsForInFalse() {
 Node nodeNotForIn = new Node();
 nodeNotForIn.setType(Token.IF);
 nodeNotForIn.setChildCount(1);

 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node();
 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.setChild(nodeStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));
 }

 @Test
 public void testIsStatementFalse() {
 Node nodeNotStatement = new Node();
 Node parentExpression = new Node();
 parentExpression.setType(Token.CALL);
 parentExpression.setChild(nodeNotStatement);

 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node();
 Node nameNode = new Node();
 nameNode.setType(Token.IDENTIFIER);
 nameNode.setString("myFunction");
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setChild(nameNode);
 Node body = new Node();
 body.setType(Token.BLOCK);
 nodeFunctionDeclaration.setChild(1, body);

 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.setChild(nodeFunctionDeclaration);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationFalse() {
 Node nodeFunctionExpression = new Node();
 Node nameNode = new Node();
 nameNode.setType(Token.IDENTIFIER);
 nameNode.setString("myFunction");
 Node body = new Node();
 body.setType(Token.BLOCK);
 nodeFunctionExpression.setType(Token.FUNCTION);
 nodeFunctionExpression.setChild(0, nameNode);
 nodeFunctionExpression.setChild(1, body);

 Node parentExpression = new Node();
 parentExpression.setType(Token.ASSIGN);
 parentExpression.setChild(0, new Node().setType(Token.IDENTIFIER).setValue("myVariable"));
 parentExpression.setChild(1, nodeFunctionExpression);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node();
 Node nameNode = new Node();
 nameNode.setType(Token.IDENTIFIER);
 nameNode.setString("myFunction");
 Node body = new Node();
 body.setType(Token.BLOCK);
 nodeHoistedFunctionDeclaration.setType(Token.FUNCTION);
 nodeHoistedFunctionDeclaration.setChild(0, nameNode);
 nodeHoistedFunctionDeclaration.setChild(1, body);

 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.setChild(nodeHoistedFunctionDeclaration);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationFalse() {
 Node nodeNotHoistedFunctionDeclaration = new Node();
 Node nameNode = new Node();
 nameNode.setType(Token.IDENTIFIER);
 nameNode.setString("myFunction");
 Node body = new Node();
 body.setType(Token.BLOCK);
 nodeNotHoistedFunctionDeclaration.setType(Token.FUNCTION);
 nodeNotHoistedFunctionDeclaration.setChild(0, nameNode);
 nodeNotHoistedFunctionDeclaration.setChild(1, body);

 Node parentFunction = new Node();
 parentFunction.setType(Token.FUNCTION);
 parentFunction.setChild(0, nameNode);
 parentFunction.setChild(1, body);
 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.setChild(nodeFunctionDeclaration);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
 }
}