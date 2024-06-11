package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        assertNull(ScopeUtils.getScopeFromMods(ast));
        
        ast.setType(14);
        ast.setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
        ast.setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
        
        ast.setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast.setType(14);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast));
        
        ast.setType(15);
        DetailAST token = new DetailAST();
        token.setType(5);
        ast.addChild(token);
        ast.setText("test");
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast.setText("test");
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        ast.setText("test");
        assertNull(ScopeUtils.getSurroundingScope(ast));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        ast.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        ast.setType(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        ast.setType(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        ast.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        ast.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));
        
        ast.setType(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        ast.setType(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        ast.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(ast));
        
        ast.setType(136);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        ast.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast.setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        ast.setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(ast));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast.setType(15);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast.setType(157);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        ast.setType(154);
        assertFalse(ScopeUtils.isOuterMostType(ast));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        ast.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        ast.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
        
        ast.setType(10);
        parent.setType(35);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
        
        ast.setType(10);
        parent.setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
        
        ast.setType(21);
        parent.setType(96);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        ast.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        ast.setType(10);
        parent.setType(35);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        ast.setType(10);
        parent.setType(156);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        ast.setType(21);
        parent.setType(96);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));
        
        ast.setType(14);
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));
        
        ast.setType(14);
        assertNull(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        ast.setType(14);
        assertNull(ScopeUtils.isInScope(ast, Scope.PROTECTED));
        
        ast.setType(14);
        assertNull(ScopeUtils.isInScope(ast, Scope.PRIVATE));
    }
}