package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast1 = new DetailAST();
        ast1.addChild(new DetailAST());
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST(89));
        ast2.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.addChild(new DetailAST(89));
        ast3.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.addChild(new DetailAST(89));
        ast4.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast4));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertNull(ScopeUtils.getSurroundingScope(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        assertNull(ScopeUtils.getSurroundingScope(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(157);
        assertNull(ScopeUtils.getSurroundingScope(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(154);
        assertNull(ScopeUtils.getSurroundingScope(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast5));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(157);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast5));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast5));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(154);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast5));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(157);
        assertFalse(ScopeUtils.isInEnumBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        assertFalse(ScopeUtils.isInEnumBlock(ast5));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(ast5));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast1 = new DetailAST();
        ast1.getParent();
        assertTrue(ScopeUtils.isOuterMostType(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.getParent().setType(14);
        assertFalse(ScopeUtils.isOuterMostType(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.getParent().setType(15);
        assertFalse(ScopeUtils.isOuterMostType(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.getParent().setType(157);
        assertFalse(ScopeUtils.isOuterMostType(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.getParent().setType(154);
        assertFalse(ScopeUtils.isOuterMostType(ast5));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        assertFalse(ScopeUtils.isLocalVariableDef(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(7);
        ast2.setParent(new DetailAST(10));
        assertTrue(ScopeUtils.isLocalVariableDef(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(35);
        ast3.setParent(new DetailAST(10));
        assertTrue(ScopeUtils.isLocalVariableDef(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(156);
        ast4.setParent(new DetailAST(10));
        assertTrue(ScopeUtils.isLocalVariableDef(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(21);
        ast5.setParent(new DetailAST(96));
        assertTrue(ScopeUtils.isLocalVariableDef(ast5));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(10);
        ast2.getParent().setType(7);
        assertTrue(ScopeUtils.isClassFieldDef(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(10);
        ast3.getParent().setType(35);
        assertTrue(ScopeUtils.isClassFieldDef(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(10);
        ast4.getParent().setType(156);
        assertTrue(ScopeUtils.isClassFieldDef(ast4));

        DetailAST ast5 = new DetailAST();
        ast5.setType(10);
        ast5.getParent().setType(96);
        assertTrue(ScopeUtils.isClassFieldDef(ast5));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        DetailAST ast2 = new DetailAST();
        ast2.setType(157);
        assertFalse(ScopeUtils.isInScope(ast1, Scope.PUBLIC));
        assertFalse(ScopeUtils.isInScope(ast2, Scope.PRIVATE));

        DetailAST ast3 = new DetailAST();
        ast3.setType(10);
        ast3.setParent(ast1);
        assertTrue(ScopeUtils.isInScope(ast3, Scope.PACKAGE));
    }
}