package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForOneTypeVariable() {
        Class<?> erasedType = List.class;
        JavaType typeArg = TypeBindings.handleResolvedType(List.class, 0);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertSame(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForTwoTypeVariables() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = TypeBindings.handleResolvedType(Map.class, 0);
        JavaType typeArg2 = TypeBindings.handleResolvedType(Map.class, 1);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(typeArg1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(typeArg2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForCollection() {
        Class<?> erasedType = ArrayList.class;
        JavaType[] types = new JavaType[0];
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForMap() {
        Class<?> erasedType = HashMap.class;
        JavaType[] types = new JavaType[] { TypeBindings.handleResolvedType(Map.class, 0), TypeBindings.handleResolvedType(Map.class, 1) };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(TypeBindings.handleResolvedType(Map.class, 0), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(TypeBindings.handleResolvedType(Map.class, 1), bindings.getBoundType(1));
    }

    @Test
    public void testHasUnboundVariable() {
        Class<?> erasedType = HashMap.class;
        JavaType[] types = new JavaType[] { TypeBindings.handleResolvedType(Map.class, 0), TypeBindings.handleResolvedType(Map.class, 1) };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = HashMap.class;
        JavaType[] types = new JavaType[] { TypeBindings.handleResolvedType(Map.class, 0), TypeBindings.handleResolvedType(Map.class, 1) };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(String.class, bindings.findBoundType("K").getType().getRawType());
        assertEquals(Integer.class, bindings.findBoundType("V").getType().getRawType());
    }

    @Test
    public void testWithUnboundVariable() {
        String testClassName = "com.test.MyClass";
        Class<?> erasedType = Type.getRawType(Type.getType(testClassName));
        JavaType[] types = new JavaType[] { TypeBindings.handleResolvedType(List.class, 0), TypeBindings.handleResolvedType(Map.class, 0) };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        String unboundVariable = "T";
        TypeBindings newBindings = bindings.withUnboundVariable(unboundVariable);
        assertArrayEquals(new String[] { "E", unboundVariable, "K", "V" }, newBindings._names);
        assertArrayEquals(new JavaType[] { TypeBindings.handleResolvedType(List.class, 0), TypeBindings.handleResolvedType(Map.class, 0), null, null }, newBindings._types);
        assertEquals(4, newBindings.size());
        assertNull(newBindings.findBoundType(unboundVariable));
    }

}