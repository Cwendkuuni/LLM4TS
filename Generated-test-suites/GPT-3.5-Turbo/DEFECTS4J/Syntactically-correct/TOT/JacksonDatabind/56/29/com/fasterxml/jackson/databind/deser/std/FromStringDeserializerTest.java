package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    // Test case for deserialize method
    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 1);
        JsonParser parser = null;
        DeserializationContext ctxt = null;
        assertNull(deserializer.deserialize(parser, ctxt));
    }

    // Test case for _deserialize method with unknown kind
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_UnknownKind() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 13);
        DeserializationContext ctxt = null;
        deserializer._deserialize("test", ctxt);
    }

    // Test case for _deserialize method with STD_FILE kind
    @Test
    public void testDeserialize_STD_FILE() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 1);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("test", ctxt);
        assertTrue(result instanceof java.io.File);
    }

    // Test case for _deserialize method with STD_URL kind
    @Test
    public void testDeserialize_STD_URL() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 2);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("http://www.example.com", ctxt);
        assertTrue(result instanceof java.net.URL);
    }

    // Test case for _deserialize method with STD_URI kind
    @Test
    public void testDeserialize_STD_URI() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 3);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("http://www.example.com", ctxt);
        assertTrue(result instanceof java.net.URI);
    }

    // Test case for _deserialize method with STD_CLASS kind
    @Test(expected = ClassNotFoundException.class)
    public void testDeserialize_STD_CLASS() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 4);
        DeserializationContext ctxt = null;
        deserializer._deserialize("com.example.TestClass", ctxt);
    }

    // Test case for _deserialize method with STD_JAVA_TYPE kind
    @Test
    public void testDeserialize_STD_JAVA_TYPE() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 5);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("java.util.HashMap", ctxt);
        assertTrue(result instanceof com.fasterxml.jackson.databind.JavaType);
    }

    // Test case for _deserialize method with STD_CURRENCY kind
    @Test
    public void testDeserialize_STD_CURRENCY() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 6);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("USD", ctxt);
        assertTrue(result instanceof java.util.Currency);
    }

    // Test case for _deserialize method with STD_PATTERN kind
    @Test
    public void testDeserialize_STD_PATTERN() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 7);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("[a-z]", ctxt);
        assertTrue(result instanceof java.util.regex.Pattern);
    }

    // Test case for _deserialize method with STD_LOCALE kind
    @Test
    public void testDeserialize_STD_LOCALE() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 8);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("en_US", ctxt);
        assertTrue(result instanceof java.util.Locale);
    }

    // Test case for _deserialize method with STD_CHARSET kind
    @Test
    public void testDeserialize_STD_CHARSET() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 9);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("UTF-8", ctxt);
        assertTrue(result instanceof java.nio.charset.Charset);
    }

    // Test case for _deserialize method with STD_TIME_ZONE kind
    @Test
    public void testDeserialize_STD_TIME_ZONE() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 10);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("America/New_York", ctxt);
        assertTrue(result instanceof java.util.TimeZone);
    }

    // Test case for _deserialize method with STD_INET_ADDRESS kind
    @Test
    public void testDeserialize_STD_INET_ADDRESS() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 11);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("127.0.0.1", ctxt);
        assertTrue(result instanceof java.net.InetAddress);
    }

    // Test case for _deserialize method with STD_INET_SOCKET_ADDRESS kind
    @Test
    public void testDeserialize_STD_INET_SOCKET_ADDRESS() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 12);
        DeserializationContext ctxt = null;
        Object result = deserializer._deserialize("localhost:8080", ctxt);
        assertTrue(result instanceof java.net.InetSocketAddress);
    }
    
    // Test case for _deserialize method with STD_INET_SOCKET_ADDRESS kind and invalid input
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_STD_INET_SOCKET_ADDRESS_InvalidInput() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 12);
        DeserializationContext ctxt = null;
        deserializer._deserialize("localhost:invalid", ctxt);
    }

    // Test case for _deserializeEmbedded method with unknown object type
    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void testDeserializeEmbedded_UnknownType() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 1);
        DeserializationContext ctxt = null;
        deserializer._deserializeEmbedded(new Object(), ctxt);
    }

    // Test case for _deserializeFromEmptyString method with STD_URI kind
    @Test
    public void testDeserializeFromEmptyString_STD_URI() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 3);
        Object result = deserializer._deserializeFromEmptyString();
        assertTrue(result instanceof java.net.URI);
        assertEquals("", result.toString());
    }

    // Test case for _deserializeFromEmptyString method with STD_LOCALE kind
    @Test
    public void testDeserializeFromEmptyString_STD_LOCALE() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 8);
        Object result = deserializer._deserializeFromEmptyString();
        assertTrue(result instanceof java.util.Locale);
        assertEquals(Locale.ROOT, result);
    }

    // Test case for _deserializeFromEmptyString method with other kinds
    @Test
    public void testDeserializeFromEmptyString_OtherKinds() throws IOException {
        FromStringDeserializer<Object>.Std deserializer = new FromStringDeserializer.Std(Object.class, 1);
        assertNull(deserializer._deserializeFromEmptyString());
    }

}