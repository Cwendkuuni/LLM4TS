package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        // Test that method does not do anything
        resolver.registerSubtype(Object.class, "");
    }

    @Test
    public void testIdFromValue_withEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(TestEnum.ABC);
        assertNotNull(id);
        assertEquals(TestEnum.class.getName(), id);
    }

    @Test
    public void testIdFromValue_withoutEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(123);
        assertNotNull(id);
        assertEquals(Integer.class.getName(), id);
    }

    @Test
    public void testIdFromValueAndType_withEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(TestEnum.ABC, TestEnum.class);
        assertNotNull(id);
        assertEquals(TestEnum.class.getName(), id);
    }

    @Test
    public void testIdFromValueAndType_withoutEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(123, Integer.class);
        assertNotNull(id);
        assertEquals(Integer.class.getName(), id);
    }

    @Test
    public void testTypeFromId_withValidId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = TestEnum.class.getName();
        JavaType type = resolver.typeFromId(null, id);
        assertNotNull(type);
        assertEquals(TestEnum.class, type.getRawClass());
    }

    @Test
    public void testTypeFromId_withInvalidId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "com.example.DoesNotExist";
        try {
            resolver.typeFromId(null, id);
        } catch (IOException e) {
            // Expecting exception
            assertEquals("no such class found", e.getMessage());
        }
    }

    @Test
    public void testTypeFromId_withGenericType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.util.List<java.lang.Integer>";
        JavaType type = resolver.typeFromId(null, id);
        assertNotNull(type);
        assertEquals(Integer.class, type.containedType(0).getRawClass());
    }

    @Test
    public void test_typeFromId_withSpecializedType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(Integer.class), TypeFactory.defaultInstance());
        String id = Integer.class.getName();
        JavaType type = resolver.typeFromId(null, id);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test
    public void test_idFrom_withEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = resolver._idFrom(EnumSet.noneOf(TestEnum.class), EnumSet.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.TestEnum>", id);
    }

    @Test
    public void test_idFrom_withEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = resolver._idFrom(new EnumMap<TestEnum, Integer>(TestEnum.class), EnumMap.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.TestEnum,java.lang.Integer>", id);
    }

    @Test
    public void test_idFrom_withArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = resolver._idFrom(new ArrayList<String>(), ArrayList.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFrom_withNestedClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(NestedClass.InnerClass.class), TypeFactory.defaultInstance());
        String id = resolver._idFrom(new NestedClass.InnerClass(), NestedClass.InnerClass.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.NestedClass$InnerClass", id);
    }

    @Test
    public void test_idFrom_withNonStaticNestedClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(NonStaticNestedClass.InnerClass.class), TypeFactory.defaultInstance());
        String id = resolver._idFrom(new NonStaticNestedClass.InnerClass(), NonStaticNestedClass.InnerClass.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.NonStaticNestedClass$InnerClass", id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}

class NestedClass {
    static class InnerClass {}
}

class NonStaticNestedClass {
    class InnerClass {}
}

enum TestEnum {
    ABC
}