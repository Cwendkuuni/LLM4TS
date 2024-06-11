package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

public class StdKeyDeserializerTest {
    
    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @Test
    public void testDeserializeKey_success() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Integer expected = 10;
        assertEquals(expected, stdKeyDeserializer._parse("10", ctxt));
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_throwIOException() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        stdKeyDeserializer._parse("x", ctxt);
    }

    @Test
    public void testDeserializeKey_nullStringValue_returnNull() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        assertNull(stdKeyDeserializer._parse(null, ctxt));
    }

    @Test
    public void testDeserializeKey_nullValue_returnNull() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        assertNull(stdKeyDeserializer._parse("10", null));
    }

    @Test
    public void testDeserializeKey_invalidIntValue_throwException() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            stdKeyDeserializer._parse("10L", ctxt);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("For input string: \"10L\"", e.getMessage());
        }
    }

    @Test
    public void testDeserializeKey_maxIntValue_success() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Integer expected = Integer.MAX_VALUE;
        assertEquals(expected, stdKeyDeserializer._parse("2147483647", ctxt));
    }

    @Test
    public void testDeserializeKey_minIntValue_success() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Integer expected = Integer.MIN_VALUE;
        assertEquals(expected, stdKeyDeserializer._parse("-2147483648", ctxt));
    }
}