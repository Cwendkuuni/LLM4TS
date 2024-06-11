package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {
    
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertFalse(arrowType.isSubtype(null));
        assertFalse(arrowType.isSubtype(registry.createUnknownType()));
        assertFalse(arrowType.isSubtype(registry.createArrayType(registry.createObjectType())));
        assertFalse(arrowType.isSubtype(registry.createRecordType()));
        assertFalse(arrowType.isSubtype(registry.createUnionType(registry.createObjectType(), registry.createStringType())));
        assertFalse(arrowType.isSubtype(registry.createArrowType(null, registry.createObjectType())));
        assertTrue(arrowType.isSubtype(arrowType));
    }
    
    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        ArrowType arrowType3 = new ArrowType(registry, null, registry.createObjectType());
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
        assertTrue(arrowType1.hashCode() != arrowType3.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        arrowType1.getLeastSupertype(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        arrowType1.getGreatestSubtype(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        arrowType1.testForEquality(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        arrowType.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        arrowType.resolveInternal(null, null);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, registry.createObjectType());
        assertFalse(arrowType1.hasUnknownParamsOrReturn());
        assertTrue(arrowType2.hasUnknownParamsOrReturn());
    }
}