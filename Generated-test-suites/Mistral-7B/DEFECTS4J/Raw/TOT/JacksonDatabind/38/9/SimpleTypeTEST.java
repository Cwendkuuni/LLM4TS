package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        JavaType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        JavaType simpleType = SimpleType.construct(String.class, TypeBindings.mapType(new TypeReference<List<Integer>>() {}));
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(List.class, simpleType.getContentType());
        assertEquals(JavaType.getTypeFactory().constructParametricType(List.class, Integer.class), simpleType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithArrayClass() {
        JavaType simpleType = SimpleType.construct(int[].class);
        fail("Expected IllegalArgumentException but got: " + simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithMapClass() {
        JavaType simpleType = SimpleType.construct(HashMap.class);
        fail("Expected IllegalArgumentException but got: " + simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithCollectionClass() {
        JavaType simpleType = SimpleType.construct(ArrayList.class);
        fail("Expected IllegalArgumentException but got: " + simpleType);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        JavaType simpleType = SimpleType.construct(Integer.class).withStaticTyping();
        assertNotNull(simpleType);
        assertEquals(Integer.TYPE, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        JavaType simpleType = SimpleType.construct(Integer.class).withValueHandler(new ObjectMapper.StdScalarValueSerializer(Integer.class));
        assertNotNull(simpleType);
        assertEquals(Integer.TYPE, simpleType.getRawClass());
    }
}