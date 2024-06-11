package com.fasterxml.jackson.databind.deser.std;

import java.io.File;
import java.io.IOException;
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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {
    
    private FromStringDeserializer<Object> deserializer;
    private DeserializationContext context;
    private JsonParser parser;
    
    @Before
    public void setup() {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);
        context = new DeserializationContext(null, null, null);
        parser = new JsonParser(null, 0, null, null, null);
    }

    @Test
    public void testDeserialize_File() throws IOException {
        File file = (File) deserializer.deserialize(parser, context);
        assertNull(file);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        URL url = (URL) deserializer.deserialize(parser, context);
        assertNull(url);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        URI uri = (URI) deserializer.deserialize(parser, context);
        assertNull(uri);
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        Class<?> clazz = (Class<?>) deserializer.deserialize(parser, context);
        assertNull(clazz);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        JavaType javaType = (JavaType) deserializer.deserialize(parser, context);
        assertNull(javaType);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        Currency currency = (Currency) deserializer.deserialize(parser, context);
        assertNull(currency);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        Pattern pattern = (Pattern) deserializer.deserialize(parser, context);
        assertNull(pattern);
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        Locale locale = (Locale) deserializer.deserialize(parser, context);
        assertNull(locale);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        Charset charset = (Charset) deserializer.deserialize(parser, context);
        assertNull(charset);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        TimeZone timeZone = (TimeZone) deserializer.deserialize(parser, context);
        assertNull(timeZone);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(parser, context);
        assertNull(inetAddress);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(parser, context);
        assertNull(inetSocketAddress);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_UnknownType() throws IOException {
        deserializer.deserialize(parser, context);
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_EmptyString_URI() throws IOException {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        stdDeserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserialize_EmptyString_Locale() throws IOException {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) stdDeserializer.deserialize(parser, context);
        assertEquals(Locale.ROOT, locale);
    }

    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        Object result = deserializer._deserializeFromEmptyString();
        assertNull(result);
    }
}