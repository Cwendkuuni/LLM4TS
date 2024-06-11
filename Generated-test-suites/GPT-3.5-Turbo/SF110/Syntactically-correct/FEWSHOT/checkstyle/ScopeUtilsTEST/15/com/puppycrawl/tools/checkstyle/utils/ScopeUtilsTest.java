package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.setText("public");

        Scope scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setText("public");
        ast.setParent(parent);

        Scope scope = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);

        boolean isInInterface = ScopeUtils.isInInterfaceBlock(ast);
        assertTrue(isInInterface);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);

        boolean isInAnnotation = ScopeUtils.isInAnnotationBlock(ast);
        assertTrue(isInAnnotation);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);

        boolean isInInterfaceOrAnnotation = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(isInInterfaceOrAnnotation);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);

        boolean isInEnum = ScopeUtils.isInEnumBlock(ast);
        assertTrue(isInEnum);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);
        ast.setParent(parent);

        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertTrue(isInCodeBlock);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);

        boolean isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertTrue(isOuterMostType);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10);

        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        assertFalse(isLocalVariableDef);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10);

        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        assertFalse(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setText("public");

        Scope scope = ScopeUtils.getScopeFromMods(ast);
        boolean isInScope = ScopeUtils.isInScope(ast, scope);
        assertTrue(isInScope);
    }
}