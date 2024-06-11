package com.fasterxml.jackson.databind.deser.std;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer<Object> fromStringDeserializer;

    @BeforeEach
    public void setUp() {
        fromStringDeserializer = new FromStringDeserializer.Std(String.class, 1);
    }

    @Test
    public void testDeserializeWithStringValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"test\"");
        DeserializationContext ctxt = new JsonFactory().createCodec().getDeserializationContext();
        Object result = fromStringDeserializer.deserialize(jp, ctxt);
        assertEquals(String.class, result.getClass());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = new JsonFactory().createCodec().getDeserializationContext();
        Object result = fromStringDeserializer.deserialize(jp, ctxt);
        assertEquals(String.class, result.getClass());
        assertEquals("", result);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("null");
        DeserializationContext ctxt = new JsonFactory().createCodec().getDeserializationContext();
        Object result = fromStringDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithInvalidValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"123abc\"");
        DeserializationContext ctxt = new JsonFactory().createCodec().getDeserializationContext();
        assertThrows(JsonMappingException.class, () -> fromStringDeserializer.deserialize(jp, ctxt));
    }
}