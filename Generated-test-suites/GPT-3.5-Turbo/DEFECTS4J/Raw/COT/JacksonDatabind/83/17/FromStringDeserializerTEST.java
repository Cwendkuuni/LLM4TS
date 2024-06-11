
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = null;
        try {
            file = (File) deserializer._deserialize("path/to/file.txt", null);
        } catch (Exception e) {}

        Assert.assertNotNull(file);
        Assert.assertEquals("path/to/file.txt", file.getPath());
    }

    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = null;
        try {
            url = (URL) deserializer._deserialize("https://example.com", null);
        } catch (Exception e) {}

        Assert.assertNotNull(url);
        Assert.assertEquals("https://example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = null;
        try {
            uri = (URI) deserializer._deserialize("https://example.com", null);
        } catch (Exception e) {}

        Assert.assertNotNull(uri);
        Assert.assertEquals("https://example.com", uri.toString());
    }

    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = null;
        try {
            clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
        } catch (Exception e) {}

        Assert.assertNotNull(clazz);
        Assert.assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_JavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = null;
        try {
            javaType = (JavaType) deserializer._deserialize("java.util.List<java.lang.String>", null);
        } catch (Exception e) {}

        Assert.assertNotNull(javaType);
        Assert.assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
    }

    @Test
    public void testDeserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = null;
        try {
            currency = (Currency) deserializer._deserialize("USD", null);
        } catch (Exception e) {}

        Assert.assertNotNull(currency);
        Assert.assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = null;
        try {
            pattern = (Pattern) deserializer._deserialize("\\d+", null);
        } catch (Exception e) {}

        Assert.assertNotNull(pattern);
        Assert.assertEquals("\\d+", pattern.pattern());
    }

    @Test
    public void testDeserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = null;
        try {
            locale = (Locale) deserializer._deserialize("en", null);
        } catch (Exception e) {}

        Assert.assertNotNull(locale);
        Assert.assertEquals(new Locale("en"), locale);
    }

    @Test
    public void testDeserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = null;
        try {
            charset = (Charset) deserializer._deserialize("UTF-8", null);
        } catch (Exception e) {}

        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = null;
        try {
            timeZone = (TimeZone) deserializer._deserialize("America/Los_Angeles", null);
        } catch (Exception e) {}

        Assert.assertNotNull(timeZone);
        Assert.assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = null;
        try {
            inetAddress = (InetAddress) deserializer._deserialize("127.0.0.1", null);
        } catch (Exception e) {}

        Assert.assertNotNull(inetAddress);
        Assert.assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserialize_InetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = null;
        try {
            inetSocketAddress = (InetSocketAddress) deserializer._deserialize("localhost:8080", null);
        } catch (Exception e) {}

        Assert.assertNotNull(inetSocketAddress);
        Assert.assertEquals("localhost", inetSocketAddress.getHostName());
        Assert.assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_StringBuilder() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder stringBuilder = null;
        try {
            stringBuilder = (StringBuilder) deserializer._deserialize("Hello, world!", null);
        } catch (Exception e) {}

        Assert.assertNotNull(stringBuilder);
        Assert.assertEquals("Hello, world!", stringBuilder.toString());
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InvalidString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = null;
        try {
            uri = (URI) deserializer._deserialize("notaurl", null);
        } catch (Exception e) {}

        Assert.assertNull(uri);
    }
}

