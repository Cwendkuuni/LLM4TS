package com.fasterxml.jackson.databind.jsontype.impl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.anintrospect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.anintrospect.AnnotatedMember;
import com.fasterxml.jackson.databind.anintrospect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.anintrospect.AnnotationIntrospector;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotatedMember property;
    private JavaType baseType;
    private AnnotatedClass annotatedType;
    private NamedType namedType;
    private Set<Class<?>> typesHandled;
    private Map<String,NamedType> byName;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        config = new MapperConfigImpl();
        property = new AnnotatedMemberImpl();
        baseType = new JavaTypeImpl();
        annotatedType = new AnnotatedClassImpl();
        namedType = new NamedType(Object.class);
        typesHandled = new HashSet<>();
        byName = new LinkedHashMap<>();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {new NamedType(Integer.class), new NamedType(Double.class)};
        subtypeResolver.registerSubtypes(types);
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?>[] classes = {Integer.class, Double.class};
        subtypeResolver.registerSubtypes(classes);
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithPropertyNull() {
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
        subtypeResolver.registerSubtypes(Integer.class);
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithSubtypesInAnnotatedType() {
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithPropertyNull() {
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
        subtypeResolver.registerSubtypes(Integer.class);
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithSubtypesInAnnotatedType() {
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCombineNamedAndUnnamedWithTypesHandledEmpty() {
        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(Object.class, typesHandled, byName);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCombineNamedAndUnnamedWithTypesHandledNotEmpty() {
        typesHandled.add(Integer.class);
        typesHandled.add(Double.class);
        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(Object.class, typesHandled, byName);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    private static class MapperConfigImpl extends MapperConfig<Object> {

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return null;
        }

        @Override
        public JavaType introspectClassAnnotations(Class<?> cls) {
            return null;
        }

        @Override
        public JavaType constructType(Class<?> cls) {
            return null;
        }

        @Override
        protected MapperConfig<Object> _withBase(BaseSettings newBase) {
            return null;
        }

    }

    private static class AnnotatedMemberImpl extends AnnotatedMember {
    }

    private static class JavaTypeImpl extends JavaType {
    }

    private static class AnnotatedClassImpl extends AnnotatedClass {
    }

}