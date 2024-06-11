package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import static com.google.javascript.rhino.NodeTest.*;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node forNode = newScope().setSource("for (var i in arr) {}", "test.js")
 .getChild(0);
 assertTrue(isForIn(forNode));
 }

 @Test
 public void testIsForIn_InvalidNode() {
 Node invalidNode = newScope().setSource("if (1 > 0)", "test.js")
 .getChild(0);
 assertFalse(isForIn(invalidNode));
 }

 @Test
 public void testIsForIn_InvalidChildCount() {
 Node forNodeWithInvalidChildCount = newScope().setSource("for (var i in arr) {}", "test.js")
 .getChild(0);
 forNodeWithInvalidChildCount.setChildCount(2);
 assertFalse(isForIn(forNodeWithInvalidChildCount));
 }

 @Test
 public void testIsForIn_InvalidChild() {
 Node forNodeWithInvalidChild = newScope().setSource("for (var i in arr) {}", "test.js")
 .getChild(0);
 forNodeWithInvalidChild.setChild(1, newNode(Token.NUMBER, "1"));
 assertFalse(isForIn(forNodeWithInvalidChild));
 }

 @Test
 public void testIsStatement_InvalidNode() {
 Node invalidNode = newNode(Token.NUMBER, "1");
 assertFalse(isStatement(invalidNode));
 }

 @Test
 public void testIsStatement_NotPartOfAST() {
 Node nodeNotPartOfAST = newScope().setSource("for (var i in arr) {}", "test.js")
 .getChild(0).getChild(1);
 assertFalse(isStatement(nodeNotPartOfAST));
 }

 @Test
 public void testIsStatement_NotFunctionDeclaration() {
 Node nodeNotFunctionDeclaration = newScope().setSource("var x = 1; {}", "test.js")
 .getChild(0);
 assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclaration_InvalidNode() {
 Node invalidNode = newNode(Token.NUMBER, "1");
 assertFalse(isFunctionDeclaration(invalidNode));
 }

 @Test
 public void testIsFunctionDeclaration_NoName() {
 Node functionDeclarationWithoutName = newScope().setSource("function() {}", "test.js")
 .getChild(0);
 assertFalse(isFunctionDeclaration(functionDeclarationWithoutName));
 }

 @Test
 public void testIsFunctionDeclaration_IncorrectChildCount() {
 Node functionDeclarationWithIncorrectChildCount = newScope().setSource("function foo(x) {}", "test.js")
 .getChild(0);
 functionDeclarationWithIncorrectChildCount.setChildCount(2);
 assertFalse(isFunctionDeclaration(functionDeclarationWithIncorrectChildCount));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_NonFunctionNode() {
 Node nonFunctionNode = newNode(Token.NUMBER, "1");
 assertFalse(isHoistedFunctionDeclaration(nonFunctionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_NotHoisted() {
 Node functionNode = newScope().setSource("function foo() {}", "test.js")
 .getChild(0);
 functionNode.setParent(newNode(Token.VAR, "foo"));
 assertFalse(isHoistedFunctionDeclaration(functionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration_NotPartOfScriptOrFunction() {
 Node functionNode = newScope().setSource("function foo() {}", "test.js")
 .getChild(0);
 functionNode.setParent(newNode(Token.ASSIGN, ""));
 assertFalse(isHoistedFunctionDeclaration(functionNode));
 }
}