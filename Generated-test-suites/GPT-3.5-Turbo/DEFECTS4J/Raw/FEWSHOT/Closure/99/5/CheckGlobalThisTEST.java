package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckLevel level;

    @Test
    public void testShouldTraverse_FunctionConstructor() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionThisAnnotation() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FuncWithoutLogicalPlace() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_AssignHasAssignLhsChild() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_AssignNotPrototypeProperty() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_AssignPrototypeProperty() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Verify
        assertEquals(false, result);
    }

    @Test
    public void testVisit_ThisNotOnAssignLhsChild() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        checkGlobalThis.visit(null, null, null);

        // Verify
    }

    @Test
    public void testVisit_ThisOnAssignLhsChild() {
        // Setup
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test
        checkGlobalThis.visit(null, null, null);

        // Verify
    }

    @Test
    public void testGetFunctionJsDocInfo_JsDocNotNull() {
        // Setup
        Node n = new Node();
        
        // Test
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentTypeIsName() {
        // Setup
        Node n = new Node();
        
        // Test
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentTypeIsAssign() {
        // Setup
        Node n = new Node();
        
        // Test
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentTypeIsNameAndJSDocIsNull() {
        // Setup
        Node n = new Node();
        
        // Test
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Verify
        assertNotNull(result);
    }
}