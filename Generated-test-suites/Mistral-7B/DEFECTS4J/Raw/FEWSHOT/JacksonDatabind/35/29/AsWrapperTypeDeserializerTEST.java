package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.parseCharArray(json), mapper.getDeserializationContext());

        assertEquals("Hello World", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructArrayType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String[].class);

        String json = "[{\"type\":\"java.lang.String\",\"value\":\"Hello World 1\"},{\"type\":\"java.lang.String\",\"value\":\"Hello World 2\"}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.parseCharArray(json), mapper.getDeserializationContext());

        assertArrayEquals(new String[]{"Hello World 1", "Hello World 2"}, (String[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, Integer.class);

        String json = "{\"type\":\"java.lang.Integer\",\"value\":42}";
        Object result = deserializer.deserializeTypedFromScalar(mapper.parseCharArray(json), mapper.getDeserializationContext());

        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(Number.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, Number.class);

        String json1 = "{\"type\":\"java.lang.Integer\",\"value\":42}";
        Object result1 = deserializer.deserializeTypedFromAny(mapper.parseCharArray(json1), mapper.getDeserializationContext());

        assertEquals(42, result1);

        String json2 = "{\"type\":\"java.lang.Double\",\"value\":3.14}";
        Object result2 = deserializer.deserializeTypedFromAny(mapper.parseCharArray(json2), mapper.getDeserializationContext());

        assertEquals(3.14, result2);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\"}";
        Object result = deserializer.deserializeWithNativeTypeId(mapper.parseCharArray(json), mapper.getDeserializationContext());

        assertEquals("", result);

        String jsonWithTypeId = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
        Object resultWithTypeId = deserializer.deserializeWithNativeTypeId(mapper.parseCharArray(jsonWithTypeId), mapper.getDeserializationContext());

        assertEquals("Hello World", resultWithTypeId);
    }

    @Test(expected = IOException.class)
    public void testWrongJsonStructure() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"value\":\"Hello World\"}";
        deserializer.deserializeTypedFromObject(mapper.parseCharArray(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\"}";
        deserializer.deserializeWithNativeTypeId(mapper.parseCharArray(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingValue() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\"}";
        deserializer.deserializeTypedFromObject(mapper.parseCharArray(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingEndObject() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"";
        deserializer.deserializeTypedFromObject(mapper.parseCharArray(json), mapper.getDeserializationContext());
    }
}