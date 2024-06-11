package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateBindingsForClassWithNoTypeParameters() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertFalse(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateBindingsForClassWithOneTypeParameter() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class) };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        String name = bindings.getBoundName(0);
        assertEquals("E", name);
        JavaType type = bindings.getBoundType(0);
        assertEquals(Integer.class, type.getRawType());
    }

    @Test
    public void testCreateBindingsForClassWithTwoTypeParameters() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)
        };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        String name1 = bindings.getBoundName(0);
        assertEquals("K", name1);
        JavaType type1 = bindings.getBoundType(0);
        assertEquals(String.class, type1.getRawType());
        String name2 = bindings.getBoundName(1);
        assertEquals("V", name2);
        JavaType type2 = bindings.getBoundType(1);
        assertEquals(Integer.class, type2.getRawType());
    }

    @Test
    public void testCreateIfNeededWithClassWithOneTypeParameter() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType typeArg = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        String name = bindings.getBoundName(0);
        assertEquals("E", name);
        JavaType type = bindings.getBoundType(0);
        assertEquals(Integer.class, type.getRawType());
    }

    @Test
    public void testCreateIfNeededWithClassWithTwoTypeParameters() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)
        };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        String name1 = bindings.getBoundName(0);
        assertEquals("K", name1);
        JavaType type1 = bindings.getBoundType(0);
        assertEquals(String.class, type1.getRawType());
        String name2 = bindings.getBoundName(1);
        assertEquals("V", name2);
        JavaType type2 = bindings.getBoundType(1);
        assertEquals(Integer.class, type2.getRawType());
    }

    @Test
    public void testCreateIfNeededWithClassWithoutTypeParameters() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testFindBoundType() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)
        };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
        String name1 = "K";
        JavaType type1 = bindings.findBoundType(name1);
        assertEquals(String.class, type1.getRawType());
        String name2 = "V";
        JavaType type2 = bindings.findBoundType(name2);
        assertEquals(Integer.class, type2.getRawType());
        String name3 = "E";
        JavaType type3 = bindings.findBoundType(name3);
        assertNull(type3);
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType erasedType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] types = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)
        };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
        TypeBindings newBindings = bindings.withUnboundVariable("X");
        String name1 = "K";
        JavaType type1 = newBindings.findBoundType(name1);
        assertEquals(String.class, type1.getRawType());
        String name2 = "V";
        JavaType type2 = newBindings.findBoundType(name2);
        assertEquals(Integer.class, type2.getRawType());
        String name3 = "X";
        JavaType type3 = newBindings.findBoundType(name3);
        assertNull(type3);
    }
}