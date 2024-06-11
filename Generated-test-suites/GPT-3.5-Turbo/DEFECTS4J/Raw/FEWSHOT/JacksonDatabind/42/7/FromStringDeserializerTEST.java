package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        File result = (File) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof File);
    }
    
    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL result = (URL) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof URI);
    }

    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class<?> result = (Class<?>) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof Class<?>);
    }
    
    @Test
    public void testDeserialize_JavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType result = (JavaType) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof JavaType);
    }

    @Test
    public void testDeserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency result = (Currency) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof Currency);
    }
    
    @Test
    public void testDeserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        Pattern result = (Pattern) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof Pattern);
    }
    
    @Test
    public void testDeserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale result = (Locale) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof Locale);
    }

    @Test
    public void testDeserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset result = (Charset) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof Charset);
    }
    
    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof TimeZone);
    }

    @Test
    public void testDeserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        InetAddress result = (InetAddress) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof InetAddress);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(jsonParser, deserializationContext);
        assertTrue(result instanceof InetSocketAddress);
    }
}