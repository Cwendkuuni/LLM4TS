package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType simpleTypeConstruct = SimpleType.construct(cls);

        assertEquals(cls, simpleType.getRawClass());
        assertEquals(simpleType, simpleTypeConstruct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidClassConstruction() {
        Class<?> cls = Map.class;
        SimpleType simpleType = new SimpleType(cls);
    }

    @Test
    public void testNarrowing() {
        Class<?> stringClass = String.class;
        Class<?> stringBuilderClass = StringBuffer.class;

        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);

        JavaType narrowed = stringType.narrow(stringBuilderClass);
        assertEquals(stringBuilderType, narrowed);

        // Should throw an IllegalArgumentException as _class is not a subclass of StringBuffer
        JavaType nullNarrowed = stringType.narrow(int.class);
        assertNull(nullNarrowed);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.constructType(int[].class);
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentType(contentType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class);

        JavaType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.util.ArrayList<java.lang.Object>";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        String expected = "[simple type, class java.util.ArrayList]";
        String actual = simpleType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> stringClass = String.class;
        Class<?> stringBuilderClass = StringBuffer.class;

        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);
        SimpleType stringType2 = new SimpleType(stringClass);

        assertTrue(stringType.equals(stringType));
        assertTrue(stringType.equals(stringType2));
        assertFalse(stringType.equals(stringBuilderType));
    }

    @Test
    public void testRefine() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        JavaType refinedType = simpleType.refine(String[].class, null, null, null);
        assertNull(refinedType);
    }
}