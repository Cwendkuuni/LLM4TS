package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertEquals(0, emptyBindings.size());
        assertNull(emptyBindings.findBoundType("Test"));
    }

    @Test
    public void testCreate() {
        Class<List<String>> listOfStringClass = java.util.List.class;
        JavaType listOfStringType = TypeFactory.defaultInstance().constructType(listOfStringClass);

        TypeBindings bindings = TypeBindings.create(listOfStringClass, listOfStringType);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(listOfStringType, bindings.getBoundType(0));

        assertEquals(listOfStringType, bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("Test"));
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<List<String>> listOfStringClass = java.util.List.class;

        TypeBindings bindings = TypeBindings.create(listOfStringClass, null);
        assertEquals(0, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypes() {
        Class<List<String>> listOfStringClass = java.util.List.class;
        JavaType listOfStringType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType listOfStringType2 = TypeFactory.defaultInstance().constructType(String.class);

        TypeBindings.create(listOfStringClass, listOfStringType1);

        try {
            TypeBindings.create(listOfStringClass, listOfStringType1, listOfStringType2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateWithUnmatchedTypes() {
        Class<List<String>> listOfStringClass = java.util.List.class;
        JavaType listOfIntegerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType listOfStringType = TypeFactory.defaultInstance().constructType(String.class);

        try {
            TypeBindings.create(listOfStringClass, listOfIntegerType, listOfStringType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        Class<Map<String, Integer>> mapStringToIntegerClass = java.util.Map.class;
        JavaType mapStringToIntegerType = TypeFactory.defaultInstance().constructType(mapStringToIntegerClass, String.class, Integer.class);

        TypeBindings bindings = TypeBindings.create(mapStringToIntegerClass, mapStringToIntegerType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));

        assertEquals(mapStringToIntegerType, bindings.findBoundType("java.util.Map<java.lang.String, java.lang.Integer>"));
        assertNull(bindings.findBoundType("Test"));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<Map<String, List<Integer>>> mapStringToListOfIntegerClass = java.util.Map.class;
        JavaType mapStringToListOfIntegerType = TypeFactory.defaultInstance().constructType(mapStringToListOfIntegerClass, String.class, java.util.List.class, Integer.class);

        TypeBindings bindings = TypeBindings.create(mapStringToListOfIntegerClass, mapStringToListOfIntegerType);
        assertEquals(3, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(java.util.List.class, bindings.getBoundType(1));
        assertEquals("java.lang.Integer", bindings.getBoundName(2));
        assertEquals(Integer.class, bindings.getBoundType(2));

        assertEquals(mapStringToListOfIntegerType, bindings.findBoundType("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        assertNull(bindings.findBoundType("Test"));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<Integer>> listOfIntegerClass = java.util.List.class;

        TypeBindings bindings = TypeBindings.createIfNeeded(listOfIntegerClass, null);
        assertEquals(0, bindings.size());

        JavaType listOfIntegerType = TypeFactory.defaultInstance().constructType(listOfIntegerClass);
        bindings = TypeBindings.createIfNeeded(listOfIntegerClass, listOfIntegerType);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(listOfIntegerType, bindings.getBoundType(0));

        assertEquals(listOfIntegerType, bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("Test"));
    }

    @Test
    public void testCreateIfNeededWithTypes() {
        Class<Map<String, List<Integer>>> mapStringToListOfIntegerClass = java.util.Map.class;

        TypeBindings bindings = TypeBindings.createIfNeeded(mapStringToListOfIntegerClass, null);
        assertEquals(0, bindings.size());

        JavaType mapStringToListOfIntegerType = TypeFactory.defaultInstance().constructType(mapStringToListOfIntegerClass, String.class, java.util.List.class, Integer.class);
        bindings = TypeBindings.createIfNeeded(mapStringToListOfIntegerClass, mapStringToListOfIntegerType);
        assertEquals(3, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(java.util.List.class, bindings.getBoundType(1));
        assertEquals("java.lang.Integer", bindings.getBoundName(2));
        assertEquals(Integer.class, bindings.getBoundType(2));

        assertEquals(mapStringToListOfIntegerType, bindings.findBoundType("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        assertNull(bindings.findBoundType("Test"));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("Test");

        assertEquals("Test", bindingsWithUnboundVariable.getBoundName(bindingsWithUnboundVariable.size() - 1));
        assertNull(bindingsWithUnboundVariable.findBoundType("Test"));
    }
}