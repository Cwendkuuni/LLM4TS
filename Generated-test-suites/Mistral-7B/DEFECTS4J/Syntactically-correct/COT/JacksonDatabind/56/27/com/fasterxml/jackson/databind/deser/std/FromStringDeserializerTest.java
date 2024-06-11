package com.fasterxml.jackson.databind.deser.std; // add the package name for imports

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.net.*;
import java.net.URI;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(file);
        assertEquals("testFile.txt", file.getName());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(uri);
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(mapper.parser(), mapper.context()).toString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer[].class);
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(javaType.getType());
        Object result = deserializer.deserialize(mapper.parser(), mapper.context());
        assertTrue(result instanceof Integer[]);
        assertEquals(5, ((Integer[]) result).length);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(currency);
        assertEquals("EUR", currency.getCurrencyCode());
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        deserializer.deserialize(mapper.parser(), mapper.context());
        fail("InvalidFormatException not thrown");
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(locale);
        assertEquals("en_US", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(timeZone);
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(inetAddress);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(mapper.parser(), mapper.context());
        assertNotNull(inetSocketAddress);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String text = null;
        JsonParser parser = mapper.parser();
        parser.setValue(text);
        try {
            deserializer.deserialize(parser, mapper.context());
            fail("JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            // pass
        }
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        String json = "{\"class\":\"java.lang.String\",\"value\":\"testString\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        Object result = deserializer.deserializeEmbedded(mapper.readTree(json), mapper.context());
        assertEquals("testString", result);
    }
}