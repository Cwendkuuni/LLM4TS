package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<Collection> superType = Collection.class;
        Class<List> subType = List.class;
        
        resolver.addMapping(superType, subType);
        
        assertTrue(resolver._mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass() {
        Class<Collection> superType = Collection.class;
        
        resolver.addMapping(superType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NotSubType() {
        Class<Collection> superType = Collection.class;
        Class<Integer> subType = Integer.class;
        
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NotAbstract() {
        Class<LinkedList> superType = LinkedList.class;
        Class<ArrayList> subType = ArrayList.class;
        
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping_withMapping() {
        Class<Collection> superType = Collection.class;
        Class<List> subType = List.class;
        
        resolver.addMapping(superType, subType);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(superType, null);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_noMapping() {
        Class<Collection> superType = Collection.class;
        Class<String> subType = String.class;
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(superType, null);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Collection.class, null);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}