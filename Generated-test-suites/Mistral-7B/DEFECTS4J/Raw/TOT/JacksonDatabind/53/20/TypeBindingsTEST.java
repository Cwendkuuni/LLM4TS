package com.example.junit4;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameter;

public class TypeBindingsTest {

    @org.junit.Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("MyTypeVariable"));
    }

    @org.junit.Test
    public void testCreateBindingsForSingleTypeVariable() {
        Class<List<String>> collectionClass = List.class;
        List<JavaType> types = Arrays.asList(String.class);
        TypeBindings bindings = TypeBindings.create(collectionClass, types);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @org.junit.Test
    public void testCreateBindingsForMultipleTypeVariables() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        List<JavaType> types = Arrays.asList(String.class, List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        assertEquals(3, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));
        assertEquals("java.lang.Integer", bindings.getBoundName(2));
        assertEquals(Integer.class, bindings.getBoundType(2));
    }

    @org.junit.Test
    public void testCreateBindingsForClassWithoutTypeParameters() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.create(stringClass, null);
        assertEquals(0, bindings.size());
    }

    @org.junit.Test
    public void testCreateBindingsForClassWithMultipleTypeParametersButOnlyOneProvided() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        JavaType typeArg1 = List.class;
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg1);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0));
    }

    @org.junit.Test
    public void testCreateBindingsForClassWithMultipleTypeParametersButOnlySomeProvided() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        JavaType typeArg1 = List.class;
        JavaType typeArg2 = Integer.class;
        TypeBindings bindings = TypeBindings.create(mapClass, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));
    }

    @org.junit.Test
    public void testCreateBindingsForArrayType() {
        Class<int[]> intArrayClass = int[].class;
        TypeBindings bindings = TypeBindings.create(intArrayClass, null);
        assertEquals(1, bindings
                .size());
        assertEquals("int[]", bindings.getBoundName(0));
        assertEquals(int[].class, bindings.getBoundType(0));
    }

    @org.junit.Test
    public void testFindBoundTypeForExistingTypeVariable() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        List<JavaType> types = Arrays.asList(String.class, List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        assertEquals("java.lang.String", bindings.findBoundType("java.lang.String").getTypeName());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindBoundTypeForNonExistingTypeVariable() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        List<JavaType> types = Arrays.asList(String.class, List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        bindings.findBoundType("java.lang.Integer");
    }

    @org.junit.Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = new TypeBindings(new String[]{"A", "B"},
                new JavaType[]{String.class, Number.class}, null);
        TypeBindings newBindings = bindings.withUnboundVariable("C");
        String[] newNames = newBindings._names;
        assertEquals(3, newNames.length);
        assertEquals("A", newNames[0]);
        assertEquals("B", newNames[1]);
        assertEquals("C", newNames[2]);
    }

    @org.junit.Test
    public void testIsEmpty() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());

        Class<List<String>> collectionClass = List.class;
        List<JavaType> types = Arrays.asList(String.class);
        TypeBindings bindings = TypeBindings.create(collectionClass, types);
        assertFalse(bindings.isEmpty());
    }

    @org.junit.Test
    public void testToString() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        List<JavaType> types = Arrays.asList(String.class, List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        String toString = bindings.toString();
        assertTrue(toString.startsWith("< Map<"));
        assertTrue(toString.endsWith(">"));
        assertTrue(toString.contains("java.lang.String"));
        assertTrue(toString.contains("java.util.List"));
        assertTrue(toString.contains("java.lang.Integer"));
    }

    @org.junit.Test
    public void testHashCodeAndEquals() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        List<JavaType> types1 = Arrays.asList(String.class, List.class, Integer.class);
        List<JavaType> types2 = Arrays.asList(String.class, List.class, Integer.class);
        TypeBindings bindings1 = TypeBindings.create(mapClass, types1);
        TypeBindings bindings2 = TypeBindings.create(mapClass, types2);
        assertEquals(bindings1, bindings2);

        List<JavaType> types3 = Arrays.asList(String.class, List.class, Long.class);
        TypeBindings bindings3 = TypeBindings.create(mapClass, types3);
        assertNotEquals(bindings1, bindings3);
    }
}