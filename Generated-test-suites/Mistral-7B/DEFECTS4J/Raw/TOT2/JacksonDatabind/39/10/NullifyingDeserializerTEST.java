package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test for deserialize method when provided with JsonParser.
     */
    @Test
    public void testDeserializeWithJsonParser() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, new DeserializationContext(null)));
    }

    /**
     * Test for deserializeWithType method when provided with JsonParser, DeserializationContext and TypeDeserializer.
     */
    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, new DeserializationContext(null), new TypeDeserializer() {}));
    }
}