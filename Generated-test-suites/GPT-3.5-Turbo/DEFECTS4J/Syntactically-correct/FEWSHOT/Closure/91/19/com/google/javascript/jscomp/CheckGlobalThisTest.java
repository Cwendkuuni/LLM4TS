package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_withFunctionConstructor_shouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(null, null, new Node(Token.FUNCTION));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionWithThisAnnotation_shouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(null, null, new Node(Token.FUNCTION));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionWithoutLogicalPlaceForThisAnnotation_shouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(null, null, new Node(Token.FUNCTION));

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withNonGlobalContext_shouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(null, null, new Node(Token.FUNCTION));

        assertEquals(false, result);
    }

    @Test
    public void testShouldReportThis_withThisOnLeftSideOfAssign_shouldReturnTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));

        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_withThisWithinPropertyAccess_shouldReturnTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));

        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_withThisWithinNonPropertyAccess_shouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.NAME));

        assertEquals(false, result);
    }

    @Test
    public void testVisit_withThisAndShouldNotReportThis_shouldNotReportGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);

        checkGlobalThis.visit(t, n, null);

        assertEquals(0, compiler.reports.size());
    }

    @Test
    public void testVisit_withThisAndShouldReportThis_shouldReportGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.reports.size());
    }

    @Test
    public void testVisit_withAssignLhsChild_shouldSetAssignLhsChildToNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.shouldReportThis(n, parent);
        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, parent);

        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_withJSDocNotNull_shouldReturnJSDoc() {
        Node n = new Node();
        n.setJSDocInfo(new JSDocInfo());

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(n.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withJSDocNullAndParentName_shouldReturnJSDoc() {
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        Node n = new Node();
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(parent.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withJSDocNullAndParentAssign_shouldReturnJSDoc() {
        Node grandparent = new Node(Token.VAR);
        grandparent.setJSDocInfo(new JSDocInfo());
        Node parent = new Node(Token.NAME);
        parent.setParent(grandparent);
        parent.setJSDocInfo(null);
        Node n = new Node();
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(grandparent.getJSDocInfo(), result);
    }
}