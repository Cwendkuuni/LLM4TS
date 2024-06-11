package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std fromStringDeserializer;

    @Before
    public void setUp() {
        fromStringDeserializer = new FromStringDeserializer.Std(String.class, 0);
    }

    @Test
    public void testDeserialize_file() throws IOException {
        File file = (File) fromStringDeserializer._deserialize("testFile.txt", null);
        assertNotNull(file);
        assertEquals("testFile.txt", file.getName());
    }

    @Test
    public void testDeserialize_url() throws IOException {
        URL url = (URL) fromStringDeserializer._deserialize("http://www.google.com", null);
        assertNotNull(url);
        assertEquals("http://www.google.com", url.toString());
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        URI uri = (URI) fromStringDeserializer._deserialize("https://www.google.com", null);
        assertNotNull(uri);
        assertEquals("https://www.google.com", uri.toString());
    }

    @Test
    public void testDeserialize_class() throws IOException {
        Class<?> cls = (Class<?>) fromStringDeserializer._deserialize("com.example.Class", null);
        assertNotNull(cls);
        assertEquals("com.example.Class", cls.getName());
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        // Not sure how to test this as JavaType is an abstract class
        // However, the _deserialize method will return null instead of throwing an exception
        assertNull(fromStringDeserializer._deserialize("java.util.List", null));
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        Currency currency = (Currency) fromStringDeserializer._deserialize("USD", null);
        assertNotNull(currency);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        Pattern pattern = (Pattern) fromStringDeserializer._deserialize("[a-z]+", null);
        assertNotNull(pattern);
        assertEquals("[a-z]+", pattern.pattern());
    }

    @Test
    public void testDeserialize_locale_simple() throws IOException {
        Locale locale = (Locale) fromStringDeserializer._deserialize("en", null);
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void testDeserialize_locale_withCountry() throws IOException {
        Locale locale = (Locale) fromStringDeserializer._deserialize("en_US", null);
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserialize_locale_withCountryAndVariant() throws IOException {
        Locale locale = (Locale) fromStringDeserializer._deserialize("en_US_POSIX", null);
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX", locale.getVariant());
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        Charset charset = (Charset) fromStringDeserializer._deserialize("UTF-8", null);
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        TimeZone timeZone = (TimeZone) fromStringDeserializer._deserialize("UTC", null);
        assertNotNull(timeZone);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        InetAddress inetAddress = (InetAddress) fromStringDeserializer._deserialize("127.0.0.1", null);
        assertNotNull(inetAddress);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) fromStringDeserializer._deserialize("[::1]:8080", null);
        assertNotNull(inetSocketAddress);
        assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

}