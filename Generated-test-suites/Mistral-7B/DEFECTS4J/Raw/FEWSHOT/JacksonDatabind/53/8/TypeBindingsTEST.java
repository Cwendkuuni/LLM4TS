package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreate() {
        Class<List<String>> listClass = List.class;
        List<JavaType> javaTypes = Arrays.asList(String.class, String[].class);
        TypeBindings bindings = TypeBindings.create(listClass, javaTypes);
        assertEquals(2, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals("K[]", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals(String[].class, bindings.getBoundType(1));
        assertArrayEquals(new String[]{"E", "K[]"}, bindings.getTypeParameters().toArray(new String[0]));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, String[].class);
        assertTrue(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(0));
        assertEquals(String[].class, bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededNullTypes() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, null);
        assertTrue(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertNull(bindings.getBoundType(0));
        assertNull(bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        String name = "X";
        TypeBindings bindings1 = TypeBindings.createIfNeeded(mapClass, String[].class);
        TypeBindings bindings2 = bindings1.withUnboundVariable(name);
        assertEquals(3, bindings2.size());
        assertEquals(name, bindings2.getBoundName(2));
        assertEquals(List.class, bindings2.getBoundType(0));
        assertEquals(String[].class, bindings2.getBoundType(1));
        assertEquals(null, bindings2.getBoundType(2));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        List<JavaType> javaTypes = Arrays.asList(String.class, String[].class);
        TypeBindings bindings = TypeBindings.create(mapClass, javaTypes);
        assertEquals("K", bindings.findBoundType("K").getName());
        assertEquals("V", bindings.findBoundType("V").getName());
        assertNull(bindings.findBoundType("X"));
    }

    @Test
    public void testHasUnbound() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        List<JavaType> javaTypes = Arrays.asList(String.class, String[].class);
        TypeBindings bindings = TypeBindings.create(mapClass, javaTypes);
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
        assertFalse(bindings.hasUnbound("X"));

        String name = "X";
        TypeBindings bindings2 = bindings.withUnboundVariable(name);
        assertTrue(bindings2.hasUnbound(name));
    }

    @Test
    public void testEquals() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        List<JavaType> javaTypes1 = Arrays.asList(String.class, String[].class);
        List<JavaType> javaTypes2 = Arrays.asList(String.class, String[].class);
        TypeBindings bindings1 = TypeBindings.create(mapClass, javaTypes1);
        TypeBindings bindings2 = TypeBindings.create(mapClass, javaTypes2);
        TypeBindings bindings3 = TypeBindings.create(Map.class, javaTypes1); // different class

        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertNotEquals(bindings1, bindings3);
    }

    @Test
    public void testToString() {
        Class<Map<String, List<String>>> mapClass = Map.class;
        List<JavaType> javaTypes = Arrays.asList(String.class, String[].class);
        TypeBindings bindings = TypeBindings.create(mapClass, javaTypes);
        String expected = "<Map<java.lang.String, java.util.List<java.lang.String>>>";
        assertEquals(expected, bindings.toString());
    }

}