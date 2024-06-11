package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        
        // Test with no modifiers
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, result);
        
        // Test with public modifier
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);
        
        // Test with protected modifier
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().getNextSibling().setText("protected");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);
        
        // Test with private modifier
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().getNextSibling().getNextSibling().setText("private");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        
        // Test with no surrounding scope
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertNull(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        
        // Test with surrounding package scope
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, result);
        
        parent.setType(15);
        
        // Test with surrounding public scope
        DetailAST mods = new DetailAST();
        parent.addChild(mods);
        mods.setType(5);
        mods.addChild(new DetailAST());
        mods.getFirstChild().setText("public");
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);
        
        // Test with surrounding protected scope
        parent.getChild(0).setText("protected");
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PROTECTED, result);
        
        // Test with surrounding private scope
        parent.getChild(0).setText("private");
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, result);
        
        // Test with surrounding anonymous inner scope
        parent.setType(136);
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        
        // Test when not in interface block
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        // Test when in interface block
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        
        // Test when not in annotation block
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        // Test when in annotation block
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        result = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        
        // Test when not in interface or annotation block
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        // Test when in interface block
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
        
        // Test when in annotation block
        parent.setType(157);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        
        // Test when not in enum block
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        // Test when in enum block
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        result = ScopeUtils.isInEnumBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        
        // Test when not in code block
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
        
        // Test when in code block
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        // Test when parent is null
        node.setParent(null);
        result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        
        // Test when node is outer most type
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
        
        // Test when node is not outer most type
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        
        // Test when node is not a local variable definition
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        // Test when node is a local variable definition as variable
        DetailAST parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(7);
        parent.setParent(grandParent);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        // Test when node is a local variable definition as parameter
        parent.setType(21);
        grandParent.setType(96);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        
        // Test when node is not a class field definition
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        // Test when node is a class field definition
        node.setType(10);
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
        
        // Test when node is a local variable definition
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        
        // Test when not in scope
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        // Test when in scope
        ast.setType(14);
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        parent.addChild(new DetailAST());
        parent.getFirstChild().setType(5);
        parent.getFirstChild().addChild(new DetailAST());
        parent.getFirstChild().getFirstChild().setText("public");
        result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);
    }
    
}