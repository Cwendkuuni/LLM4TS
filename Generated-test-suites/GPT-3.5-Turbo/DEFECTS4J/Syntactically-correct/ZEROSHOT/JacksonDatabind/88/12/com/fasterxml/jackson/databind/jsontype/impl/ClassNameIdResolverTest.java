package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Test that the method does nothing
        resolver.registerSubtype(Object.class, "name");
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
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "java.lang.String";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_invalidId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "invalid.class.name";
        JavaType type = resolver.typeFromId(context, id);
        assertNull(type);
    }

    @Test
    public void test_typeFromId_unknownTypeId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null);
        String id = "unknown.class.name";
        JavaType type = resolver.typeFromId(context, id);
        assertNull(type);
    }

    @Test
    public void test_typeFromId_enumSet() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "java.util.EnumSet<com.example.EnumType>";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(EnumSet.class, type.getRawClass());
        assertEquals(com.example.EnumType.class, type.getContentType().getRawClass());
    }

    @Test
    public void test_typeFromId_enumMap() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "java.util.EnumMap<com.example.EnumType,java.util.List<java.lang.String>>";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(EnumMap.class, type.getRawClass());
        assertEquals(com.example.EnumType.class, type.getKeyType().getRawClass());
        assertEquals(List.class, type.getContentType().getRawClass());
        assertEquals(String.class, type.getContentType().getContentType().getRawClass());
    }

    @Test
    public void test_typeFromId_innerClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "com.example.OuterClass$InnerClass";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(com.example.OuterClass.InnerClass.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_innerClass_nonStatic() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "com.example.OuterClass$NonStaticInnerClass";
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
        assertEquals(com.example.OuterClass.NonStaticInnerClass.class, type.getRawClass());
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals(cls.getName(), id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}