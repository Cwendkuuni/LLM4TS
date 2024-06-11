package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeBindingsTest {
    
    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = new TypeBindings(new String[]{"T"}, new JavaType[]{TypeFactory.unknownType()}, null);
        assertNull(bindings.findBoundType("A"));
        assertNotNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateWithOneType() {
        Class<?> erasedType = List.class;
        JavaType typeArg1 = TypeFactory.unknownType();
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.unknownType();
        JavaType typeArg2 = TypeFactory.unknownType();
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
    }

    @Test
    public void testCreateIfNeededNoTypeParameters() {
        Class<?> erasedType = String.class;
        JavaType typeArg1 = TypeFactory.unknownType();
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = new TypeBindings(new String[]{"T"}, new JavaType[]{TypeFactory.unknownType()}, null);
        TypeBindings updatedBindings = bindings.withUnboundVariable("A");
        assertEquals(1, bindings.size());
        assertEquals(2, updatedBindings.size());
    }

}