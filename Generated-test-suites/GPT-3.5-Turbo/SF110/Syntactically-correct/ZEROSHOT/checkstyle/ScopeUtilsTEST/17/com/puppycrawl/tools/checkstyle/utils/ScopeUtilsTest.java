package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = null;
        Scope expected = Scope.PACKAGE;
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = null;
        Scope expected = null;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = null;
        boolean expected = true;
        boolean result = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = null;
        boolean expected = false;
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = null;
        Scope scope = Scope.PACKAGE;
        boolean expected = false;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, result);
    }
}