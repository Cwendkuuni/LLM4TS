package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private MockConfig config;
    private MockAnnotatedMember property;
    private MockAnnotatedClass type;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
        config = new MockConfig();
        property = new MockAnnotatedMember();
        type = new MockAnnotatedClass();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = { new NamedType(String.class), new NamedType(Integer.class) };
        subtypeResolver.registerSubtypes(types);
        
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(types[0]));
        assertTrue(subtypeResolver._registeredSubtypes.contains(types[1]));
    }

    @Test
    public void testRegisterSubtypes_withClasses() {
        Class<?>[] classs = { String.class, Integer.class };
        subtypeResolver.registerSubtypes(classs);
        
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(classs[0])));
        assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(classs[1])));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withProperty() {
        subtypeResolver.registerSubtypes(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withBaseType() {
        subtypeResolver.registerSubtypes(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withProperty() {
        property.setRawType(String.class);
        subtypeResolver.registerSubtypes(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, null);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withBaseType() {
        type.setRawType(String.class);
        subtypeResolver.registerSubtypes(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    private class MockConfig extends MapperConfig<MockConfig> {
        private AnnotationIntrospector annotationIntrospector;

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return annotationIntrospector;
        }
    }

    private class MockAnnotatedMember extends AnnotatedMember {
        private Class<?> rawType;

        @Override
        public Class<?> getRawType() {
            return rawType;
        }

        @Override
        public JavaType getType() {
            return null;
        }
    }

    private class MockAnnotatedClass extends AnnotatedClass {
        private Class<?> rawType;

        @Override
        public Class<?> getRawType() {
            return rawType;
        }
    }

}