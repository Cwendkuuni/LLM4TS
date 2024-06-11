package com.google.javascript.jscomp.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {
    @Test
    public void testGetDef() {
        // Test with a local variable
        Node defNode = MustBeReachingVariableDef.getDef("x", useNode);
        assertEquals("N1", defNode.getString());

        // Test with a global variable
        Node defNode2 = MustBeReachingVariableDef.getDef("y", useNode);
        assertNull(defNode2);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        // Test with a local variable that depends on outer scope variables
        boolean depends = MustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode);
        assertTrue(depends);

        // Test with a local variable that does not depend on outer scope variables
        boolean depends2 = MustBeReachingVariableDef.dependsOnOuterScopeVars("y", useNode);
        assertFalse(depends2);
    }
}