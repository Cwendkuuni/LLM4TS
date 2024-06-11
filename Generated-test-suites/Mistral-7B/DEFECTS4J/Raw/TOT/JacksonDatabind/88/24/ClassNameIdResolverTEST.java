package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testIdFromValue() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), new TypeFactory());
        String value = "Hello World";
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        // Test for Enum
        Enum e = Enum.valueOf(String.class, "VALUE");
        id = resolver.idFromValue(e);
        assertEquals(e.getClass().getName(), id);

        // Test for Collections
        List<String> list = Arrays.asList("one", "two", "three");
        id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);

        EnumMap<String, String> map = new EnumMap<>(String.class);
        map.put(Enum.valueOf(String.class, "KEY"), "VALUE");
        id = resolver.idFromValue(map);
        assertEquals("java.util.EnumMap<java.lang.String, java.lang.String>", id);

        EnumSet<String> set = EnumSet.allOf(String.class);
        id = resolver.idFromValue(set);
        assertEquals("java.util.EnumSet", id);
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), new TypeFactory());
        String value = "Hello World";

        Class<?> type = String[].class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.String[]", id);

        // Test for Enum
        Enum e = Enum.valueOf(String.class, "VALUE");
        Class<?> enumClass = e.getClass();
        id = resolver.idFromValueAndType(e, enumClass);
        assertEquals(enumClass.getName(), id);

        // Test for Collections
        List<String> list = Arrays.asList("one", "two", "three");
        Class<?> listType = List.class;
        id = resolver.idFromValueAndType(list, listType);
        assertEquals("java.util.ArrayList<java.lang.String>", id);

        EnumMap<String, String> map = new EnumMap<>(String.class);
        Class<?> mapType = Map.class;
        id = resolver.idFromValueAndType(map, mapType);
        assertEquals("java.util.EnumMap<java.lang.String, java.lang.String>", id);

        EnumSet<String> set = EnumSet.allOf(String.class);
        Class<?> setType = Set.class;
        id = resolver.idFromValueAndType(set, setType);
        assertEquals("java.util.EnumSet", id);
    }

    @org.junit.Test
    public void testTypeFromId() throws IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), mapper.getTypeFactory());
        String json = "{\"id\":\"java.lang.String\"}";
        Object obj = mapper.readValue(json, new TypeReference<Object>() {});
        assertEquals(String.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String").getRawClass());

        json = "{\"id\":\"java.util.ArrayList<java.lang.String>\"}";
        obj = mapper.readValue(json, new TypeReference<Object>() {});
        assertEquals(ArrayList.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList<java.lang.String>").getRawClass());

        json = "{\"id\":\"java.util.EnumMap<java.lang.String, java.lang.String>\"}";
        obj = mapper.readValue(json, new TypeReference<Object>() {});
        assertEquals(EnumMap.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumMap<java.lang.String, java.lang.String>").getRawClass());

        json = "{\"id\":\"java.util.EnumSet<java.lang.String>\"}";
        obj = mapper.readValue(json, new TypeReference<Object>() {});
        assertEquals(EnumSet.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumSet<java.lang.String>").getRawClass());
    }

    @org.junit.Test
    public void testUnknownClass() throws IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), mapper.getTypeFactory());

        String json = "{\"id\":\"com.unknown.UnknownClass\"}";
        try {
            Object obj = mapper.readValue(json, new TypeReference<Object>() {});
            fail("Should not reach this point.");
        } catch (IOException e) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            JavaType javaType = resolver.typeFromId(ctxt, "com.unknown.UnknownClass");
            assertNull(javaType);
        }
    }

    @org.junit.Test
    public void testClassNotFoundException() throws IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), mapper.getTypeFactory());

        String json = "{\"id\":\"com.not.Found.Class\"}";
        try {
            Object obj = mapper.readValue(json, new TypeReference<Object>() {});
            fail("Should not reach this point.");
        } catch (ClassNotFoundException e) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            JavaType javaType = resolver.typeFromId(ctxt, "com.not.Found.Class");
            assertNull(javaType);
        }
    }

    @org.junit.Test
    public void testInvalidTypeId() throws IOException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), mapper.getTypeFactory());

        String json = "{\"id\":\"InvalidTypeId\"}";
        try {
            Object obj = mapper.readValue(json, new TypeReference<Object>() {});
            fail("Should not reach this point.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'InvalidTypeId' (for id type 'Id.class'): com.fasterxml.jackson.databind.JsonMappingException: Invalid type id 'InvalidTypeId'", e.getMessage());
        }
    }
}