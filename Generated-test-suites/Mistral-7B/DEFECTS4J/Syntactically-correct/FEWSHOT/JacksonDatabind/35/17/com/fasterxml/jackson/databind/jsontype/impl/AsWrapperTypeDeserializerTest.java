package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, "_type", true, String.class);

        String jsonString = "{\"_type\":\"java.lang.String\",\"value\":\"test\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, "_type", true, Integer.class);

        String jsonString = "[{\"_type\":\"java.lang.Integer\",\"value\":1},{\"_type\":\"java.lang.Integer\",\"value\":2}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.readTree(jsonString), mapper.getDeserializationContext());
        List<Integer> list = (List<Integer>) result;
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, "_type", true, String.class);

        String jsonString = "{\"_type\":\"java.lang.String\"}";
        Object result = deserializer.deserializeTypedFromScalar(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals("", result.toString());
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Boolean.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, "_type", true, Boolean.class);

        String jsonString1 = "{\"_type\":\"java.lang.Boolean\",\"value\":true}";
        Object result1 = deserializer.deserializeTypedFromAny(mapper.readTree(jsonString1), mapper.getDeserializationContext());
        assertTrue((Boolean) result1);

        String jsonString2 = "{\"_type\":\"java.lang.String\",\"value\":\"false\"}";
        Object result2 = deserializer.deserializeTypedFromAny(mapper.readTree(jsonString2), mapper.getDeserializationContext());
        assertFalse((Boolean) result2);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, null, "_type", true, String.class);

        String jsonString = "{\"_class\":\"java.lang.String\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals("", result);
    }
}