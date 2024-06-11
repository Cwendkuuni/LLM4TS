
package com.fasterxml.jackson.databind.module;

import java.util.Collection;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Assert mappings
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelfType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithConcreteSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        DeserializationConfig config = null; // Create DeserializationConfig object
        
        // Create JavaType object for Collection
        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class);
        
        // Find type mapping
        JavaType resolvedType = resolver.findTypeMapping(config, collectionType);
        
        // Assert resolved type
        assertEquals(TypeFactory.defaultInstance().constructType(LinkedList.class), resolvedType);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        DeserializationConfig config = null; // Create DeserializationConfig object
        
        // Create JavaType object for abstract type
        JavaType abstractType = TypeFactory.defaultInstance().constructType(AbstractCollection.class);
        
        // Resolve abstract type
        JavaType resolvedType = resolver.resolveAbstractType(config, abstractType);
        
        // Assert resolved type
        assertNull(resolvedType);
    }
}
