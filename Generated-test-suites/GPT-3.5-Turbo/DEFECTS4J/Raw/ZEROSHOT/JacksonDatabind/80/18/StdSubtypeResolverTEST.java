package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(SubtypeClass1.class), new NamedType(SubtypeClass2.class));
        
        // Assert that the registered subtypes are correct
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(SubtypeClass1.class)));
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(SubtypeClass2.class)));
    }
    
    @Test
    public void testRegisterSubtypes_empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes();
        
        // Assert that the registered subtypes is empty
        assertNotNull(resolver._registeredSubtypes);
        assertTrue(resolver._registeredSubtypes.isEmpty());
    }
    
    @Test
    public void testRegisterSubtypes_null() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes((NamedType) null);
        
        // Assert that the registered subtypes is empty
        assertNotNull(resolver._registeredSubtypes);
        assertTrue(resolver._registeredSubtypes.isEmpty());
    }
    
    @Test
    public void testRegisterSubtypes_duplicate() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(SubtypeClass1.class));
        resolver.registerSubtypes(new NamedType(SubtypeClass1.class));
        
        // Assert that only one instance of the duplicate subtype is registered
        assertEquals(1, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(SubtypeClass1.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_baseTypeNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(SubtypeClass1.class);
        NamedType subtype2 = new NamedType(SubtypeClass2.class);
        resolver.registerSubtypes(subtype1, subtype2);

        // Test when the base type is null
        assertNull(resolver.collectAndResolveSubtypesByClass(null, null, null));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_noAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(SubtypeClass1.class);
        NamedType subtype2 = new NamedType(SubtypeClass2.class);
        resolver.registerSubtypes(subtype1, subtype2);

        // Test when there are no annotations
        assertNull(resolver.collectAndResolveSubtypesByClass(new MapperConfig<Object>() {}, null, null));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(SubtypeClass1.class);
        NamedType subtype2 = new NamedType(SubtypeClass2.class);
        resolver.registerSubtypes(subtype1, subtype2);
        
        // Test when there are annotations present
        assertNotNull(resolver.collectAndResolveSubtypesByClass(new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return new AnnotationIntrospector() {
                    @Override
                    public Collection<NamedType> findSubtypes(Annotated ann) {
                        return Arrays.asList(new NamedType(AnnotatedSubtypeClass1.class), new NamedType(AnnotatedSubtypeClass2.class));
                    }
                };
            }
        }, null, null));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(SubtypeClass1.class);
        NamedType subtype2 = new NamedType(SubtypeClass2.class);
        resolver.registerSubtypes(subtype1, subtype2);
        
        // Test when there are no annotations or base type
        assertNull(resolver.collectAndResolveSubtypesByTypeId(new MapperConfig<Object>() {}, null, null));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(SubtypeClass1.class);
        NamedType subtype2 = new NamedType(SubtypeClass2.class);
        resolver.registerSubtypes(subtype1, subtype2);
        
        // Test when there are annotations present
        assertNotNull(resolver.collectAndResolveSubtypesByTypeId(new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return new AnnotationIntrospector() {
                    @Override
                    public Collection<NamedType> findSubtypes(Annotated ann) {
                        return Arrays.asList(new NamedType(AnnotatedSubtypeClass1.class), new NamedType(AnnotatedSubtypeClass2.class));
                    }
                };
            }
        }, null, null));
    }
    
    private static class SubtypeClass1 {}
    
    private static class SubtypeClass2 {}
    
    private static class AnnotatedSubtypeClass1 {}
    
    private static class AnnotatedSubtypeClass2 {}
}