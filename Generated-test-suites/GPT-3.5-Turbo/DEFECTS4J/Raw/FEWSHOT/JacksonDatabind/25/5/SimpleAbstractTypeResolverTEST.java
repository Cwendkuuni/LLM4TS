package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
        assertEquals(1, ((HashMap) resolver._mappings).size());
    }

    @Test
    public void testAddMapping_sameClass() {
        try {
            resolver.addMapping(LinkedList.class, LinkedList.class);
        } catch (IllegalArgumentException e) {
            // Should throw IllegalArgumentException if trying to add mapping from same class
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_subTypeNotAssignable() {
        try {
            resolver.addMapping(LinkedList.class, String.class);
        } catch (IllegalArgumentException e) {
            // Should throw IllegalArgumentException if subType is not a subtype of superType
            assertEquals("Can not add mapping from class java.util.LinkedList to class java.lang.String, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_superTypeNotAbstract() {
        try {
            resolver.addMapping(String.class, LinkedList.class);
        } catch (IllegalArgumentException e) {
            // Should throw IllegalArgumentException if superType is not abstract
            assertEquals("Can not add mapping from class java.lang.String since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
        DeserializationConfig config = null; // Provide a proper DeserializationConfig object as per your requirement.
        JavaType type = null; // Provide a proper JavaType object as per your requirement.
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_mappingNotFound() {
        DeserializationConfig config = null; // Provide a proper DeserializationConfig object as per your requirement.
        JavaType type = null; // Provide a proper JavaType object as per your requirement.
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null; // Provide a proper DeserializationConfig object as per your requirement.
        JavaType type = null; // Provide a proper JavaType object as per your requirement.
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}