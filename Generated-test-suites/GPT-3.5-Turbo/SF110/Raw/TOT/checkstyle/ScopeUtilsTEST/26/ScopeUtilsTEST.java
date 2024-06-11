
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.addChild(new AST());
        aMods.addChild(new AST());
        
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, scope);
        
        aMods = new DetailAST();
        aMods.addChild(new AST("public"));
        
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);
        
        aMods = new DetailAST();
        aMods.addChild(new AST("protected"));
        
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, scope);
        
        aMods = new DetailAST();
        aMods.addChild(new AST("private"));
        
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, scope);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
        
        node = new DetailAST();
        node.setParent(new DetailAST(14));
        
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);
        
        node = new DetailAST();
        node.setParent(new DetailAST(15));
        
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
        
        node = new DetailAST();
        node.setParent(new DetailAST(136));
        
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, scope);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(15));
        
        result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(14));
        
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(136));
        
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(157));
        
        result = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(14));
        
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(136));
        
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(15));
        
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(157));
        
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(14));
        
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(136));
        
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(154));
        
        result = ScopeUtils.isInEnumBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(15));
        
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(157));
        
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(9));
        
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(8));
        
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(10));
        
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(11));
        
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(12));
        
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(14));
        
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(15));
        
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(157));
        
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setParent(new DetailAST(154));
        
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(35);
        node.setParent(parent);
        
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(156);
        node.setParent(parent);
        
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(35);
        node.setParent(parent);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(156);
        node.setParent(parent);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setParent(new DetailAST(15));
        
        Scope scope = ScopeUtils.getSurroundingScope(ast);
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);
    }
}
