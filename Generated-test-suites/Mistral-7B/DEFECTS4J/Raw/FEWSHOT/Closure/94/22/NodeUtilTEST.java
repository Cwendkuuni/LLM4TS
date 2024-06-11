package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node();
 nodeForIn.setType(Token.FOR);
 nodeForIn.setChildCount(3);

 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node();
 nodeNotForIn.setType(Token.IF);
 nodeNotForIn.setChildCount(1);

 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeScript = new Node();
 nodeScript.setType(Token.SCRIPT);
 nodeScript.setChildCount(1);

 Node nodeStatement = new Node();
 nodeStatement.setType(Token.PRINT);
 nodeStatement.setParent(nodeScript);

 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeExpression = new Node();
 nodeExpression.setType(Token.PRINT);

 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.addChild(nodeFunctionDeclaration);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionExpression() {
 Node nodeFunctionExpression = new Node();
 nodeFunctionExpression.setType(Token.FUNCTION);
 nodeFunctionExpression.setChildCount(1); // function body
 Node parentExpression = new Node();
 parentExpression.setType(Token.ASSIGN); // a = ...
 parentExpression.addChild(nodeFunctionExpression);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);
 parentScript.addChild(nodeFunctionDeclaration);

 Node parentFunction = new Node();
 parentFunction.setType(Token.FUNCTION);
 parentFunction.setName("bar");
 parentFunction.addChild(parentScript);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_withEmptyParent() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parentScript = new Node();
 parentScript.setType(Token.SCRIPT);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_withParentFunction() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parentFunction = new Node();
 parentFunction.setType(Token.FUNCTION);
 parentFunction.setName("bar");
 parentFunction.addChild(nodeFunctionDeclaration);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_withParentBlock() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parentBlock = new Node();
 parentBlock.setType(Token.BLOCK);
 parentBlock.addChild(nodeFunctionDeclaration);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test(expected = AssertionError.class)
 public void testIsHoistedFunctionDeclaration_withNullParent() {
 Node nodeFunctionDeclaration = new Node();
 nodeFunctionDeclaration.setType(Token.FUNCTION);
 nodeFunctionDeclaration.setName("foo");
 nodeFunctionDeclaration.setChildCount(1); // function body
 Node parent = null; // should throw an assertion error

 // Preconditions check should throw an error if parent is null
 checkState(parent != null);
 }
}