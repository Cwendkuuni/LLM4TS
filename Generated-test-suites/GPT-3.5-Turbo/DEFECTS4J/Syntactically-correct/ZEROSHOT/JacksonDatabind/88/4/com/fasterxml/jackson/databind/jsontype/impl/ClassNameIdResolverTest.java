package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.registerSubtype(Object.class, "Object");
        // Assert that the method does nothing for class name-based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValueAndType(value, value.getClass());
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = Object.class.getName();
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_invalid_id() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "invalid_id";
        try {
            resolver.typeFromId(context, id);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void test_typeFromId_unknown_class() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "com.example.UnknownClass";
        JavaType type = resolver.typeFromId(context, id);
        assertNull(type);
    }

    @Test
    public void test_typeFromId_inner_class() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "com.example.OuterClass$InnerClass";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(com.example.OuterClass.InnerClass.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_inner_class_no_outer() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "com.example.InnerClass";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(com.example.InnerClass.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_enum_set() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "java.util.EnumSet";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(java.util.EnumSet.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_enum_map() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "java.util.EnumMap";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(java.util.EnumMap.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_non_generic_collection() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null);
        String id = "java.util.ArrayList";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(java.util.ArrayList.class, type.getRawClass());
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}