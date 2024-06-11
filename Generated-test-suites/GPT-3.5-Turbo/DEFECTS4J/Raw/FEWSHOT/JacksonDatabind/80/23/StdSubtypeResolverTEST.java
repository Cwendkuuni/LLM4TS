package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Child.class));

        assertEquals(1, resolver._registeredSubtypes.size());
        
        resolver.registerSubtypes(new NamedType(Grandchild.class));
        assertEquals(2, resolver._registeredSubtypes.size());
    }
    
    @Test
    public void testRegisterSubtypes_NullArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes((NamedType[]) null);
        assertNull(resolver._registeredSubtypes);
    }
    
    @Test
    public void testRegisterSubtypes_NullClasss() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes((Class<?>[]) null);
        assertNull(resolver._registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Child.class), new NamedType(Grandchild.class));
        
        // Test collectAndResolveSubtypesByClass with baseType = null
        Collection<NamedType> result1 = resolver.collectAndResolveSubtypesByClass(null, null);
        assertEquals(2, result1.size());
        
        // Test collectAndResolveSubtypesByClass with baseType = Child.class
        AnnotatedMember property = null;
        JavaType baseType = TypeFactory.defaultInstance().constructType(Child.class);
        Collection<NamedType> result2 = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        assertEquals(1, result2.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Child.class), new NamedType(Grandchild.class));
        
        // Test collectAndResolveSubtypesByTypeId with baseType = null
        Collection<NamedType> result1 = resolver.collectAndResolveSubtypesByTypeId(null, null);
        assertEquals(2, result1.size());
        
        // Test collectAndResolveSubtypesByTypeId with baseType = Child.class
        AnnotatedMember property = null;
        JavaType baseType = TypeFactory.defaultInstance().constructType(Child.class);
        Collection<NamedType> result2 = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        assertEquals(1, result2.size());
    }
}