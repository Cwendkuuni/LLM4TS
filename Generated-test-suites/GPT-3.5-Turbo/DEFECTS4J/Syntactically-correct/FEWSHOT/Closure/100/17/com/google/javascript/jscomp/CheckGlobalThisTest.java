package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        n.setType(Token.FUNCTION);
        parent.setType(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testVisit_shouldReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        n.setType(Token.THIS);
        parent.setType(Token.ASSIGN);
        boolean result = checkGlobalThis.visit(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_shouldNotReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        n.setType(Token.THIS);
        parent.setType(Token.GETPROP);
        boolean result = checkGlobalThis.visit(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testAssignLhsChild_shouldReturnNull() {
        Node n = new Node();
        checkGlobalThis.getAssignLhsChild(n);
        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testShouldReportThis_shouldReturnTrue() {
        Node n = new Node();
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_shouldReturnFalse() {
        Node n = new Node();
        Node parent = new Node();
        checkGlobalThis.assignLhsChild = new Node();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnNull() {
        Node n = new Node();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(null, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_shouldReturnNotNull() {
        Node n = new Node();
        n.setJSDocInfo(new JSDocInfo());
        Node parent = new Node();
        parent.setType(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(new JSDocInfo(), result);
    }
}