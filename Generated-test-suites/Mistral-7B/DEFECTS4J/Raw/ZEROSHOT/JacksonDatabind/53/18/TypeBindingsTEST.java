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
    }

    @Test
    public void testCreateBindingsForSingleTypeArgument() {
        JavaType javaType = null;
        try {
            javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(List.class, javaType);
        assertEquals(1, bindings.size());
        String name = bindings.getBoundName(0);
        assertEquals("E", name);
        JavaType boundType = bindings.getBoundType(0);
        assertEquals(Integer.class, boundType.getRawClass());
    }

    @Test
    public void testCreateBindingsForTwoTypeArguments() {
        JavaType javaType1 = null;
        try {
            javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JavaType javaType2 = null;
        try {
            javaType2 = TypeFactory.defaultInstance().constructType(Map.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        assertEquals(2, bindings.size());
        String name1 = bindings.getBoundName(0);
        assertEquals("K", name1);
        JavaType boundType1 = bindings.getBoundType(0);
        assertEquals(Integer.class, boundType1.getRawClass());

        String name2 = bindings.getBoundName(1);
        assertEquals("V", name2);
        JavaType boundType2 = bindings.getBoundType(1);
        assertEquals(String.class, boundType2.getRawClass());
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = null;
        try {
            javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JavaType javaType2 = null;
        try {
            javaType2 = TypeFactory.defaultInstance().constructType(Map.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);

        JavaType foundType1 = bindings.findBoundType("K");
        assertEquals(Integer.class, foundType1);

        JavaType foundType2 = bindings.findBoundType("V");
        assertEquals(String.class, foundType2);

        JavaType foundType3 = bindings.findBoundType("not_exists");
        assertNull(foundType3);
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType javaType1 = null;
        try {
            javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType1);
        assertEquals(1, bindings.size());
        String name = bindings.getBoundName(0);
        assertEquals("E", name);
        JavaType boundType = bindings.getBoundType(0);
        assertEquals(Integer.class, boundType.getRawClass());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, Integer.class);
        TypeBindings newBindings = bindings.withUnboundVariable("new_variable");
        assertNotEquals(bindings, newBindings);
        assertTrue(newBindings.hasUnbound("new_variable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeArguments() {
        JavaType javaType = null;
        try {
            javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(List.class, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeVariables() {
        JavaType javaType = null;
        try {
            javaType = TypeFactory.defaultInstance().constructType(List.class, Integer[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(List.class, javaType);
    }

    @Test
    public void testToString() {
        JavaType javaType1 = null;
        try {
            javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JavaType javaType2 = null;
        try {
            javaType2 = TypeFactory.defaultInstance().constructType(Map.class, Integer.class, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        String toString = bindings.toString();
        List<String> asList = Arrays.asList(toString.substring(1, toString.length()-1).split(","));
        List<String> expectedList = Arrays.asList("<Map<Integer, String>>");
        assertEquals(expectedList, asList);
    }

}