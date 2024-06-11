package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    // Test case for deserialize method with valid String input
    @Test
    public void testDeserializeValidStringInput() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"testString\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        String result = deserializer.deserialize(parser, context);
        assertEquals("testString", result);
    }

    // Test case for deserialize method with empty String input
    @Test
    public void testDeserializeEmptyStringInput() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        String result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserialize method with null input
    @Test
    public void testDeserializeNullInput() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": null}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        String result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserialize method with invalid String input
    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringInput() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": 123}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        deserializer.deserialize(parser, context);
    }

    // Test case for deserializeFromArray method
    @Test
    public void testDeserializeFromArray() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[\"" + "testArrayElement1" + "\", \"" + "testArrayElement2" + "\"]");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String[]> deserializer = new FromStringDeserializer<String[]>(String[].class);
        String[] result = deserializer.deserialize(parser, context);
        String[] expected = new String[]{"testArrayElement1", "testArrayElement2"};
        assertArrayEquals(expected, result);
    }

    // Test case for deserializeEmbedded method with null input
    @Test
    public void testDeserializeEmbeddedNullInput() throws IOException {
        Object obj = new Object();
        JsonParser parser = new JsonFactory().createParser("{\"embeddedObject\": " + obj.toString() + "}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserializeEmbedded(obj, context);
        assertNull(result);
    }

    // Test case for deserializeEmbedded method with valid String input
    @Test
    public void testDeserializeEmbeddedValidStringInput() throws IOException {
        Object obj = new StringBuilder("testString");
        JsonParser parser = new JsonFactory().createParser("{\"embeddedObject\": \"" + obj.toString() + "\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserializeEmbedded(obj, context);
        assertEquals(obj.toString(), result.toString());
    }

    // Test case for deserializeFromEmptyString method for File type
    @Test
    public void testDeserializeFromEmptyStringFileType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        File result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserializeFromEmptyString method for URI type
    @Test
    public void testDeserializeFromEmptyStringURIType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        URI result = deserializer.deserialize(parser, context);
        assertEquals(URI.class, result.getClass());
        assertTrue(result.isOpaque());
    }

    // Test case for deserializeFromEmptyString method for Locale type
    @Test
    public void testDeserializeFromEmptyStringLocaleType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale result = deserializer.deserialize(parser, context);
        assertEquals(Locale.ROOT, result);
    }

    // Test case for deserializeFromEmptyString method for TimeZone type
    @Test
    public void testDeserializeFromEmptyStringTimeZoneType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserializeFromEmptyString method for InetAddress type
    @Test
    public void testDeserializeFromEmptyStringInetAddressType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserializeFromEmptyString method for InetSocketAddress type
    @Test
    public void testDeserializeFromEmptyStringInetSocketAddressType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    // Test case for deserializeFromEmptyString method for StringBuilder type
    @Test
    public void testDeserializeFromEmptyStringStringBuilderType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": \"\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(parser, context);
        assertEquals("", result.toString());
    }

    // Test case for deserializeFromArray method with invalid input
    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromArrayInvalidInput() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"value\": [1, 2, 3]}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<String[]> deserializer = new FromStringDeserializer<String[]>(String[].class);
        deserializer.deserializeFromArray(parser, context);
    }

    // Test case for deserializeEmbedded method with invalid input
    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedInvalidInput() throws IOException {
        Object obj = new Object();
        JsonParser parser = new JsonFactory().createParser("{\"embeddedObject\": 123}");
        DeserializationContext context = mapper.getDeserializationContext();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserializeEmbedded(obj, context);
    }
}