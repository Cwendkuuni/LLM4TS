package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST token = new DetailAST();
        token.setType(14);
        token.setText("public");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(token));
        
        token.setType(14);
        token.setText("protected");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(token));
        
        token.setType(14);
        token.setText("private");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(token));
        
        token.setType(14);
        token.setText("foo");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(token));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST token = new DetailAST();
        token.setType(14);
        DetailAST mods = new DetailAST();
        mods.setFirstChild(token);
        assertEquals(null, ScopeUtils.getSurroundingScope(mods));
        
        token.setType(14);
        token.setText("public");
        mods = new DetailAST();
        mods.setFirstChild(token);
        assertEquals(null, ScopeUtils.getSurroundingScope(mods));
        
        token.setType(14);
        token.setText("protected");
        mods = new DetailAST();
        mods.setFirstChild(token);
        assertEquals(null, ScopeUtils.getSurroundingScope(mods));
        
        token.setType(14);
        token.setText("private");
        mods = new DetailAST();
        mods.setFirstChild(token);
        assertEquals(null, ScopeUtils.getSurroundingScope(mods));
        
        token.setType(14);
        token.setText("foo");
        mods = new DetailAST();
        mods.setFirstChild(token);
        assertEquals(null, ScopeUtils.getSurroundingScope(mods));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST token = new DetailAST();
        token.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(token));
        
        token.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(token));
        
        token.setType(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(token));
        
        token.setType(157);
        assertFalse(ScopeUtils.isInInterfaceBlock(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(token));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST token = new DetailAST();
        token.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(token));
        
        token.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(token));
        
        token.setType(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(token));
        
        token.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(token));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST token = new DetailAST();
        token.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(token));
        
        token.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(token));
        
        token.setType(154);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(token));
        
        token.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(token));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST token = new DetailAST();
        token.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(token));
        
        token.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(token));
        
        token.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(token));
        
        token.setType(157);
        assertFalse(ScopeUtils.isInEnumBlock(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isInEnumBlock(token));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST token = new DetailAST();
        token.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(token));
        
        token.setType(15);
        assertFalse(ScopeUtils.isInCodeBlock(token));
        
        token.setType(154);
        assertFalse(ScopeUtils.isInCodeBlock(token));
        
        token.setType(157);
        assertFalse(ScopeUtils.isInCodeBlock(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isInCodeBlock(token));
        
        token.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(token));
        
        token.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(token));
        
        token.setType(11);
        assertTrue(ScopeUtils.isInCodeBlock(token));
        
        token.setType(12);
        assertTrue(ScopeUtils.isInCodeBlock(token));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.setText("foo");
        DetailAST token = new DetailAST();
        token.setParent(parent);
        token.setType(14);
        assertFalse(ScopeUtils.isOuterMostType(token));
        
        token.setType(15);
        assertFalse(ScopeUtils.isOuterMostType(token));
        
        token.setType(154);
        assertFalse(ScopeUtils.isOuterMostType(token));
        
        token.setType(157);
        assertFalse(ScopeUtils.isOuterMostType(token));
        
        token.setType(136);
        assertFalse(ScopeUtils.isOuterMostType(token));
        
        token.setType(9);
        assertTrue(ScopeUtils.isOuterMostType(token));
        
        token.setType(8);
        assertTrue(ScopeUtils.isOuterMostType(token));
        
        token.setType(11);
        assertTrue(ScopeUtils.isOuterMostType(token));
        
        token.setType(12);
        assertTrue(ScopeUtils.isOuterMostType(token));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST token = new DetailAST();
        token.setType(10);
        token.setText("foo");
        DetailAST parent = new DetailAST();
        parent.setType(7);
        parent.setText("foo");
        parent.setParent(token);
        
        assertFalse(ScopeUtils.isLocalVariableDef(token));
        
        token.setType(10);
        token.setText("foo");
        parent.setType(35);
        parent.setText("foo");
        parent.setParent(token);
        assertTrue(ScopeUtils.isLocalVariableDef(token));
        
        token.setType(10);
        token.setText("foo");
        parent.setType(156);
        parent.setText("foo");
        parent.setParent(token);
        assertTrue(ScopeUtils.isLocalVariableDef(token));
        
        token.setType(21);
        token.setText("foo");
        parent.setType(96);
        parent.setText("foo");
        parent.setParent(token);
        assertTrue(ScopeUtils.isLocalVariableDef(token));
        
        token.setType(10);
        token.setText("foo");
        parent.setType(21);
        parent.setText("foo");
        parent.setParent(token);
        assertFalse(ScopeUtils.isLocalVariableDef(token));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST token = new DetailAST();
        token.setType(10);
        token.setText("foo");
        assertFalse(ScopeUtils.isClassFieldDef(token));
        
        token.setType(10);
        token.setText("foo");
        DetailAST parent = new DetailAST();
        parent.setType(7);
        parent.setText("foo");
        parent.setParent(token);
        assertTrue(ScopeUtils.isClassFieldDef(token));
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(14);
        ast.setText("foo");
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }
}