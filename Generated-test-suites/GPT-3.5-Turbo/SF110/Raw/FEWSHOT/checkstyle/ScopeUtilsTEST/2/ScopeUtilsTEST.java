package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(14, "public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
        ast.addChild(new AST(15, "protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
        
        ast.addChild(new AST(157, "private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));
        
        assertNull(ScopeUtils.getScopeFromMods(null));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(14, "public"));
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(15, "protected"));
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(157, "private"));
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(136, ""));
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(15, ""));
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(14, ""));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(154, ""));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(136, ""));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(157, ""));
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(14, ""));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(154, ""));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(136, ""));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(15, ""));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(157, ""));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(14, ""));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(154, ""));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(136, ""));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(154, ""));
        assertTrue(ScopeUtils.isInEnumBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(15, ""));
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(157, ""));
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(14, ""));
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(136, ""));
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(9, ""));
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(8, ""));
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(11, ""));
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(12, ""));
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(15, ""));
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        assertNull(ScopeUtils.isOuterMostType(ast));
        
        ast = new DetailAST();
        DetailAST child = new DetailAST();
        child.addChild(new AST(15, ""));
        ast.addChild(child);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast = new DetailAST();
        child = new DetailAST();
        child.addChild(new AST(157, ""));
        ast.addChild(child);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast = new DetailAST();
        child = new DetailAST();
        child.addChild(new AST(14, ""));
        ast.addChild(child);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast = new DetailAST();
        child = new DetailAST();
        child.addChild(new AST(154, ""));
        ast.addChild(child);
        assertFalse(ScopeUtils.isOuterMostType(ast));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        DetailAST parent = ast.getParent();
        parent.addChild(new AST(7, ""));
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        parent = ast.getParent();
        parent.addChild(new AST(35, ""));
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        parent = ast.getParent();
        parent.addChild(new AST(156, ""));
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        parent = ast.getParent();
        parent.addChild(new AST(96, ""));
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        DetailAST parent = ast.getParent();
        parent.addChild(new AST(7, ""));
        assertTrue(ScopeUtils.isClassFieldDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        parent = ast.getParent();
        parent.addChild(new AST(35, ""));
        assertTrue(ScopeUtils.isClassFieldDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        parent = ast.getParent();
        parent.addChild(new AST(156, ""));
        assertTrue(ScopeUtils.isClassFieldDef(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST(10, ""));
        ast.setParent(new DetailAST());
        DetailAST grandParent = new DetailAST();
        grandParent.addChild(new AST(136, ""));
        ast.getParent().setParent(grandParent);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        DetailAST surroundAst = new DetailAST();
        surroundAst.addChild(new AST(14, "public"));
        ast.setParent(surroundAst);
        ast.setType(15);
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        ast = new DetailAST();
        surroundAst = new DetailAST();
        surroundAst.addChild(new AST(14, "public"));
        ast.setParent(surroundAst);
        ast.setType(14);
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }

}