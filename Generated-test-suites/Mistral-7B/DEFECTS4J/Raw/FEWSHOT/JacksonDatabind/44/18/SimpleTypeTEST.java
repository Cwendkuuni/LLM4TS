package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidTypes() {
        // Test with invalid types
        Class<?>[] invalidTypes = {Map.class, Collection.class, int[].class};
        for (Class<?> invalidType : invalidTypes) {
            SimpleType.construct(invalidType);
        }
    }

    @Test
    public void testConstructSimpleType() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType baseType = SimpleType.construct(baseClass);
        SimpleType subType = SimpleType.construct(subClass);
        SimpleType narrowedType = baseType._narrow(subClass);
        assertEquals(subType, narrowedType);
    }

    @Test
    public void testNarrowInvalidSubTypes() {
        Class<?> baseClass = Object.class;
        Class<?> subClass1 = int.class;
        Class<?> subClass2 = Double.class;
        SimpleType baseType = SimpleType.construct(baseClass);
        SimpleType subType1 = SimpleType.construct(subClass1);
        SimpleType subType2 = SimpleType.construct(subClass2);
        SimpleType narrowedType1 = baseType._narrow(subClass1);
        SimpleType narrowedType2 = baseType._narrow(subClass2);
        assertNull(narrowedType1);
        assertNull(narrowedType2);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object handler = new Object() {};
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(cls, withHandler.getRawClass());
        assertEquals(handler, withHandler._typeHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String[].class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.construct(Object[].class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType refinedType = SimpleType.construct(rawType).refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = new ArrayList<Integer>().getClass();
        SimpleType simpleType = SimpleType.construct(cls);
        String canonicalName = simpleType.buildCanonicalName();
        String expected = "java.util.ArrayList<java.lang.Integer>";
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = new ArrayList<Integer>().getClass();
        SimpleType simpleType = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String expected = "java.util.List";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = new ArrayList<Integer>().getClass();
        SimpleType simpleType = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expected = "<E extends java.lang.Object, java.lang.Serializable>java.util.List<E>";
        assertEquals(expected, sb.toString());
    }
}