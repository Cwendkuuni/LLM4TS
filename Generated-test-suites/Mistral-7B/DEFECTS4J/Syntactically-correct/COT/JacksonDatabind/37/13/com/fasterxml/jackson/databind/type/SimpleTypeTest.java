package com.example.junit4;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType original = new SimpleType(Integer.class);
        SimpleType copy = new SimpleType(original);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.getValueHandler(), copy.getValueHandler());
        assertEquals(original.getTypeHandler(), copy.getTypeHandler());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithInvalidClass() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorUnsafe() {
        new SimpleType(SimpleType.class);
    }

    @Test
    public void testNarrowMethod() {
        SimpleType parentType = new SimpleType(Number.class);
        SimpleType childType = new SimpleType(Integer.class);
        SimpleType narrowedType = parentType._narrow(Integer.class);
        assertEquals(childType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        SimpleType parentType = new SimpleType(Number.class);
        SimpleType childType = new SimpleType(String.class);
        parentType._narrow(String.class);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.mapTypeVariablesToArguments(Arrays.asList("T"), Arrays.asList(String.class)));
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String[].class);
        String expected = "[simple type, class [Ljava.lang.String;]]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}