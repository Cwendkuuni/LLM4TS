package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForClassWithTypeArguments() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class, Integer.class);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForClassWithoutTypeArguments() {
        TypeBindings bindings = TypeBindings.create(AbstractList.class);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawType());
    }

    @Test
    public void testCreateBindingsForClassWithSingleTypeArgument() {
        TypeVariable<?> typeVariable = String.class.getTypeParameters()[0];
        TypeBindings bindings = TypeBindings.create(List.class, typeVariable);
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(typeVariable, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForClassWithTwoTypeArguments() {
        TypeVariable<?> typeVariable1 = String.class.getTypeParameters()[0];
        TypeVariable<?> typeVariable2 = String.class.getTypeParameters()[1];
        TypeBindings bindings = TypeBindings.create(Map.class, typeVariable1, typeVariable2);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(typeVariable1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(typeVariable2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededForClassWithTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, String.class, Integer.class);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getTypeName());
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getTypeName());
    }

    @Test
    public void testCreateIfNeededForClassWithoutTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(AbstractList.class);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        TypeVariable<?> typeVariable = String.class.getTypeParameters()[0];
        TypeBindings bindings = TypeBindings.create(List.class, typeVariable);
        assertEquals(typeVariable, bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testHasUnbound() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class, Integer.class);
        assertFalse(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));

        bindings = bindings.withUnboundVariable("U");
        assertFalse(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("U"));
    }

    @Test
    public void testTypeBindingsToString() {
        TypeVariable<?> typeVariable1 = String.class.getTypeParameters()[0];
        TypeVariable<?> typeVariable2 = String.class.getTypeParameters()[1];
        TypeBindings bindings = TypeBindings.create(Map.class, typeVariable1, typeVariable2);
        String expected = "<Map<java.lang.String, java.lang.String>>";
        assertEquals(expected, bindings.toString());
    }
}