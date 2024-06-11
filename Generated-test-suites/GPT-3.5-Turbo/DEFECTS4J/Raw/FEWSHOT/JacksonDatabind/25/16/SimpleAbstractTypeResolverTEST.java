package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
    
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding a valid mapping
        resolver.addMapping(Collection.class, ArrayList.class);
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(Collection.class)));
       
        // Test adding a mapping from class to itself
        try {
            resolver.addMapping(ArrayList.class, ArrayList.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Test adding a mapping with subtype not extending the superType
        try {
            resolver.addMapping(Collection.class, Integer.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.lang.Integer, as latter is not a subtype of former", e.getMessage());
        }
        
        // Test adding a mapping with superType not abstract
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Add mappings
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Test finding type mapping for raw class
        DeserializationConfig config = null; // Dummy config
        JavaType type = new JavaType(config, Collection.class);
        JavaType dstType = resolver.findTypeMapping(config, type);
        assertTrue(ArrayList.class.isAssignableFrom(dstType.getRawClass()));
        
        // Test finding type mapping for parameterized type
        JavaType listType = new JavaType(config, List.class);
        JavaType dstListType = resolver.findTypeMapping(config, listType);
        assertTrue(LinkedList.class.isAssignableFrom(dstListType.getRawClass()));
        
        // Test finding type mapping for unknown type
        JavaType unknownType = new JavaType(config, Set.class);
        JavaType dstUnknownType = resolver.findTypeMapping(config, unknownType);
        assertNull(dstUnknownType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test resolving abstract type
        DeserializationConfig config = null; // Dummy config
        JavaType type = new JavaType(config, Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
    
}