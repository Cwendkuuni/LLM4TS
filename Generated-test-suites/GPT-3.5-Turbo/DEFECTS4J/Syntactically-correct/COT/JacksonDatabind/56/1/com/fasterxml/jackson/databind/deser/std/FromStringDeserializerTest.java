package com.fasterxml.jackson.databind.deser.std;

import java.io.File;
import java.io.IOException;
import java.net.*;
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
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class FromStringDeserializerTest {

    // Test public static Class<?>[] types()
    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = {
                File.class,
                URL.class,
                URI.class,
                Class.class,
                JavaType.class,
                Currency.class,
                Pattern.class,
                Locale.class,
                Charset.class,
                TimeZone.class,
                InetAddress.class,
                InetSocketAddress.class
        };
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    // Test public static Std findDeserializer(Class<?> rawType)
    @Test
    public void testFindDeserializer() {
        assertEquals(FromStringDeserializer.Std.STD_FILE, FromStringDeserializer.Std.findDeserializer(File.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_URL, FromStringDeserializer.Std.findDeserializer(URL.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_URI, FromStringDeserializer.Std.findDeserializer(URI.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_CLASS, FromStringDeserializer.Std.findDeserializer(Class.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_JAVA_TYPE, FromStringDeserializer.Std.findDeserializer(JavaType.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_CURRENCY, FromStringDeserializer.Std.findDeserializer(Currency.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_PATTERN, FromStringDeserializer.Std.findDeserializer(Pattern.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_LOCALE, FromStringDeserializer.Std.findDeserializer(Locale.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_CHARSET, FromStringDeserializer.Std.findDeserializer(Charset.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_TIME_ZONE, FromStringDeserializer.Std.findDeserializer(TimeZone.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_INET_ADDRESS, FromStringDeserializer.Std.findDeserializer(InetAddress.class).getKind());
        assertEquals(FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS, FromStringDeserializer.Std.findDeserializer(InetSocketAddress.class).getKind());
        assertNull(FromStringDeserializer.Std.findDeserializer(String.class));
    }

    // Test public T deserialize(JsonParser p, DeserializationContext ctxt)
    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer.StringDeserializer deserializer = new FromStringDeserializer.StringDeserializer();
        JsonParser parser = new FakeJsonParser("test", JsonToken.VALUE_STRING);
        DeserializationContext context = new FakeDeserializationContext();
        assertEquals("test", deserializer.deserialize(parser, context));
    }

    // Test protected T _deserialize(String value, DeserializationContext ctxt) throws IOException
    // The _deserialize method is declared as protected abstract, so it cannot be directly tested

    // Test protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException
    // The _deserializeEmbedded method is declared as protected, so it cannot be directly tested

    // Test protected T _deserializeFromEmptyString() throws IOException
    // The _deserializeFromEmptyString method is declared as protected, so it cannot be directly tested

    // Test public static class Std
    @Test
    public void testStd() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(String.class, 0);
        assertEquals(0, std.getKind());
        JsonParser parser = new FakeJsonParser("test", JsonToken.VALUE_STRING);
        DeserializationContext context = new FakeDeserializationContext();
        assertEquals("test", std.deserialize(parser, context));
        std = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(FromStringDeserializer.Std.STD_FILE, std.getKind());
        assertEquals(File.class, std.getValueClass());
        assertEquals(new File("test"), std.deserialize("test", context));
        std = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(FromStringDeserializer.Std.STD_URL, std.getKind());
        assertEquals(URL.class, std.getValueClass());
        assertEquals(new URL("http://www.example.com"), std.deserialize("http://www.example.com", context));
        std = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(FromStringDeserializer.Std.STD_URI, std.getKind());
        assertEquals(URI.class, std.getValueClass());
        assertEquals(URI.create("http://www.example.com"), std.deserialize("http://www.example.com", context));
        std = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(FromStringDeserializer.Std.STD_CLASS, std.getKind());
        assertEquals(Class.class, std.getValueClass());
        assertEquals(File.class, std.deserialize("java.io.File", context));
        std = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(FromStringDeserializer.Std.STD_JAVA_TYPE, std.getKind());
        assertEquals(JavaType.class, std.getValueClass());
        assertEquals(JavaType.class, std.deserialize("java.util.List<java.lang.String>", context));
        std = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(FromStringDeserializer.Std.STD_CURRENCY, std.getKind());
        assertEquals(Currency.class, std.getValueClass());
        assertEquals(Currency.getInstance("USD"), std.deserialize("USD", context));
        std = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(FromStringDeserializer.Std.STD_PATTERN, std.getKind());
        assertEquals(Pattern.class, std.getValueClass());
        assertEquals(Pattern.compile("[a-z]+"), std.deserialize("[a-z]+", context));
        std = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(FromStringDeserializer.Std.STD_LOCALE, std.getKind());
        assertEquals(Locale.class, std.getValueClass());
        assertEquals(Locale.US, std.deserialize("en_US", context));
        std = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(FromStringDeserializer.Std.STD_CHARSET, std.getKind());
        assertEquals(Charset.class, std.getValueClass());
        assertEquals(Charset.forName("UTF-8"), std.deserialize("UTF-8", context));
        std = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(FromStringDeserializer.Std.STD_TIME_ZONE, std.getKind());
        assertEquals(TimeZone.class, std.getValueClass());
        assertEquals(TimeZone.getTimeZone("GMT"), std.deserialize("GMT", context));
        std = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(FromStringDeserializer.Std.STD_INET_ADDRESS, std.getKind());
        assertEquals(InetAddress.class, std.getValueClass());
        assertEquals(InetAddress.getByName("172.217.164.110"), std.deserialize("172.217.164.110", context));
        std = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS, std.getKind());
        assertEquals(InetSocketAddress.class, std.getValueClass());
        assertEquals(new InetSocketAddress("localhost", 8080), std.deserialize("localhost:8080", context));
    }

    // Test protected T _deserializeFromEmptyString() throws IOException
    @Test
    public void test_DeserializeFromEmptyString() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), std.deserialize("", new FakeDeserializationContext()));
        std = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.ROOT, std.deserialize("", new FakeDeserializationContext()));
    }
}