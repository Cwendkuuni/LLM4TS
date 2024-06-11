
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameType() {
        Class<?> type = Collection.class;
        resolver.addMapping(type, type);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_noSubtypeRelationship() {
        Class<?> superType = List.class;
        Class<?> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        Class<?> type = LinkedList.class;
        resolver.addMapping(type, type);
    }
    
    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        DeserializationConfig config = null; //TODO: Initialize with appropriate object
        JavaType type = null; //TODO: Initialize with appropriate object
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        //TODO: Assert the expected result with the actual result
    }
    
    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null; //TODO: Initialize with appropriate object
        JavaType type = null; //TODO: Initialize with appropriate object
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}
