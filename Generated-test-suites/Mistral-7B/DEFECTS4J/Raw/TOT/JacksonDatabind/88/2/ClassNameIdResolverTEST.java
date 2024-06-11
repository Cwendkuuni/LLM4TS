package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIdFromValue() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new Object();
        String id = resolver.idFromValue(value);
        Class<?> actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected base type for unknown value", baseType.getRawClass(), actualType);

        value = new Integer(1);
        id = resolver.idFromValue(value);
        actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected Integer type for integer value", Integer.class, actualType);

        value = new String("test");
        id = resolver.idFromValue(value);
        actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected String type for string value", String.class, actualType);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new Object();
        Class<?> expectedType = String.class;
        String id = resolver.idFromValueAndType(value, String.class);
        Class<?> actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected String type for given value and expected type", expectedType, actualType);

        value = new Integer(1);
        expectedType = Integer.class;
        id = resolver.idFromValueAndType(value, Integer.class);
        actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected Integer type for given value and expected type", expectedType, actualType);

        value = new Date();
        expectedType = Date.class;
        id = resolver.idFromValueAndType(value, Date.class);
        actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
        assertEquals("Expected Date type for given value and expected type", expectedType, actualType);
    }

    @Test
    public void testTypeFromIdNullContext() {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        assertNull("Expected null for typeFromId with null context", resolver.typeFromId(null, "test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdInvalidId() throws IOException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        resolver.typeFromId(objectMapper.getDeserializationContext(), "invalidId");
    }
}