package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        Class<?> clazz = Collection.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(Collection.class, bindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateWithTypeArgs() {
        Class<?> clazz = List.class;
        JavaType[] typeArgs = new JavaType[] { JavaType.constructCollectionType(ArrayList.class, String.class),
                JavaType.constructCollectionType(ArrayList.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(clazz, typeArgs);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawType());
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<?> clazz = Map.class;
        JavaType typeArg = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawType());
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1);
        assertEquals(Integer.class, bindings.findBoundType("V"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArgs() {
        Class<?> clazz = Map.class;
        JavaType[] typeArgs = new JavaType[] { JavaType.constructParametricType(HashMap.class, String.class, Integer.class),
                JavaType.constructParametricType(HashMap.class, String.class),
                JavaType.constructParametricType(HashMap.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(clazz, typeArgs);
    }

    @Test
    public void testCreateIfNeededWithNoTypeArgs() {
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArg() {
        Class<?> clazz = List.class;
        JavaType typeArg = JavaType.constructCollectionType(ArrayList.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
    }

    @Test
    public void testCreateIfNeededWithMultipleTypeArgs() {
        Class<?> clazz = Map.class;
        JavaType[] typeArgs = new JavaType[] { JavaType.constructClassType(String.class),
                JavaType.constructClassType(Integer.class) };
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArgs);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawType());
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("W");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("W"));
    }

    @Test
    public void testToString() {
        Class<?> clazz = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArg1);
        assertEquals("[K, V]", bindings.toString());
    }
}