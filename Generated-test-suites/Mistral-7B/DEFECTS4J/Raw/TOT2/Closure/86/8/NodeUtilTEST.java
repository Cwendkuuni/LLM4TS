package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = createNodeForForIn();
 assertTrue(NodeUtil.isForIn(nodeForIn));
 }

 @Test
 public void testIsForInNull() {
 assertFalse(NodeUtil.isForIn(null));
 }

 @Test
 public void testIsForInvalidType() {
 Node nodeInvalidType = createNodeInvalidType();
 assertFalse(NodeUtil.isForIn(nodeInvalidType));
 }

 private Node createNodeForForIn() {
 // Create a FOR-IN loop node here.
 }

 private Node createNodeInvalidType() {
 // Create a Node with an unexpected or invalid type here.
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = createNodeStatement();
 assertTrue(NodeUtil.isStatement(nodeStatement));
 }

 @Test
 public void testIsStatementNull() {
 assertFalse(NodeUtil.isStatement(null));
 }

 @Test
 public void testIsStatementInvalidType() {
 Node nodeInvalidType = createNodeInvalidType();
 assertFalse(NodeUtil.isStatement(nodeInvalidType));
 }

 private Node createNodeStatement() {
 // Create a statement node here.
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = createNodeFunctionDeclaration();
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 }

 @Test
 public void testIsFunctionDeclarationNull() {
 assertFalse(NodeUtil.isFunctionDeclaration(null));
 }

 @Test
 public void testIsFunctionDeclarationInvalidType() {
 Node nodeInvalidType = createNodeInvalidType();
 assertFalse(NodeUtil.isFunctionDeclaration(nodeInvalidType));
 }

 private Node createNodeFunctionDeclaration() {
 // Create a function declaration node here.
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = createNodeHoistedFunctionDeclaration();
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclarationNull() {
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
 }

 @Test
 public void testIsHoistedFunctionDeclarationInvalidType() {
 Node nodeInvalidType = createNodeInvalidType();
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInvalidType));
 }

 private Node createNodeHoistedFunctionDeclaration() {
 // Create a hoisted function declaration node here.
 }
}