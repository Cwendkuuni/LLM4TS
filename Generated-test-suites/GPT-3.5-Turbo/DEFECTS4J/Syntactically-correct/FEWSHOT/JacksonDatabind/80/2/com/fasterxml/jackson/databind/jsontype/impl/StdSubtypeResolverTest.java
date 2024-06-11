package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(String.class);
        NamedType type2 = new NamedType(Integer.class);
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(type1, type2);
        
        Set<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(type1));
        assertTrue(registeredSubtypes.contains(type2));
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(cls1, cls2);
        
        Set<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(cls1)));
        assertTrue(registeredSubtypes.contains(new NamedType(cls2)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);
        
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, type);
        
        assertEquals(0, subtypes.size());
    }
}