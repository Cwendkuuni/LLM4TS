package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.*;

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
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, HashMap.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertEquals(LinkedList.class, result.getRawClass());
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}