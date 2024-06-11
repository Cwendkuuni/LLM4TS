package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(false, arrowType.isSubtype(null));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        ArrowType that = new ArrowType(registry, null, null);
        assertEquals(true, arrowType.hasEqualParameters(that, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        ArrowType that = new ArrowType(registry, null, null);
        assertEquals(true, arrowType.checkArrowEquivalenceHelper(that, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(0, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(null, arrowType.getLeastSupertype(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(null, arrowType.getGreatestSubtype(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(null, arrowType.testForEquality(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(null, arrowType.visit(null));
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(arrowType, arrowType.resolveInternal(null, null));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(false, arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertEquals(false, arrowType.hasAnyTemplateInternal());
    }
}