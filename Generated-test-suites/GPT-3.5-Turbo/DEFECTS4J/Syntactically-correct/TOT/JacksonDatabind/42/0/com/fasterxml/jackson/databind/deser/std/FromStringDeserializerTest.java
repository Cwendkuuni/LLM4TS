package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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

import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        File expected = new File("test.txt");
        File result = (File) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        URL expected = new URL("http://www.example.com");
        URL result = (URL) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        URI expected = URI.create("http://www.example.com");
        URI result = (URI) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        JsonParser jp = null; // create json parser with mock data
        MyDeserializationContext ctxt = null; // create deserialization context with mock data

        Class<?> expected = Class.class;
        Class<?> result = (Class<?>) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        JavaType expected = JavaType.class;
        JavaType result = (JavaType) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        Currency expected = Currency.getInstance("USD");
        Currency result = (Currency) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        Pattern expected = Pattern.compile("[0-9]+");
        Pattern result = (Pattern) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        Locale expected = new Locale("en", "US");
        Locale result = (Locale) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        Charset expected = Charset.forName("UTF-8");
        Charset result = (Charset) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone result = (TimeZone) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        InetAddress expected = InetAddress.getLocalHost();
        InetAddress result = (InetAddress) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_invalidType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InvalidType.class, 13);
        JsonParser jp = null; // create json parser with mock data
        DeserializationContext ctxt = null; // create deserialization context with mock data

        deserializer.deserialize(jp, ctxt);
    }

    public static class InvalidType {
    }
}