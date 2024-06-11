package com.example.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JavaType.GenericType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParameterHandler;
import com.fasterxml.jackson.databind.type.TypeUtil;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThrowsExceptionWithInvalidNumberOfTypes() {
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { JavaType.constructGenericType(String.class, Integer.class), JavaType.constructGenericType(Integer.class) });
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateWithOneType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        assertEquals(1, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(Map.class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("key"));
        assertNull(bindings.findBoundType("value"));
    }

    @Test
    public void testCreateWithTwoTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        assertEquals(2, bindings.size());
        assertEquals("String[]", bindings.getBoundName(0));
        assertEquals(String[].class, bindings.getBoundType(0));
        assertEquals("Integer[]", bindings.getBoundName(1));
        assertEquals(Integer[].class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("unnamedType"));
    }

    @Test
    public void testCreateIfNeededWithNoTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(javaType.getRawClass(), JavaType.constructGenericType(String[].class));
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(javaType.getRawClass(), JavaType.constructGenericType(String.class));
        assertEquals(1, bindings.size());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("key"));
        assertNull(bindings.findBoundType("value"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.createIfNeeded(javaType.getRawClass(), null);
        assertEquals(2, bindings.size());
        assertEquals("String[]", bindings.getBoundName(0));
        assertEquals(String[].class, bindings.getBoundType(0));
        assertEquals("Integer[]", bindings.getBoundName(1));
        assertEquals(Integer[].class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("unnamedType"));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("unnamedType");
        assertEquals(3, bindingsWithUnboundVariable.size());
        assertEquals("String[]", bindingsWithUnboundVariable.getBoundName(0));
        assertEquals(String[].class, bindingsWithUnboundVariable.getBoundType(0));
        assertEquals("Integer[]", bindingsWithUnboundVariable.getBoundName(1));
        assertEquals(Integer[].class, bindingsWithUnboundVariable.getBoundType(1));
        assertEquals("unnamedType", bindingsWithUnboundVariable.getBoundName(2));
        assertNull(bindingsWithUnboundVariable.findBoundType("key"));
        assertNull(bindingsWithUnboundVariable.findBoundType("value"));
        assertNull(bindingsWithUnboundVariable.findBoundType("unnamedType2"));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        assertEquals("String", bindings.findBoundType("key").getTypeName());
        assertEquals("Integer", bindings.findBoundType("value").getTypeName());
        assertNull(bindings.findBoundType("unnamedType"));
    }

    @Test
    public void testHasUnbound() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        assertFalse(bindings.hasUnbound("key"));
        assertFalse(bindings.hasUnbound("value"));
        assertFalse(bindings.hasUnbound("unnamedType"));

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("unnamedType");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("unnamedType"));
        assertFalse(bindingsWithUnboundVariable.hasUnbound("key"));
        assertFalse(bindingsWithUnboundVariable.hasUnbound("value"));
    }

    @Test
    public void testEqualsAndHashCode() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String[].class, Integer[].class);
        TypeBindings bindings1 = TypeBindings.create(javaType.getRawClass(), javaType);
        TypeBindings bindings2 = TypeBindings.create(javaType.getRawClass(), javaType);
        assertEquals(bindings1, bindings2);

        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, String[].class, Integer[].class);
        TypeBindings bindings3 = TypeBindings.create(javaType2.getRawClass(), javaType2);
        assertNotEquals(bindings1, bindings3);

        TypeBindings bindings4 = bindings1.withUnboundVariable("unnamedType");
        assertNotEquals(bindings1, bindings4);
        assertEquals(bindings4.hashCode(), bindings4.hashCode());
        assertEquals(bindings1.hashCode(), bindings1.hashCode());
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        String bindingsString = bindings.toString();
        String expected = "<Map<String, Integer>>";
        assertEquals(expected, bindingsString);
    }

    @Test
    public void testGetTypeParameters() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals(String[].class, typeParameters.get(0));
        assertEquals(Integer[].class, typeParameters.get(1));
    }
}