package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.*;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node node = new Node("for (var i in array) { }");
 assertTrue(NodeUtil.isForIn(node));
 }

 @Test
 public void testIsForInEmptyForLoop() {
 Node node = new Node("for (; ;) { }");
 assertTrue(NodeUtil.isForIn(node));
 }

 @Test
 public void testIsStatement() {
 Node node = new Node("if (condition) { }");
 Node statementNode = node.getChildren().get(0);
 assertTrue(NodeUtil.isStatement(statementNode));
 }

 @Test
 public void testIsLabelStatement() {
 Node labelNode = new Node("label: { }");
 assertTrue(NodeUtil.isStatement(labelNode));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node node = new Node("function myFunction() { }");
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithArguments() {
 Node node = new Node("function myFunction(arg1, arg2) { }");
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithoutName() {
 Node node = new Node("function() { }");
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithBlockBody() {
 Node node = new Node("function myFunction() { return 1; }");
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithoutBlockBody() {
 Node node = new Node("function myFunction() { }");
 assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithVarKeyword() {
 Node node = new Node("var myFunction = function() { }");
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsStatementNoParentNode() {
 Node node = new Node("{}"); // Anonymous Node with no parent
 NodeUtil.isStatement(node);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsFunctionDeclarationNotFunctionNode() {
 Node node = new Node("{}"); // Anonymous Node that is not a FUNCTION node
 NodeUtil.isFunctionDeclaration(node);
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node scriptNode = new Node("function myFunction() {}");
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
 }

 @Test
 public void testIsNotHoistedFunctionDeclaration() {
 Node functionExpressionNode = new Node("var myFunction = function() { }");
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
 }
}