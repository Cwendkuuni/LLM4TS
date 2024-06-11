package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {
    @Test
    public void testConstructor() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        assertNull(subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testRegisterSubtypesNamedTypeArray() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        NamedType[] types = {new NamedType(String.class)};
        subtypeResolver.registerSubtypes(types);

        assertEquals(1, subtypeResolver._registeredSubtypes.size());
        assertEquals(String.class, subtypeResolver._registeredSubtypes.iterator().next().getType());
        assertNull(subtypeResolver._registeredSubtypes.iterator().next().getName());
    }

    @Test
    public void testRegisterSubtypesClassArray() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(String.class);

        assertEquals(1, subtypeResolver._registeredSubtypes.size());
        assertEquals(String.class, subtypeResolver._registeredSubtypes.iterator().next().getType());
        assertNull(subtypeResolver._registeredSubtypes.iterator().next().getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedMember property = null;
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByClass(config, property, null).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNonNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = JavaType.JavaTypeBuilder.build().withRawClass(Object.class).build();
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedMember property = null;
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, null).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNonNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = JavaType.JavaTypeBuilder.build().withRawClass(Object.class).build();
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNonNullAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedClass baseType = null;
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNonNullAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = null;
        MapperConfig config = null;
        AnnotatedClass baseType = null;
        assertEquals(0, subtypeResolver.collectAndResolveSubtypesByClass(config, baseType).size());
    }
}