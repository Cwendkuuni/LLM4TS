package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for StdKeyDeserializer
 */
public class StdKeyDeserializerTest {

    private StdKeyDeserializer _des;

    @Before
    public void setup() {
        _des = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey_validString() throws IOException {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Object result = _des.deserializeKey(key, ctxt);
        assertNotNull(result);
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_nullKey() throws IOException {
        String key = null;
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Object result = _des.deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_invalidString() throws IOException {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null) {
            @Override
            public Object handleWeirdKey(Class<?> keyClass, String keyValue, String msg, Object... msgArgs) {
                throw new IllegalArgumentException("Invalid key");
            }
        };
        try {
            _des.deserializeKey(key, ctxt);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid key", e.getMessage());
        }
    }

}