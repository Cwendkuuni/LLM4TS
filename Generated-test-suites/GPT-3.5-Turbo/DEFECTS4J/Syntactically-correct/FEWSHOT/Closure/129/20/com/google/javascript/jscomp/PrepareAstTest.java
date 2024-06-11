package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    private PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst(new AbstractCompiler());
    }

    @Test
    public void testProcess_checkOnly() {
        // TODO: Implement test case
    }

    @Test
    public void testProcess_externsNullAndRootNull() {
        // TODO: Implement test case
    }

    @Test
    public void testProcess_externsNotNullAndRootNull() {
        // TODO: Implement test case
    }

    @Test
    public void testProcess_externsNullAndRootNotNull() {
        // TODO: Implement test case
    }

    @Test
    public void testProcess_externsAndRootNotNull() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeNodeTypes() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_ifNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_whileNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_doNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_forNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_switchNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_labelNode() {
        // TODO: Implement test case
    }

    @Test
    public void testNormalizeBlocks_emptyNode() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_objectLitNode() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_objectLitNode_keyHasJSDocInfo() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_objectLitNode_keyHasNoJSDocInfo_valueIsFunction() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_objectLitNode_keyHasNoJSDocInfo_valueIsNotFunction() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_functionNode() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_functionNode_parentHasNoJSDocInfo() {
        // TODO: Implement test case
    }

    @Test
    public void testPrepareAnnotations_functionNode_parentHasJSDocInfo_isJavaDispatcher() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateCalls_freeCall() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateCalls_notFreeCall() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateCalls_directEval() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateCalls_notDirectEval() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateDispatchers_assignNode() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateDispatchers_notAssignNode() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateDispatchers_parentHasNoJSDocInfo() {
        // TODO: Implement test case
    }

    @Test
    public void testAnnotateDispatchers_parentHasJSDocInfo_isNotJavaDispatcher() {
        // TODO: Implement test case
    }
}