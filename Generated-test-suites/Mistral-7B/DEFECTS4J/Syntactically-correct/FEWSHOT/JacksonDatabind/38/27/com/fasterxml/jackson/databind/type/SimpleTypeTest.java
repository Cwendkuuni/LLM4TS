package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

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
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.TYPE);
        assertNotNull(simpleType);
        assertEquals(Integer.TYPE, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalid() {
        SimpleType.construct(List.class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        // no content type, so:
        assertNull(simpleType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType withValueHandler = simpleType.withValueHandler(null);
        assertNotNull(withValueHandler);
        assertEquals(String.class, withValueHandler.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        SimpleType.construct(Object.class);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String[].class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.TYPE);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(String[].class);
        assertFalse(simpleType1.equals(simpleType4));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        assertEquals("java.util.List", simpleType.getCanonicalName());

        SimpleType simpleType2 = new SimpleType(ArrayList.class);
        assertEquals("java.util.ArrayList", simpleType2.getCanonicalName());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
}