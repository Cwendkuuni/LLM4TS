package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test for {@link StdSubtypeResolver#registerSubtypes(Class...)} method.
     */
    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Cat.class;
        Class<?> subtype2 = Dog.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfiguration(), null, baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, subtype1, subtype2);
    }

    /**
     * Test for {@link StdSubtypeResolver#registerSubtypes(NamedType...)} method.
     */
    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        String catName = "Cat";
        String dogName = "Dog";

        NamedType cat = new NamedType(Cat.class, catName);
        NamedType dog = new NamedType(Dog.class, dogName);

        subtypeResolver.registerSubtypes(cat, dog);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfiguration(), null, baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, cat, dog);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)} method.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Cat.class;
        Class<?> subtype2 = Dog.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfiguration(), null, baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, subtype1, subtype2);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)} method.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassAnnotated() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Cat.class;
        Class<?> subtype2 = Dog.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfiguration(), new AnnotatedClass(Object.class, null, null, null, null), baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, subtype1, subtype2);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)} method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Cat.class;
        Class<?> subtype2 = Dog.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfiguration(), null, baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, subtype1, subtype2);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)} method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotated() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Cat.class;
        Class<?> subtype2 = Dog.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfiguration(), new AnnotatedClass(Object.class, null, null, null, null), baseType);

        assertEquals(2, subtypes.size());
        assertSubtypes(subtypes, subtype1, subtype2);
    }

    /**
     * Helper method for verifying the collected subtypes.
     */
    private void assertSubtypes(List<NamedType> subtypes, Class<?>... expectedSubtypes) {
        for (NamedType subtype : subtypes) {
            if (subtype.hasName()) {
                assertEquals(expectedSubtypes[Arrays.asList(expectedSubtypes).indexOf(subtype.getType())].getName(), subtype.getName());
            } else {
                assertEquals(expectedSubtypes[Arrays.asList(expectedSubtypes).indexOf(subtype.getType())], subtype.getType());
            }
            assertNotNull(subtype.getName());
        }
    }

    /**
     * Helper class for testing.
     */
    static class Animal {}

    static class Cat extends Animal {}

    static class Dog extends Animal {}
}