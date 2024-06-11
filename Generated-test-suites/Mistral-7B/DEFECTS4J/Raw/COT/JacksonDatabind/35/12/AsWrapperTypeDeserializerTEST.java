package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    private ObjectMapper mapper;
    private TypeIdResolver idResolver;
    private TypeDeserializer deserializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        idResolver = mapper.getTypeIdResolver();
        deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), idResolver, "_type", true, String.class);
    }

    // Test for basic functionality of each method
    @Test
    public void testDeserializeTypedFromObject() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        assertEquals("test", deserializer.deserializeTypedFromObject(jp, context()));
    }

    @Test
    public void testDeserializeTypedFromArray() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("[\"" + As.WRAPPER_OBJECT.idFromValue(String.class) + "\",\"test\"]");
        assertEquals("test", deserializer.deserializeTypedFromArray(jp, context()));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"test\"");
        assertEquals("test", deserializer.deserializeTypedFromScalar(jp, context()));
    }

    @Test
    public void testDeserializeTypedFromAny() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("null");
        assertEquals(null, deserializer.deserializeTypedFromAny(jp, context()));
    }

    // Test for edge cases and exception handling scenarios
    @Test(expected = JsonProcessingException.class)
    public void testWrongTokenException() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":1,\"value\":\"test\"}");
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingTypeId() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"value\":\"test\"}");
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingValue() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\"}");
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingEndObject() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        jp.nextToken();
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidTypeId() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"invalidTypeId\",\"value\":\"test\"}");
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws JsonProcessingException, IOException {
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
        jp.getParserConfig().disableParsingForUnknownFields();
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseType() {
        try {
            new AsWrapperTypeDeserializer(null, idResolver, "_type", true, String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("baseType argument cannot be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeIdResolver() {
        try {
            new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), null, "_type", true, String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("typeIdResolver argument cannot be null", e.getMessage());
            throw e;
        }
    }
}