package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // Test for shouldTraverse() method
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit() {
        // Test for visit() method
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Test for getFunctionJsDocInfo() method
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        Node n = new Node();
        JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(jsDoc);
    }

}