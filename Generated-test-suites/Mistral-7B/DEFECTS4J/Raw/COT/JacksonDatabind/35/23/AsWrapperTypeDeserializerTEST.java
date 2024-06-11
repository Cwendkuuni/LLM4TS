package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, String.class);
        String jsonString = "{\"typeId\":\"java.lang.String\",\"value\":\"test\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object[].class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, Integer.class);
        String jsonString = "[{\"typeId\":\"java.lang.Integer\",\"value\":42}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.createParser(jsonString), mapper.getDeserializationContext());
        assertArrayEquals(new Integer[] {42}, (Integer[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, Integer.class);
        String jsonString = "{\"typeId\":\"java.lang.Integer\",\"value\":42}";
        Object result = deserializer.deserializeTypedFromScalar(mapper.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, Double.class);
        String jsonString = "{\"typeId\":\"java.lang.Double\",\"value\":3.14}";
        Object result = deserializer.deserializeTypedFromAny(mapper.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(3.14, result);
    }

    @Test(expected = IOException.class)
    public void testMissingTypeIdException() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, String.class);
        String jsonString = "{\"value\":\"test\"}";
        deserializer.deserializeTypedFromObject(mapper.createParser(jsonString), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingValueException() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, String.class);
        String jsonString = "{\"typeId\":\"java.lang.String\"}";
        deserializer.deserializeTypedFromObject(mapper.createParser(jsonString), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeIdException() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_typeId", true, String.class);
        String jsonString = "{\"typeId\":\"java.lang.InvalidTypeId\",\"value\":\"test\"}";
        deserializer.deserializeTypedFromObject(mapper.createParser(jsonString), mapper.getDeserializationContext());
    }
}