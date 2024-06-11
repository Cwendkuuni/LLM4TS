
package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.*;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {
    
    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotationIntrospector annotationIntrospector;
    
    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }
        };
        annotationIntrospector = new AnnotationIntrospector() {
            @Override
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                return null;
            }
        };
    }
    
    @Test
    public void testRegisterSubtypesWithNamedType() {
        NamedType expectedType1 = new NamedType(ExampleType1.class, "type1");
        NamedType expectedType2 = new NamedType(ExampleType2.class, "type2");
        
        subtypeResolver.registerSubtypes(expectedType1, expectedType2);
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    @Test
    public void testRegisterSubtypesWithClass() {
        Class<?>[] expectedClasses = {ExampleType1.class, ExampleType2.class};
        NamedType expectedType1 = new NamedType(ExampleType1.class);
        NamedType expectedType2 = new NamedType(ExampleType2.class);
        
        subtypeResolver.registerSubtypes(ExampleType1.class, ExampleType2.class);
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType expectedType1 = new NamedType(ExampleType1.class, "type1");
        NamedType expectedType2 = new NamedType(ExampleType2.class, "type2");
        
        subtypeResolver.registerSubtypes(expectedType1, expectedType2);
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClassWithProperty() {
        NamedType expectedType1 = new NamedType(ExampleType1.class, "type1");
        NamedType expectedType2 = new NamedType(ExampleType2.class, "type2");
        AnnotatedMember property = null; // To be implemented
        
        annotationIntrospector = new AnnotationIntrospector() {
            @Override
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                return Arrays.asList(expectedType1, expectedType2);
            }
        };
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType expectedType1 = new NamedType(ExampleType1.class, "type1");
        NamedType expectedType2 = new NamedType(ExampleType2.class, "type2");
        
        subtypeResolver.registerSubtypes(expectedType1, expectedType2);
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithProperty() {
        NamedType expectedType1 = new NamedType(ExampleType1.class, "type1");
        NamedType expectedType2 = new NamedType(ExampleType2.class, "type2");
        AnnotatedMember property = null; // To be implemented
        
        annotationIntrospector = new AnnotationIntrospector() {
            @Override
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                return Arrays.asList(expectedType1, expectedType2);
            }
        };
        
        Collection<NamedType> actualSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, null);
        
        assertTrue(actualSubtypes.contains(expectedType1));
        assertTrue(actualSubtypes.contains(expectedType2));
        assertEquals(2, actualSubtypes.size());
    }
    
    private static class ExampleType1 { }
    
    private static class ExampleType2 { }
}

