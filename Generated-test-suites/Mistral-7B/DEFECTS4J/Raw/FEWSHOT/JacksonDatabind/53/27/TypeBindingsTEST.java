package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsForSingleTypeVariable() {
        JavaType typeArg = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForTwoTypeVariables() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForCollectionType() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Collection.class, elementType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertSame(elementType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMapType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, typeArg1, typeArg2);
        assertEquals(typeArg1, bindings.findBoundType("T"));
        assertEquals(typeArg2, bindings.findBoundType("E"));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, typeArg1, typeArg2);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        assertEquals(3, bindingsWithUnboundVariable.size());
        assertArrayEquals(new String[]{"T", "E", "X"}, bindingsWithUnboundVariable.getTypeParameters().toArray(new String[0]));
    }
}