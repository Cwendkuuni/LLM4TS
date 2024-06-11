package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractType.class, ConcreteType.class);
        resolver.addMapping(List.class, ArrayList.class);
        
        // Test mappings
        assertEquals(ConcreteType.class, resolver._mappings.get(new ClassKey(AbstractType.class)));
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(List.class)));
        
        // Test exceptions
        try {
            resolver.addMapping(List.class, String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.List to java.lang.String, as latter is not a subtype of former", e.getMessage());
        }
        
        try {
            resolver.addMapping(AbstractType.class, AbstractType.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class com.fasterxml.jackson.databind.module.AbstractType to com.fasterxml.jackson.databind.module.AbstractType, as latter is not a subtype of former", e.getMessage());
        }
        
        try {
            resolver.addMapping(ConcreteType.class, AbstractType.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class com.fasterxml.jackson.databind.module.ConcreteType since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractType.class, ConcreteType.class);
        resolver.addMapping(List.class, ArrayList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType abstractType = TypeFactory.defaultInstance().constructFromCanonical(AbstractType.class.getCanonicalName());
        JavaType listType = TypeFactory.defaultInstance().constructFromCanonical(List.class.getCanonicalName());
        
        // Test mappings
        assertEquals(ConcreteType.class, resolver.findTypeMapping(config, abstractType).getRawClass());
        assertEquals(ArrayList.class, resolver.findTypeMapping(config, listType).getRawClass());
        
        // Test non-existent mapping
        assertNull(resolver.findTypeMapping(config, TypeFactory.defaultInstance().constructFromCanonical(Map.class.getCanonicalName())));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType abstractType = TypeFactory.defaultInstance().constructFromCanonical(AbstractType.class.getCanonicalName());
        
        // Test resolveAbstractType always returns null
        assertNull(resolver.resolveAbstractType(config, abstractType));
    }
    
    private abstract class AbstractType {
        
    }
    
    private class ConcreteType extends AbstractType {
        
    }
}