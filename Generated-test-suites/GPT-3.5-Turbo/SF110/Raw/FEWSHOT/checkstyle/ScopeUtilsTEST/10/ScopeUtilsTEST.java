package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        aMods.addChild(new AST());
        aMods.getFirstChild().getNextSibling().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));

        aMods.addChild(new AST());
        aMods.getFirstChild().getNextSibling().getNextSibling().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));

        aMods.addChild(new AST());
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        aMods = new DetailAST();
        assertNull(ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(14));
        node.getFirstChild().addChild(new DetailAST(5));

        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

        node.addChild(new DetailAST(15));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));

        node.addChild(new DetailAST(157));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));

        node.addChild(new DetailAST(154));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));

        node.addChild(new DetailAST(136));
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));

        node.addChild(new DetailAST(136));
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15));
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node.getFirstChild().setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node.addChild(new DetailAST(154));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node.addChild(new DetailAST(157));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node.addChild(new DetailAST(136));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(157));
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node.getFirstChild().setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node.addChild(new DetailAST(154));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node.addChild(new DetailAST(15));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node.addChild(new DetailAST(136));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.addChild(new DetailAST(157));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.addChild(new DetailAST(14));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.addChild(new DetailAST(136));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.addChild(new DetailAST(154));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.addChild(new DetailAST(154));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(154));
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node.getFirstChild().setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.addChild(new DetailAST(15));
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.addChild(new DetailAST(157));
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.addChild(new DetailAST(136));
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(9));
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.getFirstChild().setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.getFirstChild().setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.getFirstChild().setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.getFirstChild().setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.setParent(new DetailAST());

        node.getParent().setType(14);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.getParent().setType(15);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.getParent().setType(157);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.getParent().setType(154);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.getParent().setType(10);
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.setParent(new DetailAST());

        node.getParent().setType(7);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.getParent().setType(35);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.getParent().setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.setType(21);
        node.getParent().setType(96);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.getParent().setType(97);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.setParent(new DetailAST());

        node.getParent().setType(7);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.getParent().setType(35);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.getParent().setType(156);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.setType(21);
        node.getParent().setType(96);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.getParent().setType(97);
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setParent(new DetailAST());
        ast.getParent().setType(14);
        assertEquals(ScopeUtils.getSurroundingScope(ast), ScopeUtils.getSurroundingScope(ast));

        ast.getParent().setType(15);
        assertEquals(ScopeUtils.getSurroundingScope(ast), ScopeUtils.getSurroundingScope(ast));

        ast.getParent().setType(157);
        assertEquals(ScopeUtils.getSurroundingScope(ast), ScopeUtils.getSurroundingScope(ast));

        ast.getParent().setType(154);
        assertEquals(ScopeUtils.getSurroundingScope(ast), ScopeUtils.getSurroundingScope(ast));

        ast.getParent().setType(136);
        assertEquals(ScopeUtils.getSurroundingScope(ast), ScopeUtils.getSurroundingScope(ast));

        ast.getParent().setType(136);
        assertNull(ScopeUtils.getSurroundingScope(ast));
    }
}