package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Object.class, String.class);
        resolver.addMapping(Number.class, Integer.class);

        // Test for correct mappings
        assertEquals(String.class, resolver._mappings.get(new ClassKey(Object.class)));
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));

        // Test for incorrect mappings
        assertNull(resolver._mappings.get(new ClassKey(Double.class)));
        assertNull(resolver._mappings.get(new ClassKey(Long.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Object.class, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Integer.class, Double.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Object.class, String.class);
        resolver.addMapping(Number.class, Integer.class);

        DeserializationConfig config = new DeserializationConfig(null, null, null, null, null, null, null, null, null);
        TypeFactory typeFactory = TypeFactory.defaultInstance();

        JavaType objectType = typeFactory.constructType(Object.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType numberType = typeFactory.constructType(Number.class);
        JavaType integerType = typeFactory.constructType(Integer.class);
        JavaType doubleType = typeFactory.constructType(Double.class);

        // Test for correct mappings
        assertEquals(stringType, resolver.findTypeMapping(config, objectType));
        assertEquals(integerType, resolver.findTypeMapping(config, numberType));

        // Test for incorrect mappings
        assertNull(resolver.findTypeMapping(config, doubleType));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig(null, null, null, null, null, null, null, null, null);
        TypeFactory typeFactory = TypeFactory.defaultInstance();

        JavaType objectType = typeFactory.constructType(Object.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType numberType = typeFactory.constructType(Number.class);
        JavaType integerType = typeFactory.constructType(Integer.class);
        JavaType doubleType = typeFactory.constructType(Double.class);

        // Test for no materialization (always returns null)
        assertNull(resolver.resolveAbstractType(config, objectType));
        assertNull(resolver.resolveAbstractType(config, stringType));
        assertNull(resolver.resolveAbstractType(config, numberType));
        assertNull(resolver.resolveAbstractType(config, integerType));
        assertNull(resolver.resolveAbstractType(config, doubleType));
    }
}