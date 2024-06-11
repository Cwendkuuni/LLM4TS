package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.registerSubtype(String.class, "string");
        // Test not used with class name-based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String value = "test";
        String expectedId = resolver._idFrom(value, value.getClass(), null);
        assertEquals(expectedId, resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String value = "test";
        String expectedId = resolver._idFrom(value, String.class, null);
        assertEquals(expectedId, resolver.idFromValueAndType(value, String.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.lang.String";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "unknown.Class";
        DatabindContext context = new DeserializationContext(null, null);
        JavaType expectedType = context.handleUnknownTypeId(null, id, resolver, "no such class found");
        assertEquals(expectedType, resolver.typeFromId(context, id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "<Invalid>";
        resolver.typeFromId(null, id);
    }

    @Test
    public void test_typeFromId_with_GenericType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.util.List<java.lang.String>";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_typeFromId_with_EnumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.util.EnumSet<java.lang.String>";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_typeFromId_with_EnumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.util.EnumMap<java.lang.String,java.lang.Object>";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_typeFromId_with_ArraysAsList() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.util.Arrays$ArrayList";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_typeFromId_with_InnerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "com.fasterxml.jackson.databind.util.ClassUtil$EnumTypeLocator";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_typeFromId_with_NonStaticInnerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "com.fasterxml.jackson.databind.util.ClassUtil$1";
        JavaType expectedType = resolver._typeFromId(id, null);
        assertEquals(expectedType, resolver.typeFromId(null, id));
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String value = "test";
        Class<?> cls = String.class;
        String expectedId = cls.getName();
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_with_EnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumSet<?> value = EnumSet.allOf(TestEnum.class);
        Class<?> cls = value.getClass();
        String expectedId = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, TestEnum.class).toCanonical();
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_with_EnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumMap<?, ?> value = new EnumMap<>(TestEnum.class);
        Class<?> cls = value.getClass();
        String expectedId = TypeFactory.defaultInstance().constructMapType(EnumMap.class, TestEnum.class, Object.class).toCanonical();
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_with_ArraysAsList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String[] value = {"1", "2", "3"};
        Class<?> cls = value.getClass();
        String expectedId = "java.util.ArrayList";
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_with_InnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        InnerClass value = new InnerClass();
        Class<?> cls = value.getClass();
        String expectedId = cls.getName();
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_with_NonStaticInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        NonStaticInnerClass value = new NonStaticInnerClass();
        Class<?> cls = value.getClass();
        String expectedId = cls.getName();
        assertEquals(expectedId, resolver._idFrom(value, cls, null));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    private enum TestEnum { VALUE; }

    private class InnerClass { }

    private class NonStaticInnerClass { }
}