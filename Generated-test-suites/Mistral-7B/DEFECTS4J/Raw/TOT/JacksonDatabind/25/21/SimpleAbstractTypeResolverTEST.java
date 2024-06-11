package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    /**
     * Test for findTypeMapping method with Collection as input.
     */
    @Test
    public void testFindTypeMappingWithCollection() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        JavaType javaTypeMapped = resolver.findTypeMapping(new ObjectMapper().getDeserializationConfig(), javaType);
        assertEquals(LinkedList.class, javaTypeMapped.getRawClass());
    }

    /**
     * Test for findTypeMapping method with List as input.
     */
    @Test
    public void testFindTypeMappingWithList() {
        JavaType javaType = new TypeReference<List<Double>>() {}.getType();
        JavaType javaTypeMapped = resolver.findTypeMapping(new ObjectMapper().getDeserializationConfig(), javaType);
        assertEquals(LinkedList.class, javaTypeMapped.getRawClass());
    }

    /**
     * Test for findTypeMapping method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingWithInvalidInput() {
        JavaType javaType = Object.class.getTypeName();
        resolver.findTypeMapping(new ObjectMapper().getDeserializationConfig(), javaType);
    }

    /**
     * Test for resolveAbstractType method.
     */
    @Test
    public void testResolveAbstractType() {
        JavaType javaType = ArrayType.getRawType(int[].class);
        JavaType javaTypeResolved = resolver.resolveAbstractType(new ObjectMapper().getDeserializationConfig(), javaType);
        assertEquals(null, javaTypeResolved);
    }
}