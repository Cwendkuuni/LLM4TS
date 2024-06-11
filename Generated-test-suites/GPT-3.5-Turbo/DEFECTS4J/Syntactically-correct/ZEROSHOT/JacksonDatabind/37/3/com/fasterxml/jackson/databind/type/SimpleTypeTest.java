package com.fasterxml.jackson.databind.type;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(Object.class);
        SimpleType type = (SimpleType) baseType.narrow(String.class);
        assertEquals(String.class, type.getRawClass());
        assertEquals(baseType.getBindings(), type.getBindings());
        assertEquals(baseType.getSuperClass(), type.getSuperClass());
        assertEquals(baseType.getSuperInterfaces(), type.getSuperInterfaces());
        assertEquals(baseType.getValueHandler(), type.getValueHandler());
        assertEquals(baseType.getTypeHandler(), type.getTypeHandler());
        assertFalse(type.isAsStatic());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNarrow() {
        SimpleType baseType = new SimpleType(Object.class);
        baseType.narrow(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentType(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentTypeHandler(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentValueHandler(Object.class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType baseType = new SimpleType(Object.class);
        SimpleType type = (SimpleType) baseType.withStaticTyping();
        assertEquals(Object.class, type.getRawClass());
        assertEquals(baseType.getBindings(), type.getBindings());
        assertEquals(baseType.getSuperClass(), type.getSuperClass());
        assertEquals(baseType.getSuperInterfaces(), type.getSuperInterfaces());
        assertEquals(baseType.getValueHandler(), type.getValueHandler());
        assertEquals(baseType.getTypeHandler(), type.getTypeHandler());
        assertTrue(type.isAsStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testRefine() {
        SimpleType baseType = new SimpleType(Object.class);
        JavaType type = baseType.refine(String.class, null, null, null);
        assertNull(type);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
        assertEquals("Ljava/lang/String;;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(String.class);
        String result = type.buildCanonicalName();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(String.class);
        boolean result = type.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);
        String result = type.toString();
        assertEquals("[simple type, class java.lang.String]", result);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));

        type1 = new SimpleType(String.class);
        type2 = new SimpleType(Integer.class);
        assertFalse(type1.equals(type2));
        assertFalse(type2.equals(type1));
    }
}