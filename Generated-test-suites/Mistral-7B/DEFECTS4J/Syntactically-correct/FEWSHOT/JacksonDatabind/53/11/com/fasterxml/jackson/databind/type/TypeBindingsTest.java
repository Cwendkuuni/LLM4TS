package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(Arrays.asList(new String[0]), bindings.getTypeParameters());
    }

    @Test
    public void testCreate() {
        Class<List<String>> listOfStrings = List.class.getGenericType();
        TypeVariable<String> stringTypeVar = listOfStrings.getTypeParameters()[0];

        TypeBindings bindings = TypeBindings.create(List.class, Arrays.asList(stringTypeVar));
        assertFalse(bindings.isEmpty());
        assertEquals(Arrays.asList(new String[] { "T" }), bindings.getNames());
        assertEquals(Arrays.asList(stringTypeVar), bindings.getTypeParameters());

        assertEquals(stringTypeVar, bindings.findBoundType("T"));
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<List<String>> listOfStrings = List.class.getGenericType();
        TypeVariable<String> stringTypeVar = listOfStrings.getTypeParameters()[0];

        TypeBindings bindings = TypeBindings.create(List.class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(Arrays.asList(new String[0]), bindings.getTypeParameters());
    }

    @Test
    public void testCreateWithWrongNumberOfTypes() {
        Class<List<String>> listOfStrings = List.class.getGenericType();
        TypeVariable<String> stringTypeVar1 = listOfStrings.getTypeParameters()[0];
        TypeVariable<String> stringTypeVar2 = TypeVariable.create("X", Object.class);

        TypeBindings bindings = TypeBindings.create(List.class, Arrays.asList(stringTypeVar1, stringTypeVar2));
        assertFalse(bindings.isEmpty());
        assertEquals(Arrays.asList(new String[] { "T", "X" }), bindings.getNames());
        assertEquals(Arrays.asList(stringTypeVar1, stringTypeVar2), bindings.getTypeParameters());
    }

    @Test
    public void testCreateWithUnboundVariables() {
        Class<List<Map<String, String>>> listOfMaps = List.class.getGenericType();
        TypeVariable<Map<String, String>> mapTypeVar = listOfMaps.getTypeParameters()[0];
        TypeVariable<String> keyTypeVar = mapTypeVar.getTypeParameters()[0];
        TypeVariable<String> valueTypeVar = mapTypeVar.getTypeParameters()[1];

        TypeBindings bindings = TypeBindings.create(List.class, Arrays.asList(mapTypeVar));
        assertFalse(bindings.isEmpty());
        assertEquals(Arrays.asList(new String[] { "T" }), bindings.getNames());
        assertEquals(Arrays.asList(mapTypeVar), bindings.getTypeParameters());

        TypeBindings bindingsWithUnboundKey = bindings.withUnboundVariable("K");
        assertEquals(Arrays.asList(new String[] { "T", "K" }), bindingsWithUnboundKey.getNames());
        assertFalse(bindingsWithUnboundKey.hasUnbound("K"));
        assertFalse(bindingsWithUnboundKey.hasUnbound("T"));

        TypeVariable<String> unboundKey = TypeVariable.create("K", Object.class);
        assertEquals(unboundKey, bindingsWithUnboundKey.findBoundType("K"));
    }

    @Test
    public void testToString() {
        Class<List<Map<String, String>>> listOfMaps = List.class.getGenericType();
        TypeVariable<Map<String, String>> mapTypeVar = listOfMaps.getTypeParameters()[0];
        TypeVariable<String> keyTypeVar = mapTypeVar.getTypeParameters()[0];
        TypeVariable<String> valueTypeVar = mapTypeVar.getTypeParameters()[1];

        TypeBindings bindings = TypeBindings.create(List.class, Arrays.asList(mapTypeVar));
        String expected = "<List<T extends Map<K, V>>>";
        assertEquals(expected, bindings.toString());
    }
}