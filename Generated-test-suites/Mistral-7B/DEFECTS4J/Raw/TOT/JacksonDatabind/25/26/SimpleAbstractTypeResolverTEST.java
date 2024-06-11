package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for SimpleAbstractTypeResolver.
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithInvalidType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType mappedType = resolver.findTypeMapping(config, null);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithMappedType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractTypeWithInvalidType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithNullType() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        mapper.setTypeResolver(resolver);

        JavaType resolvedType = resolver.resolveAbstractType(config, null);
        assertNull(resolvedType);
    }
}