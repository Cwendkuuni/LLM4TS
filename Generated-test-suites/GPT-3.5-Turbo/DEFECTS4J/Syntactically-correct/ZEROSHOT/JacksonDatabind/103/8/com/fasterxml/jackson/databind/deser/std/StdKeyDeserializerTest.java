package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        try {
            Object result = deser.deserializeKey("5", ctxt);
            assertEquals(5, result);
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeKeyNull() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        try {
            Object result = deser.deserializeKey(null, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeKeyException() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        try {
            Object result = deser.deserializeKey("notANumber", ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Class<?> keyClass = deser.getKeyClass();
        assertEquals(Integer.class, keyClass);
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        try {
            int value = deser._parseInt("10");
            assertEquals(10, value);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        try {
            long value = deser._parseLong("10000");
            assertEquals(10000, value);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        try {
            double value = deser._parseDouble("3.14");
            assertEquals(3.14, value, 0.01);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testWeirdKey() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        try {
            Object result = deser._weirdKey(ctxt, "notANumber", new Exception("Invalid key"));
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
    }

}