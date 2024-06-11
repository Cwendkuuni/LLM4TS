
package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));

        ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));

        ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));

        ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().setText("default");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        ast.setType(14);
        DetailAST modsAst = new DetailAST();
        ast.addChild(modsAst);
        modsAst.addChild(new AST());
        modsAst.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(15);
        modsAst = new DetailAST();
        ast.addChild(modsAst);
        modsAst.addChild(new AST());
        modsAst.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(157);
        modsAst = new DetailAST();
        ast.addChild(modsAst);
        modsAst.addChild(new AST());
        modsAst.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(15);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(8);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(11);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(12);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(157);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        parent = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(ast));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        DetailAST parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(7);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(35);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(8);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(21);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(96);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(21);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(8);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        DetailAST parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(7);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(35);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(156);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(8);
        assertTrue(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(21);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(96);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(21);
        parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(8);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        DetailAST parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(14);
        assertTrue(ScopeUtils.isInScope(ast, Scope.PACKAGE));

        parent.addChild(new AST());
        parent.getFirstChild().setText("public");
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }
}
