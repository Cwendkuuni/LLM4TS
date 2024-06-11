package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.classinfo.ClassInfo;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

/**
 * JUnit test file for SimpleAbstractTypeResolver class
 */
public class SimpleAbstractTypeResolverTest {

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
     */
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding a valid mapping
        resolver.addMapping(Collection.class, LinkedList.class);
        Class<?> mappedType = resolver._mappings.get(new ClassKey(Collection.class));
        assertEquals(LinkedList.class, mappedType);

        // Test adding a mapping for a generic type
        resolver.addMapping(List.class, LinkedList.class);
        mappedType = resolver._mappings.get(new ClassKey(List.class));
        assertEquals(LinkedList.class, mappedType);

        // Test adding a mapping for a raw type
        resolver.addMapping(ArrayList.class, LinkedList.class);
        mappedType = resolver._mappings.get(new ClassKey(ArrayList.class));
        assertEquals(LinkedList.class, mappedType);

        // Test adding a mapping for a non-abstract class
        SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
        try {
            resolver2.addMapping(String.class, LinkedList.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
     */
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType mappedType = resolver.findTypeMapping(null, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        mappedType = resolver.findTypeMapping(null, javaType);
        assertNull(mappedType);

        javaType = TypeFactory.defaultInstance().constructType(HashMap.class);
        mappedType = resolver.findTypeMapping(null, javaType);
        assertNull(mappedType);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}
     */
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.resolveAbstractType(null, javaType);
        assertNull(mappedType);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with cyclic dependencies
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithCyclicDependency() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }

    /**
     * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with duplicate keys
     */
    @Test(expected = IllegalStateException.class)
    public void testAddMappingWithDuplicateKey() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(Collection.class, ArrayList.class);
    }

}