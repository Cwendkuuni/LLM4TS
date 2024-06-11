package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    // Test case for addMapping method with valid inputs
    @Test
    public void testAddMapping_validInputs() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        Class<?> subType1 = resolver._mappings.get(new ClassKey(Collection.class));
        Class<?> subType2 = resolver._mappings.get(new ClassKey(List.class));
        
        assertEquals("LinkedList", subType1.getSimpleName());
        assertEquals("LinkedList", subType2.getSimpleName());
    }

    // Test case for addMapping method with invalid inputs
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_withInvalidInputs() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    // Test case for findTypeMapping method with existing mapping
    @Test
    public void testFindTypeMapping_withExistingMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        JavaType type = new JavaType();
        type.setRawClass(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        
        assertNotNull(mappedType);
        assertEquals("LinkedList", mappedType.getRawClass().getSimpleName());
    }

    // Test case for findTypeMapping method with non-existing mapping
    @Test
    public void testFindTypeMapping_withNonExistingMapping() {
        resolver = new SimpleAbstractTypeResolver();
        
        JavaType type = new JavaType();
        type.setRawClass(Collection.class);
        JavaType mappedType = resolver.findMapping(config, type);
        
        assertNull(mappedType);
    }

    // Test case for resolveAbstractType method
    @Test
    public void testResolveAbstractType() {
        resolver = new SimpleAbstractTypeResolver();
        JavaType type = new JavaType();
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}