package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertEquals(true, type1.isSubtype(type2));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertEquals(true, type1.isEquivalentTo(type2));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertEquals(0, type.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.getLeastSupertype(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.getGreatestSubtype(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.testForEquality(type2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        type.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertEquals(BooleanLiteralSet.TRUE, type.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = null;
        StaticScope<JSType> scope = null;
        ArrowType type = new ArrowType(registry, null, null);
        assertEquals(type, type.resolveInternal(errorReporter, scope));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertEquals(false, type.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertEquals("ArrowType", type.toStringHelper(false));
    }
}