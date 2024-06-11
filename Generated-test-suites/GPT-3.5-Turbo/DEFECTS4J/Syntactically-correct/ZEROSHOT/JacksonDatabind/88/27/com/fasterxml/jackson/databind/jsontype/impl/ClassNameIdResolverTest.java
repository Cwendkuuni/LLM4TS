package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = classNameIdResolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        // Registering a subtype should not have any effect as the class name based resolvers do not use it
        classNameIdResolver.registerSubtype(String.class, "String");
    }

    @Test
    public void testIdFromValue() {
        Object value = "test";
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = "test";
        Class<?> type = String.class;
        String id = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.lang.String";
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), javaType);
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.Unknown";
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void testTypeFromIdWithException() {
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "invalid";
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'invalid' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdWithUnknownType() {
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.Unknown";
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'com.example.Unknown' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdWithTypeContainingGenerics() throws IOException {
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.List<java.lang.String>";
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(java.util.List.class, String.class), javaType);
    }

    @Test
    public void testIdFromWithEnumSet() {
        EnumSet<SomeEnum> value = EnumSet.of(SomeEnum.FIRST, SomeEnum.SECOND);
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.SomeEnum>", id);
    }

    @Test
    public void testIdFromWithEnumMap() {
        EnumMap<SomeEnum, String> value = new EnumMap<>(SomeEnum.class);
        value.put(SomeEnum.FIRST, "first");
        value.put(SomeEnum.SECOND, "second");
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.SomeEnum,java.lang.String>", id);
    }

    @Test
    public void testIdFromWithTypeWithArraysList() {
        java.util.List<String> value = Arrays.asList("one", "two");
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromWithTypeWithArraysAsList() {
        java.util.List<String> value = java.util.Arrays.asList("one", "two");
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromWithTypeWithCollectionsSingletonList() {
        java.util.List<String> value = Collections.singletonList("value");
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromWithTypeWithCollectionsUncheckedSingletonList() {
        java.util.List<String> value = Collections.checkedList(Collections.singletonList("value"), String.class);
        String id = classNameIdResolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    private enum SomeEnum {
        FIRST, SECOND
    }
}