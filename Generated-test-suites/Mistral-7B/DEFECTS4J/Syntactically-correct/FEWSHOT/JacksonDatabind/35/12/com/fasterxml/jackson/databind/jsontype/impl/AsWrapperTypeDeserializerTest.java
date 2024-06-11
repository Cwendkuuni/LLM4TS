package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\",\"value\":\"hello\"}";
        Object result = td.deserializeTypedFromObject(mapper.getFactory().createParser(json), new TestContext());

        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, int[].class);

        String json = "[{\"type\":\"java.lang.Integer\",\"value\":1}]";
        Object result = td.deserializeTypedFromArray(mapper.getFactory().createParser(json), new TestContext());

        assertArrayEquals(new int[]{1}, (int[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, Integer.class);

        String json = "{\"type\":\"java.lang.Integer\",\"value\":123}";
        Object result = td.deserializeTypedFromScalar(mapper.getFactory().createParser(json), new TestContext());

        assertEquals(123, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(Double.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, Double.class);

        String json = "{\"type\":\"java.lang.String\",\"value\":3.14}";
        Object result = td.deserializeTypedFromAny(mapper.getFactory().createParser(json), new TestContext());

        assertEquals(3.14, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\"}";
        Object result = td.deserializeTypedFromObject(mapper.getFactory().createParser(json), new TestContext());

        assertEquals("", result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, String.class);

        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class, null);
        JsonDeserializer<String> deser = td._findDeserializer(new TestContext(), "java.lang.String");

        assertEquals(deserializer, deser);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer td = new AsWrapperTypeDeserializer(javaType, idResolver, null, false, String.class);

        String json = "{\"type\":\"java.lang.String\"";
        td.deserializeTypedFromObject(mapper.getFactory().createParser(json), new TestContext());
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectMapper _currentValueHandler() {
        return null;
    }
}