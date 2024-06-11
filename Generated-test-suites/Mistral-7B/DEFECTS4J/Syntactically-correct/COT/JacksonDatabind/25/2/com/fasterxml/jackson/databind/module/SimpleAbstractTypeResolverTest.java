package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for SimpleAbstractTypeResolver
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
    }

    // Test for addMapping method

    @Test
    public void testAddMapping() {
        resolver.addMapping(List.class, ArrayList.class);
        List<Integer> inputList = Arrays.asList(1, 2, 3);
        JavaType javaType = TypeFactory.defaultInstance().constructType(inputList.getClass());
        JavaType expectedType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelfClassMapping() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSuperClassSubclassReversed() {
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingAbstractClassNonAbstractSubclass() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    // Test for findTypeMapping method

    @Test
    public void testFindTypeMapping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMappingAbstractType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMappingNonAbstractType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNull(actualType);
    }

    // Test for resolveAbstractType method

    @Test
    public void testResolveAbstractType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(), javaType);
        assertNull(actualType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveAbstractTypeAbstractTypeWithoutMapping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(HashMap.class);
        resolver.resolveAbstractType(new DeserializationConfig(), javaType);
    }
}