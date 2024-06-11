package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for SimpleAbstractTypeResolver.
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addAbstractTypes(resolver));
    }

    @Test
    public void testFindTypeMappingWithArrayList() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {});
        JavaType expectedType = objectMapper.getTypeFactory().constructType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMappingWithList() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<List<?>>() {});
        JavaType expectedType = objectMapper.getTypeFactory().constructType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMappingWithInvalidSuperType() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNull(actualType);
    }

    @Test
    public void testFindTypeMappingWithInvalidSubType() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractTypeWithAbstractType() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(), javaType);
        assertNull(actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidTypeRelationship() {
        resolver.addMapping(ArrayList.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelfType() {
        resolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractType() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(LinkedList.class);
        resolver.addMapping(javaType.getRawClass(), LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithEmptyType() {
        Class<?> emptyType = null;
        resolver.addMapping(emptyType, LinkedList.class);
    }
}