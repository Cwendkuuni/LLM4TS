package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = new DetailAST();
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
        
        aMods = null;
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST token = new DetailAST();
        token.setType(5);
        token.addChild(new DetailAST());
        token.getFirstChild().setText("public");
        token.addChild(new DetailAST());
        token.getFirstChild().getNextSibling().setText("protected");
        node.addChild(token);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        node = new DetailAST();
        node.setType(15);
        token = new DetailAST();
        token.setType(5);
        token.addChild(new DetailAST());
        token.getFirstChild().setText("package");
        node.addChild(token);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
        
        node = new DetailAST();
        node.setType(10);
        assertEquals(null, ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        node = new DetailAST();
        node.setType(7);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        node = new DetailAST();
        node.setType(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        node = new DetailAST();
        node.setType(10);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node = new DetailAST();
        node.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node = new DetailAST();
        node.setType(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        node = new DetailAST();
        node.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        node = new DetailAST();
        node.setType(157);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST();
        node.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST();
        node.setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST();
        node.setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(node));
        
        node = new DetailAST();
        node.setType(15);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(5);
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node = new DetailAST();
        node.setType(21);
        DetailAST parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(35);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(156);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node = new DetailAST();
        node.setType(94);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node = new DetailAST();
        node.setType(94);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        Scope scope = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast, scope));
        
        ast = null;
        scope = Scope.PACKAGE;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }
}