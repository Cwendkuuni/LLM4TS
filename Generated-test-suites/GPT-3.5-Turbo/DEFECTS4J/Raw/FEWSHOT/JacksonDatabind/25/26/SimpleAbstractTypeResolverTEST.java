package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        Class<?> result1 = resolver._mappings.get(new ClassKey(Collection.class));
        assertEquals(LinkedList.class, result1);
        
        Class<?> result2 = resolver._mappings.get(new ClassKey(List.class));
        assertEquals(LinkedList.class, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingFromClassToItself() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToNonAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig();
        config.setTypeFactory(new TypeFactory());
        JavaType type = config.constructType(Collection.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNoMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig();
        config.setTypeFactory(new TypeFactory());
        JavaType type = config.constructType(Collection.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig();
        config.setTypeFactory(new TypeFactory());
        JavaType type = config.constructType(Collection.class);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}