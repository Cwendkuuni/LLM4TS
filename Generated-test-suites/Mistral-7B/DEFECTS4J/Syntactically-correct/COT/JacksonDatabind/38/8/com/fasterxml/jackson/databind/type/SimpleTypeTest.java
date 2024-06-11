package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("java.lang.String[]");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testRefineNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNarrowInvalidSubclass() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withNarrow(Integer.class);
    }

    @Test
    public void testWithContentTypeHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType.withValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType._asStatic);
    }
}