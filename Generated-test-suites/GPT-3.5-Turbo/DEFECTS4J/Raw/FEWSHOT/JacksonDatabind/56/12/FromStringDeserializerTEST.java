package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer(null) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testDeserializeWithStringValueAsString() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        assertNull(deserializer.deserialize(jsonParser, deserializationContext, "value"));
    }

    @Test
    public void testDeserializeWithStringValueAsEmptyString() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        assertNull(deserializer.deserialize(jsonParser, deserializationContext, ""));
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeWithStringValueAsInvalidFormat() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        deserializer.deserialize(jsonParser, deserializationContext, "invalid");
    }

    @Test
    public void testDeserializeFileFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "/path/to/file");

        assertTrue(result instanceof File);
    }

    @Test
    public void testDeserializeURLFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "http://www.example.com");

        assertTrue(result instanceof URL);
    }

    @Test
    public void testDeserializeURIFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "http://www.example.com");

        assertTrue(result instanceof URI);
    }

    @Test
    public void testDeserializeClassFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "java.lang.String");

        assertTrue(result instanceof Class);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaTypeFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "java.util.List");

        assertTrue(result instanceof JavaType);
        assertEquals("java.util.List", result.toString());
    }

    @Test
    public void testDeserializeCurrencyFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "USD");

        assertTrue(result instanceof Currency);
        assertEquals("USD", result.toString());
    }

    @Test
    public void testDeserializePatternFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "\\d+");

        assertTrue(result instanceof Pattern);
        assertEquals("\\d+", result.toString());
    }

    @Test
    public void testDeserializeLocaleFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "en_US");

        assertTrue(result instanceof Locale);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharsetFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "UTF-8");

        assertTrue(result instanceof Charset);
        assertEquals("UTF-8", result.toString());
    }

    @Test
    public void testDeserializeTimeZoneFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "America/Los_Angeles");

        assertTrue(result instanceof TimeZone);
        assertEquals("America/Los_Angeles", result.toString());
    }

    @Test
    public void testDeserializeInetAddressFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "127.0.0.1");

        assertTrue(result instanceof InetAddress);
        assertEquals("localhost/127.0.0.1", result.toString());
    }

    @Test
    public void testDeserializeInetSocketAddressFromString() throws IOException {
        deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        Object result = deserializer.deserialize(jsonParser, deserializationContext, "localhost:80");

        assertTrue(result instanceof InetSocketAddress);

        InetSocketAddress address = (InetSocketAddress) result;
        assertEquals("localhost", address.getHostString());
        assertEquals(80, address.getPort());
    }
}