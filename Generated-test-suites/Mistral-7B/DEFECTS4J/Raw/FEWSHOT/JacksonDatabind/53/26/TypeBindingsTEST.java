package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForOneType() {
        Class<List<Integer>> listOfIntClass = new ArrayList<List<Integer>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.create(listOfIntClass, null);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateBindingsForTwoTypes() {
        Class<Map<String, List<Integer>>> mapOfStringToListIntClass = new ArrayList<Map<String, List<Integer>>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.create(mapOfStringToListIntClass, null);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1).getRawClass());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> mapOfStringToListIntClass = new ArrayList<Map<String, List<Integer>>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.create(mapOfStringToListIntClass, null);
        assertEquals("K", bindings.findBoundType("K").getName());
        assertEquals("V", bindings.findBoundType("V").getName());
        assertNull(bindings.findBoundType("nonExistent"));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<Integer>> listOfIntClass = new ArrayList<List<Integer>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfIntClass, null);
        assertTrue(bindings.isEmpty() || bindings.size() == 1);
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());

        listOfIntClass = new ArrayList<List<String>>() {
        }.getClass();
        bindings = TypeBindings.createIfNeeded(listOfIntClass, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithTypes() {
        Class<Map<String, List<Integer>>> mapOfStringToListIntClass = new ArrayList<Map<String, List<Integer>>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(mapOfStringToListIntClass, null);
        assertTrue(bindings.isEmpty() || bindings.size() == 2);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1).getRawClass());

        mapOfStringToListIntClass = new ArrayList<Map<String, List<String>>>() {
        }.getClass();
        bindings = TypeBindings.createIfNeeded(mapOfStringToListIntClass, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<Integer>>> mapOfStringToListIntClass = new ArrayList<Map<String, List<Integer>>>() {
        }.getClass();
        TypeBindings bindings = TypeBindings.create(mapOfStringToListIntClass, null);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("U");
        assertFalse(bindingsWithUnbound.isEmpty());
        assertEquals(3, bindingsWithUnbound.size());
        assertEquals("K", bindingsWithUnbound.getBoundName(0));
        assertEquals(String.class, bindingsWithUnbound.getBoundType(0).getRawClass());
        assertEquals("V", bindingsWithUnbound.getBoundName(1));
        assertEquals(List.class, bindingsWithUnbound.getBoundType(1).getRawClass());
        assertEquals("U", bindingsWithUnbound.getBoundName(2));
    }
}