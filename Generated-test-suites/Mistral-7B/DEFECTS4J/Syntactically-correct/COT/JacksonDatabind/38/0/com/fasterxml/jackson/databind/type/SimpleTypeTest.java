package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for each constructor

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(),
                null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test
    public void testSimpleTypeUnsafeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructMethodNotForApplicationUse() {
        Class<?> cls = String.class;
        SimpleType.construct(cls);
    }

    // Test methods for isContainerType()

    @Test
    public void testIsContainerTypeReturnsFalse() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    // Test methods for getErasedSignature() and getGenericSignature()

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        Type type = new TypeFactory().constructType(new TypeReference<List<String>>() {});
        SimpleType simpleType = new SimpleType(type.getRawType());
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.String>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test methods for equals()

    @Test
    public void testEqualsReturnsTrueForSameInstance() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = simpleType1;
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTypes() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentInstancesOfSameType() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testCannotConstructForMapType() {
        Class<?> cls = HashMap.class;
        new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotConstructForCollectionType() {
        Class<?> cls = ArrayList.class;
        new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotConstructForArrayType() {
        Class<?> cls = String[].class;
        new SimpleType(cls);
    }

    @Test
    public void test_narrowMethodWithSubclass() {
        Class<?> superclass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(superclass);
        SimpleType narrowedType = simpleType.narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithUnrelatedClass() {
        Class<?> superclass = Object.class;
        Class<?> unrelatedClass = Integer.class;
        SimpleType simpleType = new SimpleType(superclass);
        simpleType.narrow(unrelatedClass);
    }
}