package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithOneType() {
        // Test create method with one type parameter
        Class<?> clazz = String.class;
        JavaType type = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(clazz, type);
        assertNotNull(bindings);
        assertEquals(1, bindings.size());
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypes() {
        // Test create method with two type parameters
        Class<?> clazz = Map.class;
        JavaType type1 = JavaType.construct(String.class);
        JavaType type2 = JavaType.construct(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, type1, type2);
        assertNotNull(bindings);
        assertEquals(2, bindings.size());
        assertEquals(type1, bindings.getBoundType(0));
        assertEquals(type2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededWithOneType() {
        // Test createIfNeeded method with one type parameter
        Class<?> clazz = String.class;
        JavaType type = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, type);
        assertNotNull(bindings);
        assertEquals(1, bindings.size());
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithEmptyClass() {
        // Test createIfNeeded method with a class that has no type parameters
        Class<?> clazz = Object.class;
        JavaType type = JavaType.construct(Object.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, type);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testWithTypeParameters() {
        // Test getTypeParameters method
        Class<?> clazz = List.class;
        JavaType type = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(clazz, type);
        assertNotNull(bindings);
        assertEquals(1, bindings.getTypeParameters().size());
        assertEquals(type, bindings.getTypeParameters().get(0));
    }

    @Test
    public void testUnboundVariable() {
        // Test withUnboundVariable method
        Class<?> clazz = List.class;
        JavaType type = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(clazz, type).withUnboundVariable("T");
        assertNotNull(bindings);
        assertTrue(bindings.hasUnbound("T"));
    }

}