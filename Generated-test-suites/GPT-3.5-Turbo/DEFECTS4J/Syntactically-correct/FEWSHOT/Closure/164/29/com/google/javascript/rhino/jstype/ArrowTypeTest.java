package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(true, arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(true, arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(0, arrowType.hashCode());
    }

    @Test
    public void testGetLeastSupertype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(null, arrowType1.getLeastSupertype(arrowType2));
    }

    @Test
    public void testGetGreatestSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(null, arrowType1.getGreatestSubtype(arrowType2));
    }

    @Test
    public void testTestForEquality() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(null, arrowType1.testForEquality(arrowType2));
    }

    @Test
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(null, arrowType.visit(null));
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType, arrowType.resolveInternal(null, null));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(false, arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals("", arrowType.toStringHelper(false));
    }
}