package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer.deserialize(null, null);
        assertNull(file);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer.deserialize(null, null);
        assertNull(url);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer.deserialize(null, null);
        assertNull(uri);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        String className = "java.lang.String";
        Class<?> clazz = (Class<?>) deserializer._deserialize(className, null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) deserializer.deserialize(null, null);
        assertNull(javaType);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) deserializer.deserialize(null, null);
        assertNull(pattern);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer.deserialize(null, null);
        assertNull(locale);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer.deserialize(null, null);
        assertNull(charset);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(null, null);
        assertNull(timeZone);
    }

    @Test
    public void testDeserialize_INET_address() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(null, null);
        assertNull(inetAddress);
    }

    @Test
    public void testDeserialize_INET_socket_address() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress socketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
        assertNull(socketAddress);
    }

    @Test
    public void testDeserialize_string_builder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(null, null);
        assertNull(stringBuilder);
    }

    @Test
    public void testDeserialize_nullValue() throws IOException {
        FromStringDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
    }

}