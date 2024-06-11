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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(File.class, 1);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        File result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URL.class, 2);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        URL result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, 3);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        URI result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Class.class, 4);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Class result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(JavaType.class, 5);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Currency.class, 6);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Currency result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Pattern.class, 7);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Pattern result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Locale.class, 8);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Locale result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Charset.class, 9);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Charset result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(TimeZone.class, 10);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TimeZone result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(InetAddress.class, 11);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        InetAddress result = std.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        InetSocketAddress result = std.deserialize(jp, ctxt);
        assertNull(result);
    }
}