package com.example; // replace with your test package if necessary

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertNull(stringSimpleType.getSuperClass());
        assertNull(stringSimpleType.getSuperInterfaces());
        assertNull(stringSimpleType.getValueHandler());
        assertNull(stringSimpleType.getTypeHandler());
        assertFalse(stringSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringSimpleType = new SimpleType(stringClass);
        SimpleType copiedStringSimpleType = new SimpleType(originalStringSimpleType);
        assertEquals(originalStringSimpleType, copiedStringSimpleType);
    }

    @Test
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertNull(stringSimpleType.getSuperClass());
        assertNull(stringSimpleType.getSuperInterfaces());
        assertNull(stringSimpleType.getValueHandler());
        assertNull(stringSimpleType.getTypeHandler());
        assertFalse(stringSimpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithMapClass() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType simpleType = SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithCollectionClass() {
        Class<List<String>> collectionClass = List.class;
        SimpleType simpleType = SimpleType.construct(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithArrayClass() {
        Class<int[]> arrayClass = int[].class;
        SimpleType simpleType = SimpleType.construct(arrayClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringBuilderSimpleType = new SimpleType(stringBuilderClass);
        SimpleType narrowedStringBuilderSimpleType = stringSimpleType.narrow(stringBuilderClass);
        assertEquals(stringBuilderSimpleType, narrowedStringBuilderSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        stringSimpleType.narrow(integerClass);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        Object stringHandler = new Object();
        SimpleType stringSimpleTypeWithHandler = stringSimpleType.withTypeHandler(stringHandler);
        assertNotSame(stringSimpleType, stringSimpleTypeWithHandler);
        assertEquals(stringHandler, stringSimpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<List<String>> listClass = List.class;
        Class<String> stringClass = String.class;
        SimpleType listSimpleType = new SimpleType(listClass);
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType listWithStringSimpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), stringSimpleType, null, null, null, false);
        assertEquals("[java.util.List, class com.fasterxml.jackson.core.type.TypeReferenceTo<java.util.List<java.lang.String>>]", listWithStringSimpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertFalse(stringSimpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<List<String>> listClass = List.class;
        SimpleType listSimpleType = new SimpleType(listClass);
        StringBuilder sb = new StringBuilder();
        listSimpleType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<List<List<String>>> listOfListStringClass = List.class;
        SimpleType listOfListStringSimpleType = new SimpleType(listOfListStringClass);
        StringBuilder sb = new StringBuilder();
        listOfListStringSimpleType.getGenericSignature(sb);
        assertEquals("java.util.List<java.util.List<java.lang.String>>", sb.toString());
    }

    
}