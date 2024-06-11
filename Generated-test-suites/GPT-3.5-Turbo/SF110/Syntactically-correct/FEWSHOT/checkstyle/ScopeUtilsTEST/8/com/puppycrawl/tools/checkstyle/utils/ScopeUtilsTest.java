package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.setText("public");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);
        
        aMods.setText("protected");
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, scope);
        
        aMods.setText("private");
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, scope);
        
        aMods.setText("");
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, scope);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        DetailAST token = new DetailAST();
        token.setType(5);
        token.addChild(new DetailAST());
        parent.addChild(token);
        node.setParent(parent);
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);
        
        parent.setType(15);
        node.setParent(parent);
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
        
        parent.setType(157);
        node.setParent(parent);
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
        
        parent.setType(154);
        node.setParent(parent);
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
        
        parent.setType(136);
        node.setParent(parent);
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, scope);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        boolean inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(inInterfaceBlock);
        
        parent.setType(14);
        node.setParent(parent);
        inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(inInterfaceBlock);
        
        parent.setType(154);
        node.setParent(parent);
        inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(inInterfaceBlock);
        
        parent.setType(157);
        node.setParent(parent);
        inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(inInterfaceBlock);
        
        parent.setType(136);
        node.setParent(parent);
        inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(inInterfaceBlock);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        boolean inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(inAnnotationBlock);
        
        parent.setType(14);
        node.setParent(parent);
        inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(inAnnotationBlock);
        
        parent.setType(154);
        node.setParent(parent);
        inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(inAnnotationBlock);
        
        parent.setType(15);
        node.setParent(parent);
        inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(inAnnotationBlock);
        
        parent.setType(136);
        node.setParent(parent);
        inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(inAnnotationBlock);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        boolean inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(inInterfaceOrAnnotationBlock);
        
        parent.setType(14);
        node.setParent(parent);
        inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(inInterfaceOrAnnotationBlock);
        
        parent.setType(154);
        node.setParent(parent);
        inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(inInterfaceOrAnnotationBlock);
        
        parent.setType(157);
        node.setParent(parent);
        inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(inInterfaceOrAnnotationBlock);
        
        parent.setType(136);
        node.setParent(parent);
        inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(inInterfaceOrAnnotationBlock);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        boolean inEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertTrue(inEnumBlock);
        
        parent.setType(14);
        node.setParent(parent);
        inEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(inEnumBlock);
        
        parent.setType(157);
        node.setParent(parent);
        inEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(inEnumBlock);
        
        parent.setType(15);
        node.setParent(parent);
        inEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(inEnumBlock);
        
        parent.setType(136);
        node.setParent(parent);
        inEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(inEnumBlock);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        boolean inCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(inCodeBlock);
        
        parent.setType(8);
        node.setParent(parent);
        inCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(inCodeBlock);
        
        parent.setType(11);
        node.setParent(parent);
        inCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(inCodeBlock);
        
        parent.setType(12);
        node.setParent(parent);
        inCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(inCodeBlock);
        
        parent.setType(13);
        node.setParent(parent);
        inCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertFalse(inCodeBlock);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        boolean outerMostType = ScopeUtils.isOuterMostType(node);
        assertFalse(outerMostType);
        
        parent.setType(15);
        node.setParent(parent);
        outerMostType = ScopeUtils.isOuterMostType(node);
        assertFalse(outerMostType);
        
        parent.setType(157);
        node.setParent(parent);
        outerMostType = ScopeUtils.isOuterMostType(node);
        assertFalse(outerMostType);
        
        parent.setType(154);
        node.setParent(parent);
        outerMostType = ScopeUtils.isOuterMostType(node);
        assertFalse(outerMostType);
        
        parent.setType(12);
        node.setParent(parent);
        outerMostType = ScopeUtils.isOuterMostType(node);
        assertTrue(outerMostType);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        boolean localVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(localVariableDef);
        
        parent.setType(35);
        node.setParent(parent);
        localVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(localVariableDef);
        
        parent.setType(156);
        node.setParent(parent);
        localVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(localVariableDef);
        
        parent.setType(96);
        node.setParent(parent);
        localVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(localVariableDef);
        
        parent.setType(100);
        node.setParent(parent);
        localVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertFalse(localVariableDef);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        boolean classFieldDef = ScopeUtils.isClassFieldDef(node);
        assertTrue(classFieldDef);
        
        parent.setType(7);
        node.setParent(parent);
        classFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(classFieldDef);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        DetailAST surroundingScopeOfAstToken = new DetailAST();
        surroundingScopeOfAstToken.setType(14);
        boolean inScope = ScopeUtils.isInScope(ast, Scope.PACKAGE);
        assertTrue(inScope);
        
        surroundingScopeOfAstToken.setType(15);
        inScope = ScopeUtils.isInScope(ast, Scope.PUBLIC);
        assertFalse(inScope);
    }
}