package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        AST publicToken = new AST();
        publicToken.setText("public");
        AST protectedToken = new AST();
        protectedToken.setText("protected");
        AST privateToken = new AST();
        privateToken.setText("private");
        
        aMods.addChild(publicToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        
        aMods.getChildren().clear();
        aMods.addChild(protectedToken);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
        
        aMods.getChildren().clear();
        aMods.addChild(privateToken);
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
        
        aMods.getChildren().clear();
        assertNull(ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14); // class
        DetailAST mods = new DetailAST();
        mods.setType(5); // modifier list
        mods.addChild(new AST());
        parent.addChild(mods);
        node.setParent(parent);
        
        assertEquals(ScopeUtils.getScopeFromMods(mods), ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15); // interface
        node.setParent(parent);
        
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157); // annotation
        node.setParent(parent);
        
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(15); // interface
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15); // interface
        node.setParent(parent);
        
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(157); // annotation
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154); // enum
        node.setParent(parent);
        
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(15); // interface
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(11); // if statement
        parent.addChild(node);
        
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14); // class
        parent.addChild(node);
        
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        parent.setType(15); // interface
        assertFalse(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(7); // variable def
        parent.addChild(node);
        
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent.setType(14); // class
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10); // variable def
       
        assertTrue(ScopeUtils.isClassFieldDef(node));
        
        node.setType(14); // class
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        
        assertTrue(ScopeUtils.isInScope(ast, scope));
        
        scope = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }
}