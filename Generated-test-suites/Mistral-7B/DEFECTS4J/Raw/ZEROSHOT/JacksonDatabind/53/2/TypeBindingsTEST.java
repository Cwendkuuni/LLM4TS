package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals("", bindings.toString());
    }

    @Test
    public void testCreateEmptyType() {
        Class<?> clazz = Collection.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals("Collection<>", bindings.toString());
    }

    @Test
    public void testCreateSingleType() {
        Class<?> clazz = List.class;
        JavaType typeArg = new SimpleType(int.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(int.class, bindings.getBoundType(0).getRawClass());
        assertEquals("List<java.lang.Integer>", bindings.toString());
    }

    @Test
    public void testCreateDoubleType() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = new SimpleType(String.class);
        JavaType typeArg2 = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("Map<K,V>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededSingleType() {
        Class<?> clazz = Collection.class;
        JavaType typeArg = new SimpleType(int.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("Collection", bindings.getBoundName(0));
        assertEquals(int.class, bindings.getBoundType(0).getRawClass());
        assertEquals("Collection<java.lang.Integer>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededDoubleType() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = new SimpleType(String.class);
        JavaType typeArg2 = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, new Type[] { typeArg1, typeArg2 });
        assertEquals(2, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("Map<K,V>", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = new SimpleType(String.class);
        JavaType typeArg2 = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1, typeArg2);
        assertEquals(typeArg1, bindings.findBoundType("K"));
        assertEquals(typeArg2, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("XXX"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = new SimpleType(String.class);
        JavaType typeArg2 = new SimpleType(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1, typeArg2);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("Z");
        assertTrue(bindingsWithUnbound.hasUnbound("Z"));
        assertFalse(bindingsWithUnbound.hasUnbound("K"));
        assertFalse(bindingsWithUnbound.hasUnbound("V"));

        JavaType boundType = bindingsWithUnbound.findBoundType("Z");
        assertNull(boundType);
    }
}