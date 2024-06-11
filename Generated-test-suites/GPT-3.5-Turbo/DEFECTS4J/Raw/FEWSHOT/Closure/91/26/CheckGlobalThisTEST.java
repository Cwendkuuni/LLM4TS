package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        
        boolean result1 = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result1);
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        Node n = null;
        Node parent = null;
        
        boolean result1 = checkGlobalThis.shouldReportThis(n, parent);
        
        assertEquals(false, result1);
    }
}