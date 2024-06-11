package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckGlobalThis check;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_ValidFunction() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertTrue(check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_ConstructorFunction() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setJSDocInfo(new JSDocInfoBuilder().recordConstructor().build());
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setJSDocInfo(new JSDocInfoBuilder().recordThisType().build());
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setJSDocInfo(new JSDocInfoBuilder().recordOverride().build());
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideBlock() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        Node grandParent = new Node(Token.BLOCK);
        parent.addChildToFront(grandParent);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideScript() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideName() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideAssign() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideObjectLiteralKeyString() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInsideObjectLiteralKeyNumber() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_PrototypeAssignment() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        n.addChildToFront(new Node(Token.NAME, "prototype"));
        assertTrue(!check.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_ThisWithParentGet() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        t.visit(t, n, parent);
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisit_ThisWithParentFunction() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        t.visit(t, n, parent);
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisit_ThisWithParentAssign() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        t.visit(t, n, parent);
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisit_ThisWithParentOther() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.DELPROP);
        t.visit(t, n, parent);
        assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void testVisit_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        check.visit(t, n, parent);
        assertTrue(check.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_AssignLhsChildNull() {
        NodeTraversal t = new NodeTraversal(compiler, check);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        check.visit(t, n, parent);
        assertTrue(!check.shouldReportThis(n, parent));
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJSDocInfoOnNode() {
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().build());
        JSDocInfo expected = n.getJSDocInfo();
        assertEquals(expected, check.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJSDocInfoOnParentAssign() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(new JSDocInfoBuilder().build());
        parent.addChildToFront(n);
        JSDocInfo expected = parent.getJSDocInfo();
        assertEquals(expected, check.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJSDocInfoOnParentName() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfoBuilder().build());
        parent.addChildToFront(n);
        JSDocInfo expected = parent.getJSDocInfo();
        assertEquals(expected, check.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJSDocInfoOnParentVar() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandParent = new Node(Token.VAR);
        grandParent.setJSDocInfo(new JSDocInfoBuilder().build());
        grandParent.addChildToFront(parent);
        parent.addChildToFront(n);
        JSDocInfo expected = grandParent.getJSDocInfo();
        assertEquals(expected, check.getFunctionJsDocInfo(n));
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        assertNull(check.getFunctionJsDocInfo(n));
    }

}