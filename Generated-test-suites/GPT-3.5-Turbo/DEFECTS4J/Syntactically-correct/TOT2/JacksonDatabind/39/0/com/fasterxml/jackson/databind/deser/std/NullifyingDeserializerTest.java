package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        try {
            assertNull(deserializer.deserialize(null, null));
        } catch (Exception e) {
            fail("Exception thrown during deserialization test");
        }
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        try {
            assertNull(deserializer.deserializeWithType(null, null, null));
        } catch (Exception e) {
            fail("Exception thrown during deserializeWithType test");
        }
    }

    @Test
    public void testEdgeCaseDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonFactory().createParser("{}"); // Empty JSON object
        DeserializationContext ctxt = new DefaultDeserializationContext.Builder(
                new ObjectMapper(), DeserializationConfig.emptyStdConfig(), new NullNode())
            .build();
        
        try {
            assertNull(deserializer.deserialize(p, ctxt));
        } catch (Exception e) {
            fail("Exception thrown during edge case deserialize test");
        }
    }

    @Test
    public void testEdgeCaseDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser p = new JsonFactory().createParser("{}"); // Empty JSON object
        DeserializationContext ctxt = new DefaultDeserializationContext.Builder(
                new ObjectMapper(), DeserializationConfig.emptyStdConfig(), new NullNode())
            .build();
        TypeDeserializer typeDeserializer = TypeDeserializer.instantiate(new DefaultBaseTypeParser(p), new DefaultDeserializationContext.Builder(
                new ObjectMapper(), DeserializationConfig.emptyStdConfig(), new NullNode())
            .build());
        
        try {
            assertNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
        } catch (Exception e) {
            fail("Exception thrown during edge case deserializeWithType test");
        }
    }

    @Test
    public void testExceptionHandling() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        // Testing exception handling for null JsonParser
        try {
            assertNull(deserializer.deserialize(null, null));
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }

        // Testing exception handling for null JsonParser and TypeDeserializer
        try {
            assertNull(deserializer.deserializeWithType(null, null, null));
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }

    }
}