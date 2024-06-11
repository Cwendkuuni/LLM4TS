package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test class for FromStringDeserializer
 */
public class FromStringDeserializerTest {

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when the input JSON String is an empty string.
     */
    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer.Std<>(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        JsonParser parser = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertEquals(new StringBuilder(), deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when the input JSON String is null.
     */
    @Test
    public void testDeserializeNullString() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std<>(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser parser = new JsonFactory().createParser(null);
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertNull(deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.net.URL} object.
     */
    @Test
    public void testDeserializeInvalidURLFormat() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std<>(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonParser parser = new JsonFactory().createParser("{invalid_url}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(com.fasterxml.jackson.databind.exc.InvalidFormatException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing a string that is not a valid representation of a {@link java.util.Currency} object.
     */
    @Test
    public void testDeserializeInvalidCurrencyFormat() throws IOException {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std<>(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        JsonParser parser = new JsonFactory().createParser("{'XCD'}"); // Invalid currency code
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(com.fasterxml.jackson.databind.JsonMappingException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.util.Locale} object with one argument.
     */
    @Test
    public void testDeserializeInvalidLocaleFormatOneArg() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std<>(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser parser = new JsonFactory().createParser("{en}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        Locale locale = deserializer.deserialize(parser, ctxt);
        assertEquals("en", locale.getLanguage());
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.util.Locale} object with two arguments.
     */
    @Test
    public void testDeserializeInvalidLocaleFormatTwoArgs() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std<>(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser parser = new JsonFactory().createParser("{en-US}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        Locale locale = deserializer.deserialize(parser, ctxt);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.net.URI} object.
     */
    @Test
    public void testDeserializeInvalidURIFormat() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std<>(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonParser parser = new JsonFactory().createParser("{invalid_uri}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(MalformedURLException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.util.Charset} object.
     */
    @Test
    public void testDeserializeInvalidCharsetFormat() throws IOException {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std<>(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        JsonParser parser = new JsonFactory().createParser("{ISO-8859-15}"); // Invalid charset name
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(UnsupportedEncodingException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.util.TimeZone} object.
     */
    @Test
    public void testDeserializeInvalidTimeZoneFormat() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std<>(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        JsonParser parser = new JsonFactory().createParser("{InvalidTimeZone}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.net.InetAddress} object.
     */
    @Test
    public void testDeserializeInvalidInetAddressFormat() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std<>(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        JsonParser parser = new JsonFactory().createParser("{invalid_address}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(UnknownHostException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an invalid format for a {@link java.net.InetSocketAddress} object.
     */
    @Test
    public void testDeserializeInvalidInetSocketAddressFormat() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std<>(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonParser parser = new JsonFactory().createParser("{[127.0.0.1]:invalid_port}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertThrows(NumberFormatException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an empty JSON array.
     */
    @Test
    public void testDeserializeEmptyJSONArray() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std<>(Object.class, FromStringDeserializer.Std.STD_OBJECT);
        JsonParser parser = new JsonFactory().createParser("[{}]");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        assertNull(deserializer.deserialize(parser, ctxt));
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing a JSON array with multiple elements.
     */
    @Test
    public void testDeserializeJSONArrayWithMultipleElements() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std<>(Object.class, FromStringDeserializer.Std.STD_OBJECT);
        JsonParser parser = new JsonFactory().createParser("[\"String1\", 42]");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        Object[] expected = new Object[]{ "String1", 42 };
        Object[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when deserializing an embedded object of type {@link java.util.Locale}.
     */
    @Test
    public void testDeserializeEmbeddedLocaleObject() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std<>(Object.class, FromStringDeserializer.Std.STD_OBJECT);
        JsonParser parser = new JsonFactory().createParser("{\"locale\": \"en_US\"}");
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer(), new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializationConfig());
        Object result = deserializer.deserialize(parser, ctxt);
        assertEquals(new Locale("en", "US"), result);
    }
}