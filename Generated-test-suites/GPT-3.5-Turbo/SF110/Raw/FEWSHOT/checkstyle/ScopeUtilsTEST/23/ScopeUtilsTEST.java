package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST(14, "public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        aMods = new DetailAST();
        aMods.addChild(new AST(16, "default"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        aMods = new DetailAST();
        aMods.addChild(new AST(41, "protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));

        aMods = new DetailAST();
        aMods.addChild(new AST(63, "private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "public"));
        DetailAST parent = new DetailAST();
        parent.addChild(node);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

        node = new DetailAST();
        node.addChild(new AST(15, "protected"));
        parent = new DetailAST();
        parent.addChild(node);
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));

        node = new DetailAST();
        node.addChild(new AST(157, "private"));
        parent = new DetailAST();
        parent.addChild(node);
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

        node = new DetailAST();
        node.addChild(new AST(154, "default"));
        parent = new DetailAST();
        parent.addChild(node);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "public"));
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = new DetailAST();
        node.addChild(new AST(14, "protected"));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = new DetailAST();
        node.addChild(new AST(154, "private"));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(157, "public"));
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

       node = new DetailAST();
       node.addChild(new AST(15, "protected"));
       assertFalse(ScopeUtils.isInAnnotationBlock(node));

       node = new DetailAST();
       node.addChild(new AST(154, "private"));
       assertFalse(ScopeUtils.isInAnnotationBlock(node));
   }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "public"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new DetailAST();
        node.addChild(new AST(157, "protected"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new DetailAST();
        node.addChild(new AST(157, "private"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new DetailAST();
        node.addChild(new AST(14, "default"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(154, "public"));
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = new DetailAST();
        node.addChild(new AST(15, "protected"));
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = new DetailAST();
        node.addChild(new AST(157, "private"));
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(9, "public"));
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = new DetailAST();
        node.addChild(new AST(8, "protected"));
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = new DetailAST();
        node.addChild(new AST(11, "private"));
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = new DetailAST();
        node.addChild(new AST(12, "default"));
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "public"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = new DetailAST();
        node.addChild(new AST(15, "protected"));
    assertFalse(ScopeUtils.isOuterMostType(node));

        node = new DetailAST();
        node.addChild(new AST(157, "private"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = new DetailAST();
        node.addChild(new AST(154, "default"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = new DetailAST();
        node.addChild(new AST(15, "interface"));
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        parent.addChild(node);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = new DetailAST();
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        parent.addChild(node);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(15);
        parent.addChild(node);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        parent.addChild(node);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = new DetailAST();
        node.setType(15);
        parent = new DetailAST();
        parent.setType(10);
        parent.addChild(node);
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(14, "public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.addChild(new AST(15, "protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.addChild(new AST(157, "private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.addChild(new AST(154, "default"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast));
    }
}