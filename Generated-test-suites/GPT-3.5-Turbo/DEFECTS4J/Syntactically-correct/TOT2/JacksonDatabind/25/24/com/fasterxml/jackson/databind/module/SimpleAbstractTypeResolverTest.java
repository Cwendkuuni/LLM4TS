
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        Class<?> superType = Collection.class;
        Class<? extends Collection<?>> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
        
        assertTrue(resolver._mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test
    public void testAddMapping_SameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        Class<?> type = Collection.class;
        
        try {
            resolver.addMapping(type, type);
            fail("Should throw IllegalArgumentException for adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddMapping_NotSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        Class<?> superType = List.class;
        Class<?> subType = ArrayList.class;
        
        try {
            resolver.addMapping(superType, subType);
            fail("Should throw IllegalArgumentException for adding mapping with non-subtype class");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddMapping_NonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        Class<?> superType = HashMap.class;
        Class<?> subType = LinkedHashMap.class;
        
        try {
            resolver.addMapping(superType, subType);
            fail("Should throw IllegalArgumentException for adding mapping with non-abstract super type class");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Collection.class, LinkedList.class);
        
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType type = config.constructType(List.class);
        
        JavaType mappedType = resolver.findTypeMapping(config, type);
        
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_NotMapped() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType type = config.constructType(Map.class);
        
        JavaType mappedType = resolver.findTypeMapping(config, type);
        
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType type = config.constructType(List.class);
        
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        
        assertNull(resolvedType);
    }
}
