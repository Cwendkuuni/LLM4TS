package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private DeserializationConfig config;
    private JavaType type;
    
    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        config = new DeserializationConfig(null, null, null);
        type = TypeFactory.defaultInstance().constructType(Object.class);
    }
    
    @Test
    public void testAddMapping() {
        resolver.addMapping(Object.class, String.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertEquals(String.class, result.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameTypes() {
        resolver.addMapping(Object.class, Object.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNoSubtypeRelationship() {
        resolver.addMapping(String.class, Integer.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        resolver.addMapping(Integer.class, String.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Object.class, String.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertEquals(String.class, result.getRawClass());
    }
    
    @Test
    public void testFindTypeMappingWithNoMapping() {
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}