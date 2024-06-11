package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_noGlobalThisEncountered_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_constructorFunction_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EQUALS);

        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithThisAnnotation_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EQUALS);

        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionInLogicalPlace_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        Node gramps = new Node(Token.VAR);
        parent.addChildToBack(gramps);
        gramps.addChildToBack(n);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_functionNotInLogicalPlace_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_thisOnLeftSideOfAssign_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        CheckGlobalThis.assignLhsChild = new Node(Token.THIS);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_thisNotOnLeftSideOfAssign_shouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testVisit_thisOnGlobalScope_shouldReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.BLOCK);

        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getWarnings().size());
        assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getWarnings().get(0).getType());
    }

    @Test
    public void testVisit_thisNotOnGlobalScope_shouldNotReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, compiler.getWarnings().size());
    }

    @Test
    public void testVisit_thisOnLeftSideOfAssign_shouldReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        CheckGlobalThis.assignLhsChild = new Node(Token.THIS);

        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getWarnings().size());
        assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getWarnings().get(0).getType());
        assertNull(CheckGlobalThis.assignLhsChild);
    }
}