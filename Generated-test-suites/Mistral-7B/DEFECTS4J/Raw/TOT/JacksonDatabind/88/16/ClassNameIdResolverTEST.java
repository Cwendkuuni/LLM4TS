package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
        String id = classResolver.idFromValue(new Object());
        assertNotNull(id);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver classResolver = new ClassNameIdResolver(String.class, objectMapper.getTypeFactory());
        String id = classResolver.idFromValueAndType(new String(), String.class);
        assertNotNull(id);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver classResolver = new ClassNameIdResolver(Integer.class, objectMapper.getTypeFactory());
        JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), "java.lang.Integer");
        assertNotNull(javaType);
        assertEquals(Integer.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdUnknownClass() throws IOException {
        ClassNameIdResolver classResolver = new ClassNameIdResolver(Integer.class, objectMapper.getTypeFactory());
        JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), "com.test.UnknownClass");
        assertNull(javaType);
    }

    @Test
    public void testTypeFromIdGenericType() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType("java.util.List<java.lang.String>");
        ClassNameIdResolver classResolver = new ClassNameIdResolver(javaType, objectMapper.getTypeFactory());
        String id = classResolver.idFromValueAndType(Arrays.asList("String1", "String2"), javaType);
        assertEquals("java.util.List<java.lang.String>", id);
    }

    @Test
    public void testTypeFromIdEnum() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType("java.lang.EnumSet<java.lang.Enum>");
        ClassNameIdResolver classResolver = new ClassNameIdResolver(javaType, objectMapper.getTypeFactory());
        EnumSet<Enum> enumSet = EnumSet.of(Enum1.VALUE1, Enum1.VALUE2);
        String id = classResolver.idFromValueAndType(enumSet, javaType);
        assertEquals("java.lang.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void testTypeFromIdEnumMap() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType("java.util.EnumMap<java.lang.Enum, java.lang.String>");
        ClassNameIdResolver classResolver = new ClassNameIdResolver(javaType, objectMapper.getTypeFactory());
        EnumMap<Enum, String> enumMap = new EnumMap<>(Enum1.class);
        enumMap.put(Enum1.VALUE1, "Value1");
        enumMap.put(Enum1.VALUE2, "Value2");
        String id = classResolver.idFromValueAndType(enumMap, javaType);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.String>", id);
    }
}