package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
 
import org.junit.Test;
 
public class ArrowTypeTest {
 
    @Test
    public void testIsSubtype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.isSubtype(type2));
    }
 
    @Test
    public void testHasEqualParameters() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.hasEqualParameters(type2, false));
    }
 
    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(type1.checkArrowEquivalenceHelper(type2, false));
    }
 
    @Test
    public void testGetLeastSupertype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertNull(type1.getLeastSupertype(null));
    }
 
    @Test
    public void testGetGreatestSubtype() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertNull(type1.getGreatestSubtype(null));
    }
 
    @Test
    public void testTestForEquality() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertEquals(JSType.TernaryValue.FALSE, type1.testForEquality(null));
    }
 
    @Test
    public void testVisit() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertNotNull(type1.visit(null));
    }
 
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertEquals(BooleanLiteralSet.TRUE, type1.getPossibleToBooleanOutcomes());
    }
 
    @Test
    public void testResolveInternal() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertNotNull(type1.resolveInternal(null, null));
    }
 
    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertFalse(type1.hasUnknownParamsOrReturn());
    }
 
    @Test
    public void testToStringHelper() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertNotNull(type1.toStringHelper(false));
    }
 
    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertFalse(type1.hasAnyTemplateInternal());
    }
 
    @Test
    public void testHashCode() {
        ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertEquals(type1.hashCode(), type2.hashCode());
    }
}