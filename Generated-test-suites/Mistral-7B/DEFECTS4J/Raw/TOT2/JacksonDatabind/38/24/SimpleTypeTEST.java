package com.fasterxml.jackson.databind.type; // package import

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory; // for TypeFactory

public class SimpleTypeTest { // class name

    @Test
    public void testSimpleTypeConstructor() { // method name
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls); // test construction
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings());
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() { // edge case
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls); // test method call with Map class
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() { // edge case
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls); // test method call with Collection class
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() { // edge case
        Class<?> cls = int[].class;
        SimpleType.construct(cls); // test method call with array class
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType base = new SimpleType(String.class);
        SimpleType copy = new SimpleType(base);
        assertEquals(base.getRawClass(), copy.getRawClass());
        assertEquals(base.getBindings(), copy.getBindings());
        assertEquals(base.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(base.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(base.isContainerType(), copy.isContainerType());
    }

    @Test
    public void testNarrowMethod() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType narrowedType = simpleType.narrow(Integer.class);
        assertEquals(simpleType, narrowedType);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNarrowMethodWithDifferentClass() { // edge case
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
    }

}