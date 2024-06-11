package com.google.javascript.rhino.jstype;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Construct ArrowTypes
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        // Test when both return types and parameters are null
        assertTrue(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testHashCode() {
        // Construct ArrowType
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType = new ArrowType(registry, null, null);

        // Test hashCode
        assertEquals(arrowType.hashCode(), 0);
    }

    @Test
    public void testGetLeastSupertype() {
        // Construct ArrowTypes
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        // Test getLeastSupertype
        try {
            arrowType1.getLeastSupertype(arrowType2);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetGreatestSubtype() {
        // Construct ArrowTypes
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        // Test getGreatestSubtype
        try {
            arrowType1.getGreatestSubtype(arrowType2);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testTestForEquality() {
        // Construct ArrowTypes
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        // Test testForEquality
        try {
            arrowType1.testForEquality(arrowType2);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testVisit() {
        // Construct ArrowType
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType = new ArrowType(registry, null, null);

        // Test visit
        try {
            arrowType.visit(null);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testToStringHelper() {
        // Construct ArrowType
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType = new ArrowType(registry, null, null);

        // Test toStringHelper
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        // Construct ArrowType
        JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
        ArrowType arrowType = new ArrowType(registry, null, null);

        // Test hasAnyTemplateInternal
        assertFalse(arrowType.hasAnyTemplateInternal());
    }

}