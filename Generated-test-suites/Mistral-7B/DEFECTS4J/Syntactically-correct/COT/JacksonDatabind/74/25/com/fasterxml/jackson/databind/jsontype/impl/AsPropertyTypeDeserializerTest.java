package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.ClassIdHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
        this.asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                javaType, typeIdResolver, "typePropertyName", true, null, As.PROPERTY);
    }

    @Test
    public void testDeserializeTypedFromObject_ValidJsonObject() throws Exception {
        String json = "{\"typePropertyName\": {\"typeId\": \"com.test.TestClass\"}}";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);

        assertTrue(result instanceof TestClass);
    }

    @Test
    public void testDeserializeTypedFromObject_NullTypeId() throws Exception {
        String json = "{\"typePropertyName\": null}";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            //expected
        }
    }

    @Test
    public void testDeserializeTypedFromObject_InvalidTypeId() throws Exception {
        String json = "{\"typePropertyName\": {\"typeId\": \"com.invalid.InvalidClass\"}}";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            //expected
        }
    }

    @Test
    public void testDeserializeTypedFromObject_MissingTypeProperty() throws Exception {
        String json = "{}";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            //expected
        }
    }

    @Test
    public void testDeserializeTypedFromObject_WrongToken() throws Exception {
        String json = "[\"test\"]";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        try {
            asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            //expected
        }
    }

    @Test
    public void testDeserializeTypedFromObject_JsonObjectWithTypeIdVisible() throws Exception {
        String json = "{\"typePropertyName\": {\"typeId\": \"com.test.TestClass\", \"name\": \"test\"}}";
        JsonParser jsonParser = new JsonFactory().createParser(json);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);

        assertTrue(result instanceof TestClass);
        assertEquals("test", result.getClass().getSimpleName());
    }

    @Test
    public void testDeserializeTypedFromObject_TokenBuffer() throws Exception {
        String json = "{\"typePropertyName\": \"test\"}";
        JsonParser jsonParser = new JsonFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParser, null);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);

        assertTrue(result instanceof ObjectNode);
        assertEquals(json, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenBuffer.asParser()));
    }

    // Add more test cases for other methods in the AsPropertyTypeDeserializer class
}