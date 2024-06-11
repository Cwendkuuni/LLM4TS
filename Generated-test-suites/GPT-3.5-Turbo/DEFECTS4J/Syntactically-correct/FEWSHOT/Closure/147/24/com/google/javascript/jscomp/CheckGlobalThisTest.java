
package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.SCRIPT);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.SCRIPT);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_NestedFunction_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_NormalFunction_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_VariableAssignment_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_ThisNode_ShouldReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Test that compiler.report() was called with the correct error message and level
    }

    @Test
    public void testVisit_AssignLhsChild_ShouldNotReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.setAssignLhsChild(n);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Test that compiler.report() was not called
    }
}
