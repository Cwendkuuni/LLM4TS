
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Number.class, Integer.class)
                .addMapping(Number.class, Double.class);
        
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));
        assertEquals(Double.class, resolver._mappings.get(new ClassKey(Number.class)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Number.class, Number.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSubTypeInvalid() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Number.class, String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Integer.class, Double.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        resolver.addMapping(Collection.class, LinkedList.class)
                .addMapping(List.class, LinkedList.class);
        
        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        assertNull(resolver.resolveAbstractType(config, type));
    }
    
    
}
