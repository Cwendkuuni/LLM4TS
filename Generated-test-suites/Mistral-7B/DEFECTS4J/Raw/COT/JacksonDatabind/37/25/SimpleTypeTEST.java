package com.example.junit; // replace with your package name if needed

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("java.lang.String", simpleType.getCanonicalName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructorCall() {
        Class<List<String>> listClass = ArrayList.class;
        new SimpleType(listClass); // should throw an IllegalArgumentException
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowType = simpleType.narrow(StringBuffer.class);
        assertEquals(StringBuffer.class, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNonSubclass() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.narrow(intClass); // should throw an IllegalArgumentException
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertNotSame(typeHandler, simpleType.getTypeHandler());
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object contentTypeHandler = new Object();
        SimpleType newSimpleType = simpleType.withContentTypeHandler(contentTypeHandler);
        assertNull(simpleType.getContentTypeHandler());
        assertEquals(contentTypeHandler, newSimpleType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertNotSame(valueHandler, simpleType.getValueHandler());
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object contentValueHandler = new Object();
        SimpleType newSimpleType = simpleType.withContentValueHandler(contentValueHandler);
        assertNull(simpleType.getContentValueHandler());
        assertEquals(contentValueHandler, newSimpleType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
        SimpleType nonStaticType = simpleType.withStaticTyping().withStaticTyping();
        assertFalse(nonStaticType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        JavaType refinedType = simpleType.refine(StringBuilder.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<Map<String, List<Integer>>>> listMapIntegerClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listMapIntegerClass);
        String canonicalName = simpleType.getCanonicalName();
        String expected = "java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>>";
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<Map<String, List<Integer>>>> listMapIntegerClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listMapIntegerClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String expected = "java.util.List";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<Map<String, List<Integer>>>> listMapIntegerClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listMapIntegerClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expected = "<java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<List<Map<String, List<Integer>>>> listMapIntegerClass1 = ArrayList.class;
        Class<List<Map<String, List<Integer>>>> listMapIntegerClass2 = ArrayList.class;
        SimpleType simpleType1 = new SimpleType(listMapIntegerClass1);
        SimpleType simpleType2 = new SimpleType(listMapIntegerClass2);
        SimpleType simpleType3 = new SimpleType(listMapIntegerClass1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
        SimpleType simpleType4 = new SimpleType(Object.class);
        assertFalse(simpleType1.equals(simpleType4));
        SimpleType simpleType5 = new SimpleType(int[].class);
        assertFalse(simpleType1.equals(simpleType5));
    }

}