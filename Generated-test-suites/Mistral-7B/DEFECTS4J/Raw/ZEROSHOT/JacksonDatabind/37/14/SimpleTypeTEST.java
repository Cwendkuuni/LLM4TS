package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getType();
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(String.class.getName(), javaType.toString());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType copiedType = new SimpleType(simpleType);
        JavaType javaType = copiedType.getType();
        assertEquals(javaType, simpleType.getType());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType refinedType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(ArrayList.class);
    }

    @Test
    public void testNarrowType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType narrowType = simpleType._narrow(stringBuilderClass);
        JavaType javaType = narrowType.getType();
        assertEquals(StringBuilder.class, javaType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class.getName(), simpleType.buildCanonicalName());

        Class<List<Integer>> listClass = List.class.getGenericType();
        TypeVariable<List> listTypeVariable = (TypeVariable<List>) listClass.getTypeParameters()[0];
        Type<Integer> intType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        bindings.put(listTypeVariable.getName(), intType);
        SimpleType listType = new SimpleType(listClass, bindings, null, null);
        assertEquals("java.util.List<java.lang.Integer>", listType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals(String.class.getName(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listStringClass = List.class.getGenericType();
        TypeVariable<List> listTypeVariable = (TypeVariable<List>) listStringClass.getTypeParameters()[0];
        Type<String> stringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = new TypeBindings();
        bindings.put(listTypeVariable.getName(), stringType);
        SimpleType listType = new SimpleType(listStringClass, bindings, null, null);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        String expected = "<java.util.List<java.lang.String>>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String expected = "[simple type, class java.lang.String]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}