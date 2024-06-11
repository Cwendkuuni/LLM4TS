package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype_sameArrowType() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isSubtype(arrowType1));
    }

    @Test
    public void testIsSubtype_arrowTypeWithDifferentReturnType() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        arrowType2.returnType = new JSType(null);

        assertTrue(!arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_arrowTypeWithDifferentParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        arrowType2.parameters = new Node(null);

        assertTrue(!arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);

        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType.getPossibleToBooleanOutcomes(), BooleanLiteralSet.TRUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.resolveInternal(null, null);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType.hasUnknownParamsOrReturn(), false);
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType.toStringHelper(false), "[ArrowType]");
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType.hasAnyTemplateInternal(), false);
    }
}