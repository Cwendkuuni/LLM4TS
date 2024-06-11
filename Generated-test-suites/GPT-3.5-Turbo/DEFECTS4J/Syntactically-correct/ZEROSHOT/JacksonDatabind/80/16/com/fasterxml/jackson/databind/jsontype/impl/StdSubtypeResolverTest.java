package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {
    
    private StdSubtypeResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
    }
    
    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(Example1.class);
        NamedType type2 = new NamedType(Example2.class);
        resolver.registerSubtypes(type1, type2);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(type1);
        expected.add(type2);
        
        assertEquals(expected, resolver._registeredSubtypes);
    }
    
    @Test
    public void testRegisterSubtypesClass() {
        Class<?> class1 = Example1.class;
        Class<?> class2 = Example2.class;
        resolver.registerSubtypes(class1, class2);
        
        NamedType type1 = new NamedType(class1);
        NamedType type2 = new NamedType(class2);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(type1);
        expected.add(type2);
        
        assertEquals(expected, resolver._registeredSubtypes);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass1() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(new NamedType(Example1.class));
        expected.add(new NamedType(Example2.class));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass2() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, type);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(new NamedType(Example1.class));
        expected.add(new NamedType(Example2.class));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId1() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(new NamedType(Example1.class));
        expected.add(new NamedType(Example2.class));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId2() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(new NamedType(Example1.class));
        expected.add(new NamedType(Example2.class));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCollectAndResolve() {
        AnnotatedClass annotatedType = null;
        NamedType namedType = new NamedType(Example1.class);
        MapperConfig<?> config = null;
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<NamedType, NamedType>();
        
        resolver._collectAndResolve(annotatedType, namedType, config, ai, collectedSubtypes);
        
        HashMap<NamedType, NamedType> expected = new HashMap<NamedType, NamedType>();
        expected.put(namedType, namedType);
        
        assertEquals(expected, collectedSubtypes);
    }
    
    @Test
    public void testCollectAndResolveByTypeId() {
        AnnotatedClass annotatedType = null;
        NamedType namedType = new NamedType(Example1.class);
        MapperConfig<?> config = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        
        resolver._collectAndResolveByTypeId(annotatedType, namedType, config, typesHandled, byName);
        
        Map<String,NamedType> expected = new LinkedHashMap<String,NamedType>();
        expected.put(namedType.getName(), namedType);
        
        assertEquals(expected, byName);
    }
    
    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> rawBase = Example1.class;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        
        Collection<NamedType> result = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        
        Collection<NamedType> expected = new ArrayList<NamedType>();
        expected.add(new NamedType(Example1.class));
        expected.add(new NamedType(Example2.class));
        
        assertEquals(expected, result);
    }
    
    private static class Example1 {}
    
    private static class Example2 {}
}