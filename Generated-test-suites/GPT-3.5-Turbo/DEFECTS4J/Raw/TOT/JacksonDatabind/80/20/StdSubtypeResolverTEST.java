package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import java.util.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        
        // Typical use case - registering a single subtype
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        
        // Edge case - registering multiple subtypes
        subtypeResolver.registerSubtypes(new NamedType(Bar.class), new NamedType(Baz.class));
        
        // Error scenario - registering null as subtype
        subtypeResolver.registerSubtypes(null);
        
        // Error scenario - registering duplicate subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        
        // Error scenario - registering subtypes with no type specified
        subtypeResolver.registerSubtypes(new NamedType(null));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        
        // Typical use case - resolving subtypes by class with registered subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assert(subtypes.size() == 1 && subtypes.contains(new NamedType(Foo.class)));
        
        // Edge case - resolving subtypes by class with no registered or annotated subtypes
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assert(subtypes.isEmpty());
        
        // Error scenario - resolving subtypes by null class
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assert(subtypes.isEmpty());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        
        // Typical use case - resolving subtypes by type id with registered subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null);
        assert(subtypes.size() == 1 && subtypes.contains(new NamedType(Foo.class)));
        
        // Edge case - resolving subtypes by type id with no registered or annotated subtypes
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null);
        assert(subtypes.isEmpty());
        
        // Error scenario - resolving subtypes by null type id
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null);
        assert(subtypes.isEmpty());
    }

    public static class Foo {}
    
    public static class Bar {}
    
    public static class Baz {}
}