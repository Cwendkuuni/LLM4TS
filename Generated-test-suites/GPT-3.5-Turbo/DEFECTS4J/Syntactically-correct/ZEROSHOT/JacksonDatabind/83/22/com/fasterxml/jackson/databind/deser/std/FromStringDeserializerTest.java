package com.fasterxml.jackson.databind.deser.std;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);

        File result = (File) deserializer._deserialize("test/path", null);

        assertEquals("test/path", result.getPath());
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(deserializer);

        URL result = (URL) deserializer._deserialize("http://www.example.com", null);

        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(deserializer);

        URI result = (URI) deserializer._deserialize("http://www.example.com", null);

        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetAddress.class);
        assertNotNull(deserializer);

        InetAddress result = (InetAddress) deserializer._deserialize("localhost", null);

        assertEquals("localhost", result.getHostName());
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertNotNull(deserializer);

        InetSocketAddress result = (InetSocketAddress) deserializer._deserialize("localhost:8080", null);

        assertEquals("localhost", result.getHostName());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(deserializer);

        Currency result = (Currency) deserializer._deserialize("USD", null);

        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Pattern.class);
        assertNotNull(deserializer);

        Pattern result = (Pattern) deserializer._deserialize("\\d+", null);

        assertTrue(result.matcher("123").matches());
    }

    @Test
    public void testDeserialize_Locale() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Locale.class);
        assertNotNull(deserializer);

        Locale result = (Locale) deserializer._deserialize("en_US", null);

        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Charset.class);
        assertNotNull(deserializer);

        Charset result = (Charset) deserializer._deserialize("UTF-8", null);

        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        assertNotNull(deserializer);

        TimeZone result = (TimeZone) deserializer._deserialize("GMT-8:00", null);

        assertEquals("GMT-8:00", result.getID());
    }

    @Test
    public void testDeserialize_StringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertNotNull(deserializer);

        StringBuilder result = (StringBuilder) deserializer._deserialize("test", null);

        assertEquals("test", result.toString());
    }
}