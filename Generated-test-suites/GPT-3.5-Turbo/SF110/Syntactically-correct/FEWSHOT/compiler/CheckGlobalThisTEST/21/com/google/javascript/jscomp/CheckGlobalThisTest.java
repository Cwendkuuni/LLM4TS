package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node node1 = new Node(Token.THIS);
        Node node2 = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node2);
        
        assertEquals(true, checkGlobalThis.shouldReportThis(node1));
        assertEquals(true, checkGlobalThis.shouldReportThis(node2));
        assertEquals(false, checkGlobalThis.shouldReportThis(parent));
    }
}