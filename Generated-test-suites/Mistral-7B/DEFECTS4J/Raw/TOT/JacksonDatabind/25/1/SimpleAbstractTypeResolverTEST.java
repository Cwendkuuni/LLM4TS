package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for SimpleAbstractTypeResolver
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
     * with valid arguments
     */
    @Test
    public void testAddMapping() {
        resolver.addMapping(Map.class, HashMap.class);
        JavaType type = new TypeReference<Map<String, Integer>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), type);
        assertEquals(HashMap.class, resolvedType.getRawClass());
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
     * with incorrect arguments (types not related)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidArguments() {
        resolver.addMapping(String.class, Integer.class);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
     * with incorrect arguments (types equal)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelf() {
        resolver.addMapping(String.class, String.class);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
     * with incorrect arguments (supertype not abstract)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithConcreteSuperType() {
        resolver.addMapping(List.class, String.class);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
     * when no mapping exists
     */
    @Test
    public void testFindTypeMappingWithNoMapping() {
        JavaType type = new TypeReference<Map<String, Integer>>() {}.getType();
        assertNull(resolver.findTypeMapping(new DeserializationConfig(), type));
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
     * with valid mapping
     */
    @Test
    public void testFindTypeMappingWithMapping() {
        JavaType type = new TypeReference<Collection<String>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}
     * which should always return null
     */
    @Test
    public void testResolveAbstractType() {
        JavaType type = new TypeReference<Map<String, Integer>>() {}.getType();
        assertNull(resolver.resolveAbstractType(new DeserializationConfig(), type));
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
     * with cyclic dependencies
     */
    @Test(expected = StackOverflowError.class)
    public void testFindTypeMappingWithCyclicDependencies() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(LinkedList.class, ArrayList.class);
        JavaType type = new TypeReference<List<String>>() {}.getType();
        resolver.findTypeMapping(new DeserializationConfig(), type);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
     * with unbounded generic types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingWithUnboundedGenerics() {
        JavaType type = new TypeReference<Map<String, ?>>() {}.getType();
        resolver.findTypeMapping(new DeserializationConfig(), type);
    }
}