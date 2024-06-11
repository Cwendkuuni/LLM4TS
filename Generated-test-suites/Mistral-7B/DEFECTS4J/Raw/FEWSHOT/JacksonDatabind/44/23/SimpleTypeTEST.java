package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testNarrow() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = String.class;
        SimpleType parentType = SimpleType.construct(parentClass);
        SimpleType childType = SimpleType.construct(childClass);
        SimpleType narrowedType = childType._narrow(parentClass);
        assertEquals(childType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = Integer.class;
        SimpleType parentType = SimpleType.construct(parentClass);
        SimpleType childType = SimpleType.construct(childClass);
        childType._narrow(parentClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object typeHandler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(typeHandler, withTypeHandler._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object contentTypeHandler = new Object() {};
        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(contentTypeHandler);
        assertNull(withContentTypeHandler.getContentType());
        assertEquals(contentTypeHandler, withContentTypeHandler._contentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object valueHandler = new Object() {};
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(simpleType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(valueHandler, withValueHandler._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object contentValueHandler = new Object() {};
        SimpleType withContentValueHandler = simpleType.withContentValueHandler(contentValueHandler);
        assertNull(withContentValueHandler.getContentValueHandler());
        assertEquals(contentValueHandler, withContentValueHandler._contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(simpleType.getRawClass(), withStaticTyping.getRawClass());
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String[].class;
        SimpleType simpleType = SimpleType.construct(rawType);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.construct(Object[].class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;", sb.toString());
    }
}