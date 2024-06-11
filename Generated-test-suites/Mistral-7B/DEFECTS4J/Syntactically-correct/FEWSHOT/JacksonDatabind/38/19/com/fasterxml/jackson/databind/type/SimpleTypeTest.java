package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

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
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[list, java.lang.Object]", sb.toString());
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<String[]> stringArrayClass = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringArrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<Map<String, String>> mapClass = Map.class;
        SimpleType simpleType = SimpleType.constructUnsafe(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = SimpleType.constructUnsafe(listClass);
    }

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<String[]> stringArrayClass = String[].class;
        SimpleType simpleType = SimpleType.construct(stringArrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<Map<String, String>> mapClass = Map.class;
        SimpleType simpleType = SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = SimpleType.construct(listClass);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = new SimpleType(Integer.class);
        assertNull(simpleType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(handler);
        assertNotEquals(simpleType, newSimpleType);
        assertEquals(handler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        assertNull(simpleType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(handler);
        assertNotEquals(simpleType, newSimpleType);
        assertEquals(handler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        Class<List<Integer>> intListClass = List.class;
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(Integer.class, new SimpleType(Integer.class));
        JavaType refinedType = simpleType.refine(intListClass.getComponentType(), bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        assertEquals("[list, java.lang.Object]", simpleType.buildCanonicalName());
    }
}