package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for each constructor

    @Test
    public void testSimpleTypeWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.getClassName());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithClassBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertEquals("java.lang.String", simpleType.getClassName());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithClassSuperClass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), new SimpleType(List.class));
        assertEquals("java.lang.String", simpleType.getClassName());
        assertEquals(SimpleType.class, simpleType.getSuperClass().getClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithClassSuperClassInterfaces() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), new SimpleType(List.class),
                new Class[] { Collection.class });
        assertEquals("java.lang.String", simpleType.getClassName());
        assertEquals(SimpleType.class, simpleType.getSuperClass().getClass());
        assertArrayEquals(new Class[] { Collection.class }, simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithTypeBase() {
        TypeBase typeBase = new TypeBase(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType = new SimpleType(typeBase);
        assertEquals("java.lang.String", simpleType.getClassName());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclassNotSubtype() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType subclassType = new SimpleType(Integer.class);
        simpleType.narrow(Integer.class);
    }

    @Test
    public void testNarrowWithSubclass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType subclassType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertEquals(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(new SimpleType(Integer.class));
    }
}