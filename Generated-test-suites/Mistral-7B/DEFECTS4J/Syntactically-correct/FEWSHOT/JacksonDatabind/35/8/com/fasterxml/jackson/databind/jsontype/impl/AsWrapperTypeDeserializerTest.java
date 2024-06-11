package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, String.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        Object result = typeDeserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, String.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, ArrayList.class);
        JsonParser jp = mapper.getFactory().createParser("[{\"type\":\"java.lang.String\",\"value\":\"test1\"},{\"type\":\"java.lang.String\",\"value\":\"test2\"}]");
        Object result = typeDeserializer.deserializeTypedFromArray(jp, mapper.getDeserializationContext());
        List<String> list = (List<String>) result;
        assertEquals(2, list.size());
        assertEquals("test1", list.get(0));
        assertEquals("test2", list.get(1));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, Integer.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.Integer\",\"value\":42}");
        Object result = typeDeserializer.deserializeTypedFromScalar(jp, mapper.getDeserializationContext());
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(Number.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, Number.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"42\"}");
        Object result = typeDeserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext());
        assertEquals(42, result.doubleValue());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, String.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\"}");
        Object result = typeDeserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
        assertEquals("", result);
    }

    @Test(expected = IOException.class)
    public void testWrongTokenException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, String.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        jp.nextToken(); // consume START_OBJECT
        jp.nextToken(); // consume FIELD_NAME
        throwExpectedException(jp, typeDeserializer, "expected closing END_OBJECT after type information and deserialized value");
    }

    @Test(expected = IOException.class)
    public void testWrongTokenException2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", true, String.class);
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        jp.nextToken(); // consume START_OBJECT
        jp.nextToken(); // consume FIELD_NAME
        jp.nextToken(); // consume START_ARRAY
        jp.nextToken(); // consume END_ARRAY
        throwExpectedException(jp, typeDeserializer, "expected closing END_OBJECT after type information and deserialized value");
    }

    private void throwExpectedException(JsonParser jp, TypeDeserializer typeDeserializer, String message) throws IOException {
        Object result = typeDeserializer.deserializeTypedFromObject(jp, typeDeserializer.getDeserializationContext());
        jp.nextToken(); // consume END_OBJECT
        fail(message);
    }
}