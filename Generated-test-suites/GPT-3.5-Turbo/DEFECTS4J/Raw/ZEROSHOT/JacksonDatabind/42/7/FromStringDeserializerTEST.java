package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.Before;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer<Object> deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);
    }

    @Test
    public void testDeserialize_withValidStringInput() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"test\"");
        DeserializationContext ctxt = null;
        Object result = deserializer.deserialize(jp, ctxt);
        assertEquals("test", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_withEmptyStringInput() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = null;
        deserializer.deserialize(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_withNullInput() throws IOException {
        JsonParser jp = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        deserializer.deserialize(jp, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_withInvalidStringInput() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"invalid\"");
        DeserializationContext ctxt = null;
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void test_deserialize_from_empty_string_with_std_uri() throws IOException {
        deserializer = new FromStringDeserializer.Std(URI.class, 3);
        JsonParser jp = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = null;
        Object result = deserializer.deserialize(jp, ctxt);
        assertEquals(URI.create(""), result);
    }

}