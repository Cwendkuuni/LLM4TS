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
import com.fasterxml.jackson.databind.DeserializationContext.Base;
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
        resolver.registerSubtype(Object.class, "object");
        // Test case for registerSubtype can not be created as this method is not used in ClassNameIdResolver
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "java.lang.Object";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Object.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "unknown.id";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void test_typeFromId_with_Generics() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "java.util.Map<java.lang.String, java.lang.Object>";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getKeyType().getRawClass());
        assertEquals(Object.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void test_typeFromId_with_innerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$TestInnerClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(ClassNameIdResolver.TestInnerClass.class, javaType.getRawClass());
    }

    @Test(expected = ClassNotFoundException.class)
    public void test_typeFromId_with_UnknownClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "unknown.class";
        resolver.typeFromId(context, id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_with_InvalidId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        DatabindContext context = new Base(null, null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        resolver.typeFromId(context, id);
    }

    @Test
    public void test_idFrom_without_genrics() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void test_idFrom_with_enum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = EnumTest.TEST;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver.EnumTest", id);
    }

    @Test
    public void test_idFrom_with_enum_subClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = EnumSubClassTest.TEST;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$EnumTest", id);
    }

    @Test
    public void test_idFrom_with_java_util_enums() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = EnumSetTest.TEST;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("java.util.EnumSet", id);
    }

    @Test
    public void test_idFrom_with_java_util_collections() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = CollectionsTest.TEST;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFrom_with_innerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new TestInnerClass();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$TestInnerClass", id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String desc = resolver.getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }

    private enum EnumTest {
        TEST
    }

    private enum EnumSubClassTest {
        TEST {
            public String toString() {
                return "Test";
            }
        }
    }

    private enum EnumSetTest {
        TEST;
    }

    private static class TestInnerClass {
    }

    private static class CollectionsTest {
        @SuppressWarnings("rawtypes")
        private static final ArrayList TEST = new ArrayList<>();
    }
}