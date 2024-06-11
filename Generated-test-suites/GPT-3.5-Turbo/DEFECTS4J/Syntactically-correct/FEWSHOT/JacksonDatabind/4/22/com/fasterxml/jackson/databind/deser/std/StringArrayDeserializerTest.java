package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    private final StringArrayDeserializer deserializer = new StringArrayDeserializer();

    // Test deserialize method
    @Test
    public void testDeserialize() {
        // Test when parser does not point to start array
        JsonParser jp = new JsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        try {
            deserializer.deserialize(jp, ctxt);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    // Test handleNonArray method
    @Test
    public void testHandleNonArray() {
        JsonParser jp = new JsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        try {
            deserializer.handleNonArray(jp, ctxt);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp2 = new JsonFactory().createParser("null");
        try {
            deserializer.handleNonArray(jp2, ctxt);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp3 = new JsonFactory().createParser("\"\"");
        try {
            deserializer.handleNonArray(jp3, ctxt);
            fail("Expected JsonMappingException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp4 = new JsonFactory().createParser("\"test\"");
        try {
            String[] result = deserializer.handleNonArray(jp4, ctxt);
            assertNotNull(result);
            assertEquals(1, result.length);
            assertNull(result[0]);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test _deserializeCustom method
    @Test
    public void testDeserializeCustom() {
        JsonParser jp = new JsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        try {
            deserializer._deserializeCustom(jp, ctxt);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp2 = new JsonFactory().createParser("null");
        try {
            deserializer._deserializeCustom(jp2, ctxt);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp3 = new JsonFactory().createParser("\"\"");
        try {
            deserializer._deserializeCustom(jp3, ctxt);
            fail("Expected JsonMappingException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }

        JsonParser jp4 = new JsonFactory().createParser("\"test\"");
        try {
            deserializer._deserializeCustom(jp4, ctxt);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test deserializeWithType method
    @Test
    public void testDeserializeWithType() {
        JsonParser jp = new JsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = null;
        try {
            deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    // Test createContextual method
    @Test
    public void testCreateContextual() {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        BeanProperty property = null;
        try {
            deserializer.createContextual(ctxt, property);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
}