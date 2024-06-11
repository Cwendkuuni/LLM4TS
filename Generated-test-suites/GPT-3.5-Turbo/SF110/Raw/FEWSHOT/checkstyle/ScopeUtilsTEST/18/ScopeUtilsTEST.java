package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(null));
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
        AST publicToken = new AST();
        publicToken.setText("public");
        aMods.addChild(publicToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        AST protectedToken = new AST();
        protectedToken.setText("protected");
        aMods.addChild(protectedToken);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
        AST privateToken = new AST();
        privateToken.setText("private");
        aMods.addChild(privateToken);
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
        AST packageToken = new AST();
        packageToken.setText("package");
        aMods.addChild(packageToken);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        assertNull(ScopeUtils.getSurroundingScope(null));
        DetailAST node = new DetailAST();
        assertEquals(null, ScopeUtils.getSurroundingScope(node));
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertNull(ScopeUtils.getSurroundingScope(node));
        
        DetailAST token1 = new DetailAST();
        token1.setText("public");
        parent.addChild(token1);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        parent.setType(15);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        parent.setType(157);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        parent.setType(154);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
        
        parent.setType(136);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        assertFalse(ScopeUtils.isInInterfaceBlock(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(157);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        parent.setType(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        assertFalse(ScopeUtils.isInAnnotationBlock(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        parent.setType(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(154);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        parent.setType(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        assertFalse(ScopeUtils.isInEnumBlock(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(157);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        parent.setType(136);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        assertFalse(ScopeUtils.isInCodeBlock(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        parent.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        parent.setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        parent.setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        assertTrue(ScopeUtils.isOuterMostType(null));
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent.setType(15);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent.setType(154);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent.setType(157);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        parent.setType(136);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        assertFalse(ScopeUtils.isLocalVariableDef(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent.setType(35);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        parent.setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void isClassFieldDef() {
        assertFalse(ScopeUtils.isClassFieldDef(null));
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        parent.setType(35);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        parent.setType(156);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        node.setType(10);
        parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        assertFalse(ScopeUtils.isInScope(null, Scope.PUBLIC));
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }
}