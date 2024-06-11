package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        aMods.addChild(new AST("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        aMods.addChild(new AST("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        aMods.addChild(new AST("default"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST(14);
        parent.addChild(new DetailAST(5));
        node.setParent(parent);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
        
        parent = new DetailAST(15);
        parent.addChild(new DetailAST(5));
        node.setParent(parent);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        parent = new DetailAST(157);
        parent.addChild(new DetailAST(5));
        node.setParent(parent);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
        
        parent = new DetailAST(154);
        parent.addChild(new DetailAST(5));
        node.setParent(parent);
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        node = new DetailAST(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        node = new DetailAST(157);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        node = new DetailAST(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        node = new DetailAST(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node = new DetailAST(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node = new DetailAST(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST(154);
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        node = new DetailAST(14);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        node = new DetailAST(15);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST(9);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST(8);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST(11);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST(12);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST(10);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent = new DetailAST(15);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent = new DetailAST(157);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent = new DetailAST(154);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent = new DetailAST();
        node.setParent(parent);
        assertTrue(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST(10);
        DetailAST parent = new DetailAST(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent = new DetailAST(35);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent = new DetailAST(156);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent = new DetailAST(96);
        node.setParent(parent);
        node.setType(21);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent = new DetailAST();
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST(10);
        assertTrue(ScopeUtils.isClassFieldDef(node));
        
        node = new DetailAST(11);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node = new DetailAST(12);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node = new DetailAST(9);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        Scope scope = Scope.PUBLIC;
        DetailAST ast = new DetailAST();
        assertTrue(ScopeUtils.isInScope(ast, scope));
        
        DetailAST parent = new DetailAST(15);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInScope(ast, scope));
        
        parent = new DetailAST(157);
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }
}