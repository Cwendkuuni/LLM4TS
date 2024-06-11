package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> baseType = String.class;
        SimpleType baseSimpleType = new SimpleType(baseType);
        Class<?> subType = Integer.class;
        SimpleType subSimpleType = new SimpleType(subType);
        SimpleType narrowedType = baseSimpleType._narrow(subType);
        assertEquals(subType, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithIncompatibleType() {
        Class<?> baseType = String.class;
        SimpleType baseSimpleType = new SimpleType(baseType);
        Class<?> subType = Integer[].class;
        SimpleType subSimpleType = new SimpleType(subType);
        baseSimpleType._narrow(subType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object handler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object handler = new Object() {};
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        TypeBindings bindings = new TypeBindings();
        TypeBindings newBindings = new TypeBindings();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType superClass = new SimpleType(Integer.class);
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testBuildCanonicalNameWithOneTypeVariable() {
        Class<?> cls = List.class;
        TypeVariable<Class<?>> typeVariable = new TypeVariable<Class<?>>() {};
        SimpleType simpleType = new SimpleType(cls, new TypeBindings(), null, null, null, null, false);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.List<T>", canonicalName);
    }

    @Test
    public void testBuildCanonicalNameWithMultipleTypeVariables() {
        Class<?> cls = Map.class;
        TypeVariable<Class<?>> typeVariable1 = new TypeVariable<Class<?>>() {};
        TypeVariable<Class<?>> typeVariable2 = new TypeVariable<Class<?>>() {};
        SimpleType simpleType = new SimpleType(cls, new TypeBindings(), null, null, null, null, false);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.Map<K,V>", canonicalName);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = String.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actual = new StringBuilder(simpleType.toString());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = new StringBuilder(simpleType.getErasedSignature(new StringBuilder()));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = new StringBuilder(simpleType.getGenericSignature(new StringBuilder()));
        assertEquals(expected.toString(), actual.toString());

        Class<?> clsWithTypeVariable = new TypeVariable<Class<?>>() {};
        SimpleType simpleTypeWithTypeVariable = new SimpleType(clsWithTypeVariable);
        StringBuilder expectedWithTypeVariable = new StringBuilder("Ljava/lang/String;T");
        StringBuilder actualWithTypeVariable = new StringBuilder(simpleTypeWithTypeVariable.getGenericSignature(new StringBuilder()));
        assertEquals(expectedWithTypeVariable.toString(), actualWithTypeVariable.toString());
    }

    @Test
    public void testGetErasedSignatureWithArray() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = new StringBuilder(simpleType.getErasedSignature(new StringBuilder()));
        assertEquals(expected.toString(), actual.toString());
    }
}