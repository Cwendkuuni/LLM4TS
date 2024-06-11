package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {
    
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = { new NamedType(TestClass1.class), new NamedType(TestClass2.class) };
        subtypeResolver.registerSubtypes(types);
        
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(TestClass1.class)) {
                assertEquals("TestClass1", subtype.getName());
            }
            if (subtype.getType().equals(TestClass2.class)) {
                assertEquals("TestClass2", subtype.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
        NamedType[] types = { new NamedType(TestClass1.class), new NamedType(TestClass2.class) };
        subtypeResolver.registerSubtypes(types);
        
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(TestClass1.class)) {
                assertEquals("TestClass1", subtype.getName());
            }
            if (subtype.getType().equals(TestClass2.class)) {
                assertEquals("TestClass2", subtype.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedTypes() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
        NamedType[] types = { new NamedType(TestClass1.class), new NamedType(TestClass2.class) };
        subtypeResolver.registerSubtypes(types);
        
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(TestClass1.class)) {
                assertEquals("TestClass1", subtype.getName());
            }
            if (subtype.getType().equals(TestClass2.class)) {
                assertEquals("TestClass2", subtype.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedTypes() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertEquals(0, subtypes.size());
    }
    
    private static class TestClass1 {};
    
    private static class TestClass2 {};

}