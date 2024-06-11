package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType[] types = new NamedType[2];
        types[0] = new NamedType(String.class);
        types[1] = new NamedType(Integer.class);
        resolver.registerSubtypes(types);
        
        LinkedHashSet<NamedType> expectedRegisteredSubtypes = new LinkedHashSet<NamedType>();
        expectedRegisteredSubtypes.add(types[0]);
        expectedRegisteredSubtypes.add(types[1]);
        assertEquals(expectedRegisteredSubtypes, resolver._registeredSubtypes);
    }
    
    @Test
    public void testRegisterSubtypesWithClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = new Class<?>[2];
        classes[0] = String.class;
        classes[1] = Integer.class;
        resolver.registerSubtypes(classes);
        
        LinkedHashSet<NamedType> expectedRegisteredSubtypes = new LinkedHashSet<NamedType>();
        expectedRegisteredSubtypes.add(new NamedType(classes[0]));
        expectedRegisteredSubtypes.add(new NamedType(classes[1]));
        assertEquals(expectedRegisteredSubtypes, resolver._registeredSubtypes);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(new ArrayList<NamedType>(), result);
        
        // Add types to registeredSubtypes
        NamedType[] types = new NamedType[2];
        types[0] = new NamedType(String.class);
        types[1] = new NamedType(Integer.class);
        resolver._registeredSubtypes = new LinkedHashSet<NamedType>();
        resolver._registeredSubtypes.add(types[0]);
        resolver._registeredSubtypes.add(types[1]);
        
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> expectedSubtypes = new ArrayList<NamedType>();
        expectedSubtypes.add(types[0]);
        assertEquals(expectedSubtypes, resolver.collectAndResolveSubtypesByClass(config, annotatedType, baseType));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(new ArrayList<NamedType>(), result);
        
        // Add types to registeredSubtypes
        NamedType[] types = new NamedType[2];
        types[0] = new NamedType(String.class);
        types[1] = new NamedType(Integer.class);
        resolver._registeredSubtypes = new LinkedHashSet<NamedType>();
        resolver._registeredSubtypes.add(types[0]);
        resolver._registeredSubtypes.add(types[1]);
        
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();
        
        Collection<NamedType> expectedSubtypes = new ArrayList<NamedType>();
        expectedSubtypes.add(types[0]);
        assertEquals(expectedSubtypes, resolver.collectAndResolveSubtypesByTypeId(config, annotatedType, typesHandled, byName));
    }
}