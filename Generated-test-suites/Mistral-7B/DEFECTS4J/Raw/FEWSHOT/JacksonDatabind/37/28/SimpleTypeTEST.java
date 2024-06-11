package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertSame(simpleType1, simpleType2);
    }

    @Test
    public void testConstruct() {
        // This test is deprecated and should not be called
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("SimpleType.construct() should not be called by application code");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());

        SimpleType simpleTypeWithBinding = new SimpleType(List.class, null, null);
        assertEquals("java.util.List", simpleTypeWithBinding.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType refinedType = simpleType.refine(String[].class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testNarrow() {
        SimpleType stringSimpleType = new SimpleType(String.class);
        SimpleType stringArraySimpleType = new SimpleType(String[].class);

        SimpleType narrowedType = stringSimpleType._narrow(String[].class);
        assertNull(narrowedType);

        SimpleType narrowedStringArrayType = stringArraySimpleType._narrow(String.class);
        assertSame(stringSimpleType, narrowedStringArrayType);
    }
}