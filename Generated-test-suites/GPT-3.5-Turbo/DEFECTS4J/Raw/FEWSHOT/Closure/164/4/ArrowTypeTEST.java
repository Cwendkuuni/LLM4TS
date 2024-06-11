package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(0, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(super.toString(), arrowType.toStringHelper(true));
    }
}