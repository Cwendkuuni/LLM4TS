package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast1 = new DetailAST();
        ast1.addChild(new DetailAST());
        ast1.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast1));
        
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast2));
        
        DetailAST ast3 = new DetailAST();
        ast3.addChild(new DetailAST());
        ast3.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast3));

        DetailAST ast4 = new DetailAST();
        assertNull(ScopeUtils.getScopeFromMods(ast4));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        DetailAST mod1 = new DetailAST();
        ast1.addChild(mod1);
        mod1.addChild(new DetailAST());
        mod1.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast1));
        
        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        DetailAST mod2 = new DetailAST();
        ast2.addChild(mod2);
        mod2.addChild(new DetailAST());
        mod2.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(ast2));
        
        DetailAST ast3 = new DetailAST();
        ast3.setType(157);
        DetailAST mod3 = new DetailAST();
        ast3.addChild(mod3);
        mod3.addChild(new DetailAST());
        mod3.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast3));

        DetailAST ast4 = new DetailAST();
        ast4.setType(154);
        assertNull(ScopeUtils.getSurroundingScope(ast4));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(157);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast3));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast3));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast3));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(ast3));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(8);
        assertTrue(ScopeUtils.isInCodeBlock(ast3));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        assertTrue(ScopeUtils.isOuterMostType(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(154);
        assertTrue(ScopeUtils.isOuterMostType(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(157);
        assertFalse(ScopeUtils.isOuterMostType(ast3));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        ast1.setParent(parent1);
        assertTrue(ScopeUtils.isLocalVariableDef(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(21);
        DetailAST parent2 = new DetailAST();
        parent2.setType(96);
        ast2.setParent(parent2);
        assertTrue(ScopeUtils.isLocalVariableDef(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(10);
        DetailAST parent3 = new DetailAST();
        parent3.setType(50);
        ast3.setParent(parent3);
        assertFalse(ScopeUtils.isLocalVariableDef(ast3));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(ast1));

        DetailAST ast2 = new DetailAST();
        ast2.setType(21);
        assertFalse(ScopeUtils.isClassFieldDef(ast2));

        DetailAST ast3 = new DetailAST();
        ast3.setType(10);
        DetailAST parent3 = new DetailAST();
        parent3.setType(7);
        ast3.setParent(parent3);
        assertFalse(ScopeUtils.isClassFieldDef(ast3));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast1 = new DetailAST();
        DetailAST parent1 = new DetailAST();
        ast1.setParent(parent1);
        parent1.setType(14);
        assertEquals(true, ScopeUtils.isInScope(ast1, Scope.PUBLIC));
        
        DetailAST ast2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        ast2.setParent(parent2);
        parent2.setType(15);
        assertEquals(true, ScopeUtils.isInScope(ast2, Scope.PROTECTED));
        
        DetailAST ast3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        ast3.setParent(parent3);
        parent3.setType(157);
        assertEquals(true, ScopeUtils.isInScope(ast3, Scope.PRIVATE));

        DetailAST ast4 = new DetailAST();
        DetailAST parent4 = new DetailAST();
        ast4.setParent(parent4);
        parent4.setType(154);
        assertEquals(false, ScopeUtils.isInScope(ast4, Scope.PUBLIC));
    }
}