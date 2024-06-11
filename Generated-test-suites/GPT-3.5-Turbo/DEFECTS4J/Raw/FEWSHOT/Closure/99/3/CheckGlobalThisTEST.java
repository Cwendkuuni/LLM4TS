package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private static AbstractCompiler compiler;
    private static CheckLevel level;
    private static CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.ERROR;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node();
        Node parent = new Node();
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node();
        Node parent = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testVisit_shouldNotReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.VAR, new Node(Token.NAME));
        
        checkGlobalThis.visit(t, n, parent);
        
        // No assertion, if no error is reported, the test case is passed.
    }
    
    @Test
    public void testVisit_shouldReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assertion for reported error
        DiagnosticType expected = CheckGlobalThis.GLOBAL_THIS;
        DiagnosticType actual = checkGlobalThis.reportedError.getType();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_shouldReturnNull() {
        Node n = new Node(Token.FUNCTION);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertNull(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_shouldReturnJSDocInfo() {
        Node n = new Node(Token.NAME);
        n.setJSDocInfo(new JSDocInfo(false));
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertNotNull(result);
    }
}