package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        // Test for FOR-IN loop
        assertEquals(true, NodeUtil.isForIn(new Node(Token.FOR, new Node(Token.VAR), new Node(Token.FALSE))));
        assertEquals(false, NodeUtil.isForIn(new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), new Node(Token.EMPTY))));
        assertEquals(false, NodeUtil.isForIn(new Node(Token.WHILE, new Node(Token.TRUE), new Node(Token.NUMBER, "5"))));
    }
    
    @Test
    public void testIsStatement() {
        // Test for nodes used as statements
        assertEquals(true, NodeUtil.isStatement(new Node(Token.SCRIPT)));
        assertEquals(true, NodeUtil.isStatement(new Node(Token.BLOCK)));
        assertEquals(true, NodeUtil.isStatement(new Node(Token.LABEL)));
        assertEquals(false, NodeUtil.isStatement(new Node(Token.NAME, "foo")));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        // Test for function declarations
        assertEquals(true, NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), new Node(Token.EMPTY))));
        assertEquals(false, NodeUtil.isFunctionDeclaration(new Node(Token.NAME, "foo")));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for hoisted function declarations
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), new Node(Token.EMPTY))));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), new Node(Token.EMPTY)).getParent()));
    }
}