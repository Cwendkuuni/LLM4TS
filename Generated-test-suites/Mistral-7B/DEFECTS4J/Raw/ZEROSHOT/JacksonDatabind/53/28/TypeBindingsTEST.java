package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
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
    }

    @Test
    public void testCreate() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, listType.getTypeArguments());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(listType.getTypeArguments()[0], bindings.getBoundType(0));

        bindings = TypeBindings.create(Map.class, mapType.getTypeArguments());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(mapType.getTypeArguments()[0], bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(mapType.getTypeArguments()[1], bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);

        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, listType.getTypeArguments());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(listType.getTypeArguments()[0], bindings.getBoundType(0));

        bindings = TypeBindings.createIfNeeded(Map.class, mapType.getTypeArguments());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(mapType.getTypeArguments()[0], bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(mapType.getTypeArguments()[1], bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, listType.getTypeArguments());
        assertEquals("java.lang.Integer", bindings.findBoundType("java.lang.Integer").getName());
        assertNull(bindings.findBoundType("java.lang.String"));

        bindings = TypeBindings.create(Map.class, mapType.getTypeArguments());
        assertEquals("java.lang.String", bindings.findBoundType("java.lang.String").getName());
        assertEquals("java.lang.Integer", bindings.findBoundType("java.lang.Integer").getName());
        assertNull(bindings.findBoundType("java.util.Map"));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, listType.getTypeArguments());
        TypeBindings newBindings = bindings.withUnboundVariable("MyTypeVariable");
        assertTrue(Arrays.asList(newBindings._unboundVariables).contains("MyTypeVariable"));
    }
}