package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = null;
        assertEquals(new File("test.txt"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = null;
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = null;
        assertEquals(new URI("http://www.example.com"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_Class() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Class.class);
        DeserializationContext ctxt = null;
        assertEquals(String.class, deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = null;
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = null;
        assertEquals(Pattern.compile("\\d+"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_Locale() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = null;
        assertEquals(Locale.US, deserializer.deserialize(null, ctxt));
        assertEquals(new Locale("en", "US"), deserializer.deserialize(null, ctxt));
        assertEquals(new Locale("en", "US", "NewYork"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = null;
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = null;
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = null;
        assertEquals(InetAddress.getByName("www.example.com"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = null;
        assertEquals(new InetSocketAddress("www.example.com", 8080), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_StringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(StringBuilder.class);
        DeserializationContext ctxt = null;
        assertEquals(new StringBuilder("test"), deserializer.deserialize(null, ctxt));
    }

    @Test
    public void testDeserialize_EmbeddedObjectNull() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = null;
        assertNull(deserializer.deserialize(null, ctxt));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmbeddedObjectUnsupportedType() throws Exception {
        FromStringDeserializer.Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = null;
        deserializer.deserialize(null, ctxt);
    }
}