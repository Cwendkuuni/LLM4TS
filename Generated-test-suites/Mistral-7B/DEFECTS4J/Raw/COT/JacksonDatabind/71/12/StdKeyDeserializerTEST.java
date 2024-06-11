package com.example.test; // replace with your test package

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

/**
 * StdKeyDeserializer test file.
 */
public class StdKeyDeserializerTest {

    /**
     * Test for forType method.
     */
    @Test
    public void testForType() {
        // Test for Boolean
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertInstanceOf(StdKeyDeserializer.class, keyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, keyDeserializer._kind);

        // Test for Integer
        keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertInstanceOf(StdKeyDeserializer.class, keyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer._kind);

        // Test for String
        keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertInstanceOf(StdKeyDeserializer.StringKD.class, keyDeserializer);

        // Test for Object
        keyDeserializer = StdKeyDeserializer.forType(Object.class);
        assertInstanceOf(StdKeyDeserializer.StringKD.class, keyDeserializer);

        // Test for UUID
        keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        assertInstanceOf(StdKeyDeserializer.class, keyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_UUID, keyDeserializer._kind);

        // Test for Unsupported type
        keyDeserializer = StdKeyDeserializer.forType(Double.class);
        assertNull(keyDeserializer);
    }

    /**
     * Test for deserializeKey method.
     */
    @Test
    public void testDeserializeKey() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test for Boolean
        StdKeyDeserializer booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object key = "true";
        Object result = booleanDeserializer.deserializeKey(key.toString(), ctxt);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanDeserializer.deserializeKey(key.toString(), ctxt);
        assertEquals(Boolean.FALSE, result);

        // Test for Integer
        StdKeyDeserializer integerDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        key = "123";
        result = integerDeserializer.deserializeKey(key.toString(), ctxt);
        assertEquals(123, result);

        // Test for String
        StdKeyDeserializer stringDeserializer = new StdKeyDeserializer(-1, String.class);
        key = "test";
        result = stringDeserializer.deserializeKey(key.toString(), ctxt);
        assertEquals("test", result);

        // Test for UUID
        StdKeyDeserializer uuidDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        key = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
        result = uuidDeserializer.deserializeKey(key.toString(), ctxt);
        assertEquals(new UUID("6ba7b810-9dad-11d1-80b4-00c04fd430c8"), result);

        // Test for Unsupported type
        StdKeyDeserializer doubleDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        key = "3.14";
        try {
            doubleDeserializer.deserializeKey(key.toString(), ctxt);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for _parse method.
     */
    @Test
    public void testParse() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test for Boolean
        StdKeyDeserializer booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        Object result = booleanDeserializer._parse(key, ctxt);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanDeserializer._parse(key, ctxt);
        assertEquals(Boolean.FALSE, result);

        // Test for Integer
        StdKeyDeserializer integerDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        key = "123";
        result = integerDeserializer._parse(key, ctxt);
        assertEquals(123, result);

        // Test for String
        StdKeyDeserializer stringDeserializer = new StdKeyDeserializer(-1, String.class);
        key = "test";
        result = stringDeserializer._parse(key, ctxt);
        assertEquals("test", result);

        // Test for UUID
        StdKeyDeserializer uuidDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        key = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
        result = uuidDeserializer._parse(key, ctxt);
        assertEquals(new UUID("6ba7b810-9dad-11d1-80b4-00c04fd430c8"), result);

        // Test for Unsupported type
        StdKeyDeserializer doubleDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        key = "3.14";
        try {
            doubleDeserializer._parse(key, ctxt);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for _parseInt method.
     */
    @Test
    public void testParseInt() {
        StdKeyDeserializer standardDeserializer = new StdKeyDeserializer(-1, Object.class);
        assertEquals(0, standardDeserializer._parseInt("0"));
        assertEquals(123, standardDeserializer._parseInt("123"));
        try {
            standardDeserializer._parseInt("hello");
            assert false; // should not reach here
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for _parseLong method.
     */
    @Test
    public void testParseLong() {
        StdKeyDeserializer standardDeserializer = new StdKeyDeserializer(-1, Object.class);
        assertEquals(0, standardDeserializer._parseLong("0"));
        assertEquals(1234567890123L, standardDeserializer._parseLong("1234567890123"));
        try {
            standardDeserializer._parseLong("hello");
            assert false; // should not reach here
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for _parseDouble method.
     */
    @Test
    public void testParseDouble() {
        StdKeyDeserializer standardDeserializer = new StdKeyDeserializer(-1, Object.class);
        assertEquals(0.0, standardDeserializer._parseDouble("0"));
        assertEquals(3.14, standardDeserializer._parseDouble("3.14"));
        try {
            standardDeserializer._parseDouble("hello");
            assert false; // should not reach here
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}