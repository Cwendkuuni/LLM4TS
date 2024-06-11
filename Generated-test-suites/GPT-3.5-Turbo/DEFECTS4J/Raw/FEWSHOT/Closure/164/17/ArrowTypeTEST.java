package com.google.javascript.rhino.jstype;

import org.junit.Test;

import static org.junit.Assert.*;

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
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.hashCode());
    }

    @Test
    public void testGetLeastSupertype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        try {
            arrowType1.getLeastSupertype(arrowType2);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetGreatestSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        try {
            arrowType1.getGreatestSubtype(arrowType2);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testTestForEquality() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        try {
            arrowType1.testForEquality(arrowType2);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        try {
            arrowType.visit(null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.resolveInternal(null, null));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertNotNull(arrowType.toStringHelper(false));
    }
}