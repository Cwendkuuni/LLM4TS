package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding a mapping for Collection to LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);
        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        assertEquals(1, mappings.size());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));

        // Test adding a mapping for List to LinkedList
        resolver.addMapping(List.class, LinkedList.class);
        mappings = resolver._mappings;
        assertEquals(2, mappings.size());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));

        // Test adding a mapping for String to LinkedList (should fail)
        try {
            resolver.addMapping(String.class, LinkedList.class);
            assert false : "Should have thrown an exception";
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding a mapping for LinkedList to ArrayList (should fail)
        try {
            resolver.addMapping(LinkedList.class, ArrayList.class);
            assert false : "Should have thrown an exception";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(List.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);

        javaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);
    }
}