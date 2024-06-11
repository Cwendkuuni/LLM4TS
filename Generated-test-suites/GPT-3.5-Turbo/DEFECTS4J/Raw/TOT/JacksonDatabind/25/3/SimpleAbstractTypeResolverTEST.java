package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
        assertTrue(resolver._mappings.containsKey(new ClassKey(List.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }
    
    @Test
    public void testAddMapping_sameType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_notSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to java.util.ArrayList, as latter is not a subtype of former", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(List.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.List since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping_matchingMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.getTypeFactory().constructType(List.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }
    
    @Test
    public void testFindTypeMapping_noMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.getTypeFactory().constructType(List.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.getTypeFactory().constructType(List.class);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}