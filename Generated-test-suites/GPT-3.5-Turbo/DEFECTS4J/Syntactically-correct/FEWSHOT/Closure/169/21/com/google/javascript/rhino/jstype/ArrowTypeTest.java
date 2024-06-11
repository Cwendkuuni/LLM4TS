package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        ArrowType type3 = new ArrowType(null, null, null);

        // Test cases for isSubtype()
        assertTrue(type1.isSubtype(type2));
        assertFalse(type1.isSubtype(type3));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        ArrowType type3 = new ArrowType(null, null, null);

        // Test cases for hasEqualParameters()
        assertTrue(type1.hasEqualParameters(type2, true));
        assertFalse(type1.hasEqualParameters(type3, true));
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);
        ArrowType type3 = new ArrowType(null, null, null);

        // Test cases for checkArrowEquivalenceHelper()
        assertTrue(type1.checkArrowEquivalenceHelper(type2, true));
        assertFalse(type1.checkArrowEquivalenceHelper(type3, true));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for getLeastSupertype()
        type.getLeastSupertype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for getGreatestSubtype()
        type.getGreatestSubtype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for testForEquality()
        type.testForEquality(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for visit()
        type.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for getPossibleToBooleanOutcomes()
        assertEquals(BooleanLiteralSet.TRUE, type.getPossibleToBooleanOutcomes());
    }
    
    @Test
    public void testResolveInternal() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for resolveInternal()
        assertSame(type, type.resolveInternal(null, null));
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for hasUnknownParamsOrReturn()
        assertFalse(type.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType type = new ArrowType(null, null, null);

        // Test case for hasAnyTemplateInternal()
        assertFalse(type.hasAnyTemplateInternal());
    }
}