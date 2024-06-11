package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType type = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType type = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType type = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType type = SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> parent = Object.class;
        Class<?> child = String.class;
        SimpleType parentType = SimpleType.construct(parent);
        SimpleType childType = SimpleType.construct(child);
        SimpleType result = childType._narrow(parentType.getRawClass());
        assertEquals(childType, result);
    }

    @Test
    public void testNarrowWithSameType() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        SimpleType result = type._narrow(cls);
        assertEquals(type, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNonRelatedType() {
        Class<?> parent = Object.class;
        Class<?> child = Integer.class;
        SimpleType parentType = SimpleType.construct(parent);
        SimpleType childType = SimpleType.construct(child);
        childType._narrow(parentType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType type = SimpleType.construct(cls);
        JavaType result = type.withTypeHandler(handler);
        assertEquals(type.getRawClass(), result.getRawClass());
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType type = SimpleType.construct(cls);
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type.getRawClass(), result.getRawClass());
        assertEquals(handler, result.getContentTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerForSimpleType() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        type.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType type = SimpleType.construct(cls);
        JavaType result = type.withValueHandler(handler);
        assertEquals(type.getRawClass(), result.getRawClass());
        assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        JavaType result = type.withStaticTyping();
        assertEquals(type.getRawClass(), result.getRawClass());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        Class<?> refined = Integer.class;
        SimpleType type = SimpleType.construct(cls);
        JavaType result = type.refine(refined, null, null, null);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertEquals("java.lang.String", type.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = String.class;
        SimpleType type1 = SimpleType.construct(cls1);
        SimpleType type2 = SimpleType.construct(cls2);
        SimpleType type3 = SimpleType.construct(cls1);
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type3));
        assertTrue(type1.equals(type2));
    }
}