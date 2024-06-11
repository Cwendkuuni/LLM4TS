package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
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

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std std;

    @Before
    public void setUp() throws Exception {
        std = new FromStringDeserializer.Std(String.class, 1);
    }

    @Test
    public void types() {
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
                InetSocketAddress.class,
        };
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test
    public void findDeserializer() {
        assertEquals(std, FromStringDeserializer.findDeserializer(File.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(URL.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(URI.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(Class.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(JavaType.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(Currency.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(Pattern.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(Locale.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(Charset.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(TimeZone.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(InetAddress.class));
        assertEquals(std, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        assertNull(FromStringDeserializer.findDeserializer(String.class));
    }

    @Test
    public void deserialize() throws IOException {
        JsonParser parser = new JsonFactory().createParser("\"test\"");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertEquals("test", std.deserialize(parser, deserializationContext));
    }

    @Test
    public void deserialize_unwrapSingleValueArrays() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertEquals("test", std.deserialize(parser, deserializationContext));
    }

    @Test
    public void deserialize_withEmptyString() throws IOException {
        JsonParser parser = new JsonFactory().createParser("\"\"");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertNull(std.deserialize(parser, deserializationContext));
    }

    @Test(expected = JsonMappingException.class)
    public void deserialize_withInvalidString() throws IOException {
        JsonParser parser = new JsonFactory().createParser("\"invalid\"");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        std.deserialize(parser, deserializationContext);
    }

    @Test
    public void deserialize_withEmbeddedObject() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"name\":\"test\"}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertEquals("{\"name\":\"test\"}", std.deserialize(parser, deserializationContext));
    }

    @Test(expected = JsonMappingException.class)
    public void deserialize_withUnsupportedType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        std.deserialize(parser, deserializationContext);
    }

    @Test
    public void _deserialize() throws IOException {
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertEquals(new File("test"), std._deserialize("test", deserializationContext));
    }

    @Test
    public void _deserializeEmbedded() throws IOException {
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        assertThrows(JsonMappingException.class, () -> std._deserializeEmbedded(new Object(), deserializationContext));
    }

    @Test
    public void _deserializeFromEmptyString() throws IOException {
        assertNull(std._deserializeFromEmptyString());
    }
}