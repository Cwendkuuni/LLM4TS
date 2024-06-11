package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.Scope;
import antlr.collections.AST;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST detailAST = new DetailAST();
        Scope returnValue = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PACKAGE, returnValue);

        detailAST.addChild(14, new AST());
        returnValue = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PUBLIC, returnValue);

        detailAST.addChild(15, new AST());
        returnValue = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PROTECTED, returnValue);

        detailAST.addChild(157, new AST());
        returnValue = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PRIVATE, returnValue);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST detailAST = new DetailAST();
        Scope returnValue = ScopeUtils.getSurroundingScope(detailAST);
        assertNull(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.addChild(14, new AST());
        Scope modScope = ScopeUtils.getScopeFromMods(parent.findFirstToken(5));

        returnValue = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(modScope, returnValue);

        parent.addChild(157, new AST());
        returnValue = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(Scope.ANONINNER, returnValue);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isInInterfaceBlock(detailAST);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(15);
        returnValue = ScopeUtils.isInInterfaceBlock(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isInAnnotationBlock(detailAST);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(157);
        returnValue = ScopeUtils.isInAnnotationBlock(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(15);
        returnValue = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertTrue(returnValue);

        parent.setParent(null);
        parent.setType(157);
        returnValue = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isInEnumBlock(detailAST);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(154);
        returnValue = ScopeUtils.isInEnumBlock(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isInCodeBlock(detailAST);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(9);
        returnValue = ScopeUtils.isInCodeBlock(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isOuterMostType(detailAST);
        assertTrue(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(14);
        returnValue = ScopeUtils.isOuterMostType(detailAST);
        assertFalse(returnValue);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isLocalVariableDef(detailAST);
        assertFalse(returnValue);

        detailAST.setType(10);
        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(7);
        returnValue = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(returnValue);

        parent.setParent(null);
        parent.setType(35);
        returnValue = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(returnValue);

        parent.setParent(null);
        parent.setType(156);
        returnValue = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(returnValue);

        detailAST.setParent(null);
        detailAST.setType(21);
        parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(96);
        returnValue = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST detailAST = new DetailAST();
        boolean returnValue = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(returnValue);

        detailAST.setType(10);
        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(7);
        returnValue = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(returnValue);

        parent.setParent(null);
        parent.setType(35);
        returnValue = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(returnValue);

        parent.setParent(null);
        parent.setType(156);
        returnValue = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(returnValue);

        detailAST.setType(11);
        returnValue = ScopeUtils.isClassFieldDef(detailAST);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInScope() {
        DetailAST detailAST = new DetailAST();
        Scope scope = Scope.PUBLIC;
        boolean returnValue = ScopeUtils.isInScope(detailAST, scope);
        assertFalse(returnValue);

        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        parent.setType(14);
        Scope modScope = ScopeUtils.getScopeFromMods(parent.findFirstToken(5));
        detailAST.setType(10);
        returnValue = ScopeUtils.isInScope(detailAST, modScope);
        assertTrue(returnValue);
    }
}