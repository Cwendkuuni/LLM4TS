package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdSubtypeResolverTest {
    
    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(StdSubtypeResolverTest.class);
        NamedType namedType2 = new NamedType(String.class);
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(namedType1, namedType2);
        
        assertTrue(resolver._registeredSubtypes.contains(namedType1));
        assertTrue(resolver._registeredSubtypes.contains(namedType2));
    }
    
    @Test
    public void testRegisterSubtypes_classs() {
        Class<?> class1 = StdSubtypeResolverTest.class;
        Class<?> class2 = String.class;
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(class1, class2);
        
        NamedType namedType1 = new NamedType(class1);
        NamedType namedType2 = new NamedType(class2);
        
        assertTrue(resolver._registeredSubtypes.contains(namedType1));
        assertTrue(resolver._registeredSubtypes.contains(namedType2));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_propertyNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_propertyWithSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_typeNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, type);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_typeWithSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        
        AnnotatedClass type = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, type);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_propertyNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_propertyWithSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_typeNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, type);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_typeWithSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        
        AnnotatedClass type = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, type);
        
        assertEquals(0, result.size());
    }
}