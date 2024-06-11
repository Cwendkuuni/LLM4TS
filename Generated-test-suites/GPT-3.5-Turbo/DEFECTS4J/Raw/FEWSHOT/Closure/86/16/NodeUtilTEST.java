package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
   @Test
   public void testIsForIn() {
      //Test for FOR-IN loop
      assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      
      //Test for other types of nodes
      assertFalse(NodeUtil.isForIn(new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertFalse(NodeUtil.isForIn(new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
   }
   
   @Test
   public void testIsStatement() {
      //Test for statement nodes
      assertTrue(NodeUtil.isStatement(new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertTrue(NodeUtil.isStatement(new Node(Token.BLOCK, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertTrue(NodeUtil.isStatement(new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      
      //Test for non-statement nodes
      assertFalse(NodeUtil.isStatement(new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertFalse(NodeUtil.isStatement(new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
   }
   
   @Test
   public void testIsFunctionDeclaration() {
      //Test for function declaration nodes
      assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      
      //Test for non-function declaration nodes
      assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
   }
   
   @Test
   public void testIsHoistedFunctionDeclaration() {
      //Test for hoisted function declaration nodes
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY)), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      
      //Test for non-hoisted function declaration nodes
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY)), new Node(Token.EMPTY), new Node(Token.EMPTY))));
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY))));
   }
}