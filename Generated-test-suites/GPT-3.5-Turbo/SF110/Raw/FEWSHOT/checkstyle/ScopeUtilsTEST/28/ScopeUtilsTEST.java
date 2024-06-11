package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = createMods("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));

        ast = createMods("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));

        ast = createMods("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));

        ast = createMods("public protected");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast));

        ast = createMods(null);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = createParent(14);
        ast.attachChild(createMods("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast = createParent(15);
        ast.attachChild(createMods("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(ast));

        ast = createParent(157);
        ast.attachChild(createMods("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast));

        ast = createParent(154);
        ast.attachChild(createMods("public protected"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast));

        ast = createParent(14);
        ast.attachChild(createMods(null));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast));

        ast = createParent(136);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = createParent(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        ast = createParent(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = createParent(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = createParent(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        ast = createParent(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = createParent(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = createParent(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = createParent(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = createParent(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = createParent(154);
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        ast = createParent(157);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = createParent(14);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = createParent(9);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = createParent(8);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = createParent(11);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = createParent(12);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = createParent(14);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = createParent(14);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = createParent(15);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = createParent(157);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = createParent(154);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = createParent(136);
        assertTrue(ScopeUtils.isOuterMostType(ast));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = createParent(10);
        ast.attachChild(createParent(7));
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = createParent(35);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = createParent(156);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = createParent(21);
        ast.attachChild(createParent(96));
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = createParent(10);
        ast.attachChild(createParent(9));
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        ast = createParent(10);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = createParent(10);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = createParent(10);
        ast.attachChild(createParent(7));
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = createParent(10);
        ast.attachChild(createParent(35));
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = createParent(10);
        ast.attachChild(createParent(156));
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = createParent(21);
        ast.attachChild(createParent(96));
        assertTrue(ScopeUtils.isClassFieldDef(ast));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createParent(10);
        ast.attachChild(createParent(7));
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        ast = createParent(10);
        ast.attachChild(createParent(35));
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        ast = createParent(10);
        ast.attachChild(createParent(156));
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }

    private DetailAST createParent(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }

    private DetailAST createMods(String text) {
        DetailAST ast = new DetailAST();
        ast.setText(text);
        return ast;
    }
}