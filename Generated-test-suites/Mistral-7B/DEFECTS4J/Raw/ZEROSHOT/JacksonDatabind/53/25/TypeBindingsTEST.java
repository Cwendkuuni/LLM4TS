package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForSimpleClass() {
        Class<List<Integer>> clazz = List.class;
        TypeVariable<?>[] typeParams = clazz.getTypeParameters();
        JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class) };
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals(1, bindings.size());
        assertFalse(bindings.isEmpty());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("S"));
    }

    @Test
    public void testCreateBindingsForClassWithTwoTypeParams() {
        Class<Map<Integer, List<String>>> clazz = Map.class;
        TypeVariable<?>[] typeParams = clazz.getTypeParameters();
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(Integer.class),
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals(2, bindings.size());
        assertFalse(bindings.isEmpty());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0).getRawClass());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1).getRawClass());
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForCollectionLikeClass() {
        Class<List<List<String>>> clazz = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertEquals(1, bindings.size());
        assertFalse(bindings.isEmpty());
        assertNull(bindings.findBoundType("S"));
    }

    @Test
    public void testCreateBindingsForCollectionLikeClassWithTypeArgs() {
        Class<List<List<String>>> clazz = List.class;
        JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(List.class) };
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, types);
        assertEquals(1, bindings.size());
        assertFalse(bindings.isEmpty());
        assertNull(bindings.findBoundType("S"));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<Integer, List<String>>> clazz = Map.class;
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(Integer.class),
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0).getRawClass());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1).getRawClass());
        assertEquals(Integer.class, bindings.findBoundType("K").getRawClass());
        assertEquals(List.class, bindings.findBoundType("V").getRawClass());
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForClassWithUnboundTypeVariable() {
        Class<List<T>> clazz = new TypeToken<List<T>>() {}.getType();
        JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(String.class) };
        TypeBindings bindings = TypeBindings.create(clazz, types);
        assertEquals(1, bindings.size());
        assertFalse(bindings.isEmpty());
        assertNull(bindings.findBoundType("T"));
        assertTrue(bindings.hasUnbound("T"));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T2");
        assertFalse(bindings.hasUnbound("T2"));
        assertTrue(bindingsWithUnbound.hasUnbound("T"));
        assertTrue(bindingsWithUnbound.hasUnbound("T2"));
    }
}