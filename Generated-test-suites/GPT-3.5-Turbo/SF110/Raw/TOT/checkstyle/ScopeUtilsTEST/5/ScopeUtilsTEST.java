
package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    // Testing getScopeFromMods method
    @Test
    public void testGetScopeFromMods() {
        // Typical case: public scope
        DetailAST aMods1 = new DetailAST();
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        aMods1.addChild(publicToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods1));

        // Typical case: protected scope
        DetailAST aMods2 = new DetailAST();
        DetailAST protectedToken = new DetailAST();
        protectedToken.setText("protected");
        aMods2.addChild(protectedToken);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods2));

        // Typical case: private scope
        DetailAST aMods3 = new DetailAST();
        DetailAST privateToken = new DetailAST();
        privateToken.setText("private");
        aMods3.addChild(privateToken);
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods3));

        // Edge case: no scope specified
        DetailAST aMods4 = new DetailAST();
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods4));

        // Error case: incorrect scope specified
        DetailAST aMods5 = new DetailAST();
        DetailAST incorrectToken = new DetailAST();
        incorrectToken.setText("incorrect");
        aMods5.addChild(incorrectToken);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods5));
    }

    // Testing getSurroundingScope method
    @Test
    public void testGetSurroundingScope() {
        // Typical case: class scope
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(15);
        node1.setParent(parentNode1);
        assertEquals(ScopeUtils.getScopeFromMods(node1), ScopeUtils.getSurroundingScope(node1));

        // Typical case: interface scope
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertEquals(ScopeUtils.getScopeFromMods(node2), ScopeUtils.getSurroundingScope(node2));

        // Typical case: enumeration scope
        DetailAST node3 = new DetailAST();
        DetailAST parentNode3 = new DetailAST();
        parentNode3.setType(154);
        node3.setParent(parentNode3);
        assertEquals(ScopeUtils.getScopeFromMods(node3), ScopeUtils.getSurroundingScope(node3));

        // Typical case: annotation scope
        DetailAST node4 = new DetailAST();
        DetailAST parentNode4 = new DetailAST();
        parentNode4.setType(157);
        node4.setParent(parentNode4);
        assertEquals(ScopeUtils.getScopeFromMods(node4), ScopeUtils.getSurroundingScope(node4));

        // Typical case: anonymous inner class scope
        DetailAST node5 = new DetailAST();
        DetailAST parentNode5 = new DetailAST();
        parentNode5.setType(136);
        node5.setParent(parentNode5);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node5));

        // Edge case: no surrounding scope
        DetailAST node6 = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(node6));
    }

    // Testing isInInterfaceBlock method
    @Test
    public void testIsInInterfaceBlock() {
        // Typical case: node is in interface block
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(15);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInInterfaceBlock(node1));

        // Edge case: node is not in interface block
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isInInterfaceBlock(node2));
    }

    // Testing isInAnnotationBlock method
    @Test
    public void testIsInAnnotationBlock() {
        // Typical case: node is in annotation block
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(157);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInAnnotationBlock(node1));

        // Edge case: node is not in annotation block
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isInAnnotationBlock(node2));
    }

    // Testing isInInterfaceOrAnnotationBlock method
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical case: node is in interface block
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(15);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node1));

        // Typical case: node is in annotation block
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(157);
        node2.setParent(parentNode2);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node2));

        // Edge case: node is not in interface or annotation block
        DetailAST node3 = new DetailAST();
        DetailAST parentNode3 = new DetailAST();
        parentNode3.setType(14);
        node3.setParent(parentNode3);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node3));
    }

    // Testing isInEnumBlock method
    @Test
    public void testIsInEnumBlock() {
        // Typical case: node is in enumeration block
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(154);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInEnumBlock(node1));

        // Edge case: node is not in enumeration block
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isInEnumBlock(node2));
    }

    // Testing isInCodeBlock method
    @Test
    public void testIsInCodeBlock() {
        // Typical case: node is in code block
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(9);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInCodeBlock(node1));

        // Edge case: node is not in code block
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isInCodeBlock(node2));
    }

    // Testing isOuterMostType method
    @Test
    public void testIsOuterMostType() {
        // Typical case: node is outermost type
        DetailAST node1 = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node1));

        // Typical case: node is not outermost type
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isOuterMostType(node2));
    }

    // Testing isLocalVariableDef method
    @Test
    public void testIsLocalVariableDef() {
        // Typical cases: local variable definitions
        DetailAST node1 = new DetailAST();
        node1.setType(10);
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(7);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isLocalVariableDef(node1));

        DetailAST node2 = new DetailAST();
        node2.setType(10);
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(35);
        node2.setParent(parentNode2);
        assertTrue(ScopeUtils.isLocalVariableDef(node2));

        DetailAST node3 = new DetailAST();
        node3.setType(10);
        DetailAST parentNode3 = new DetailAST();
        parentNode3.setType(156);
        node3.setParent(parentNode3);
        assertTrue(ScopeUtils.isLocalVariableDef(node3));

        DetailAST node4 = new DetailAST();
        node4.setType(21);
        DetailAST parentNode4 = new DetailAST();
        parentNode4.setType(96);
        node4.setParent(parentNode4);
        assertTrue(ScopeUtils.isLocalVariableDef(node4));

        // Edge case: not a local variable definition
        DetailAST node5 = new DetailAST();
        node5.setType(10);
        DetailAST parentNode5 = new DetailAST();
        parentNode5.setType(41);
        node5.setParent(parentNode5);
        assertFalse(ScopeUtils.isLocalVariableDef(node5));
    }

    // Testing isClassFieldDef method
    @Test
    public void testIsClassFieldDef() {
        // Typical case: class field definition
        DetailAST node1 = new DetailAST();
        node1.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node1));

        // Edge case: not a class field definition
        DetailAST node2 = new DetailAST();
        node2.setType(11);
        assertTrue(ScopeUtils.isClassFieldDef(node2));
    }

    // Testing isInScope method
    @Test
    public void testIsInScope() {
        // Typical case: node is in given scope
        DetailAST node1 = new DetailAST();
        DetailAST parentNode1 = new DetailAST();
        parentNode1.setType(15);
        node1.setParent(parentNode1);
        assertTrue(ScopeUtils.isInScope(node1, ScopeUtils.getScopeFromMods(node1)));

        // Edge case: node is not in given scope
        DetailAST node2 = new DetailAST();
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(14);
        node2.setParent(parentNode2);
        assertFalse(ScopeUtils.isInScope(node2, ScopeUtils.getScopeFromMods(node2)));
    }

}

