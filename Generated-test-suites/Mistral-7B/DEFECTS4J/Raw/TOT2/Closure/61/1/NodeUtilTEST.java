package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFunction;

public class NodeUtilTest {

 static boolean isForIn(Node n) {
 return n.getType() == Token.FOR && n.getChildCount() == 3;
 }

 static boolean isStatement(Node n) {
 Node parent = n.getParent();
 Preconditions.checkState(parent != null);
 switch (parent.getType()) {
 case Token.SCRIPT:
 case Token.BLOCK:
 case Token.LABEL:
 return true;
 default:
 return false;
 }
 }

 static boolean isFunctionDeclaration(Node n) {
 return n.getType() == Token.FUNCTION && isStatement(n);
 }

 static boolean isHoistedFunctionDeclaration(Node n) {
 return isFunctionDeclaration(n) && (n.getParent().getType() == Token.SCRIPT || n.getParent().getParent().getType() == Token.FUNCTION);
 }

 @Test
 public void testIsForIn() {
 Node forNode = new Node(Token.FOR);
 forNode.setChildCount(3);
 assertTrue(NodeUtil.isForIn(forNode));
 }

 @Test
 public void testIsForIn_InvalidNode() {
 Node invalidNode = new Node(Token.NULL);
 assertFalse(NodeUtil.isForIn(invalidNode));
 }

 @Test
 public void testIsStatement() {
 Node statementNode = new Node(Token.ASSIGN);
 NodeFunction parentBlock = new NodeFunction();
 parentBlock.setChild(0, statementNode);
 assertTrue(NodeUtil.isStatement(statementNode));
 }

 @Test
 public void testIsStatement_NonStatementNode() {
 Node nonStatementNode = new Node(Token.STRING_LITERAL);
 assertFalse(NodeUtil.isStatement(nonStatementNode));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node functionDeclaration = new NodeFunction();
 functionDeclaration.setName("myFunction");
 NodeFunction parentBlock = new NodeFunction();
 parentBlock.setChild(0, functionDeclaration);
 assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
 }

 @Test
 public void testIsFunctionDeclaration_NonFunctionNode() {
 Node nonFunctionNode = new Node(Token.IF);
 assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node hoistedFunctionDeclaration = new NodeFunction();
 hoistedFunctionDeclaration.setName("myFunction");
 NodeFunction functionDeclarationParent = new NodeFunction();
 Node scriptParent = new NodeFunction();
 scriptParent.setChild(0, functionDeclarationParent);
 functionDeclarationParent.setChild(0, hoistedFunctionDeclaration);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
 }

 // Test other cases for isForIn, isStatement, and isFunctionDeclaration here
}