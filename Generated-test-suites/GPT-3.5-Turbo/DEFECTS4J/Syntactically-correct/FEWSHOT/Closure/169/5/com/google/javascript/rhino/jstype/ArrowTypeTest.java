package com.google.javascript.rhino.jstype;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.getLeastSupertype(arrowType1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.getGreatestSubtype(arrowType1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.testForEquality(arrowType1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        assertFalse(arrowType1.getPossibleToBooleanOutcomes().isEmpty());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        ArrowType arrowType1 = new ArrowType(registry, parameters, null);
        arrowType1.resolveInternal(null, null);
        assertTrue(arrowType1.hasUnknownParamsOrReturn());
    }

    @Test
    public void hasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        assertFalse(arrowType1.hasAnyTemplateInternal());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        assertEquals("[ArrowType]", arrowType1.toStringHelper(false));
    }

}