package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    private ObjectMapper objectMapper;

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        assertNotNull(simpleAbstractTypeResolver);
        simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() throws JsonMappingException {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper objectMapper = new ObjectMapper(config);
        objectMapper.setTypeResolver(new SimpleAbstractTypeResolver());

        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = simpleAbstractTypeResolver.findTypeMapping(config, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() throws JsonMappingException {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper objectMapper = new ObjectMapper(config);
        objectMapper.setTypeResolver(new SimpleAbstractTypeResolver());

        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType resolvedType = simpleAbstractTypeResolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidArguments() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(ArrayList.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidArguments2() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(ArrayList.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(LinkedList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidAbstractClass() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(ArrayList.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubClass() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(ArrayList.class, String[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithDuplicateKeys() {
        SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
        simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
        simpleAbstractTypeResolver.addMapping(Collection.class, ArrayList.class);
    }
}