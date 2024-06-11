package com.example.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"/path/to/file.txt\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"http://example.com/path\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals(new URL("http://example.com/path"), result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"uri://example.com/path\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals(new URI("uri://example.com/path"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "java.lang.String.class.getName()".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "com.example.test.FromStringDeserializerTest$FromStringDeserializerTest$$EnclosingClassType$$1.class".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, ctxt);
        TypeFactory typeFactory = ctxt.getTypeFactory();
        assertEquals(typeFactory.constructType(FromStringDeserializerTest.class.getName() + "$" +
                FromStringDeserializerTest.class.getSimpleName() + "$$EnclosingClassType$$1.class"), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"USD\".getCurrencyInstance()".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "/[a-z]+\\.js$/".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertTrue(result.pattern().matches("example.js"));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"en_US\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"en_US_POSIX\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"en_US_POSIX_abcDEF\".format(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: \"en_US_POSIX_abcDEF\": Invalid value for locale key \"variant\": " +
                    "The value \"abcDEF\" is not a valid variant.", e.getMessage());
        }
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"UTF-8\".getBytes()".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(StandardCharsets.UTF_8, result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"ISO-8859-1\".getName()".getBytes(StandardCharsets.UTF_8)));
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(StandardCharsets.ISO_8859_1, result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"UTF-16LE\".getDisplayName(Locale.ENGLISH)".getBytes(StandardCharsets.UTF_8)));
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: \"UTF-16LE\": The length of the Charset name is not valid.", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"America/Los_Angeles\".getID()".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"Etc/UTC\".getID()".getBytes(StandardCharsets.UTF_8)));
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("Etc/UTC"), result);

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"InvalidTimeZone\".getID()".getBytes(StandardCharsets.UTF_8)));
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: \"InvalidTimeZone\": Unknown timezone ID: InvalidTimeZone.", e.getMessage());
        }
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "[255.255.255.255]".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetAddress.class, result);
        // checking the result is not possible, as it depends on the OS

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "[256.256.256.256]".getBytes(StandardCharsets.UTF_8)));
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: [256.256.256.256]: Invalid IP address: 256.", e.getMessage());
        }

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "[::1]".getBytes(StandardCharsets.UTF_8)));
        InetSocketAddress inetSocketAddressResult = (InetSocketAddress) deserializer.deserialize(parser, ctxt);
        assertEquals(InetAddress.getByName("::1"), inetSocketAddressResult.getAddress());
        assertEquals(0, inetSocketAddressResult.getPort());

        parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "[::1]:8080".getBytes(StandardCharsets.UTF_8)));
        InetSocketAddress inetSocketAddressResult2 = (InetSocketAddress) deserializer.deserialize(parser, ctxt);
        assertEquals(InetAddress.getByName("::1"), inetSocketAddressResult2.getAddress());
        assertEquals(8080, inetSocketAddressResult2.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"Hello World!\"".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(parser, ctxt);
        assertEquals("Hello World!", result.toString());
    }

    @Test
    public void testEmptyString() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "\"\".getBytes(StandardCharsets.UTF_8)"));
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertSame(JsonMappingException.ClassId.INVALID_FORMAT, e.getId());
            assertInstanceOf(InvalidFormatException.class, e.getCause());
            assertNull(e.getCause().getCause());
            assertFalse(e.isUnexpectedType());
            assertFalse(e.isRecoverable());
        }
    }

    @Test
    public void testUnexpectedToken() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser parser = objectMapper.getFactory().getParser(new ByteArrayInputStream(
                "[1, 2, 3]".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unexpected token (START_ARRAY) @ [Source: (ByteArrayInputSource) @ line 1, column 1]", e.getMessage());
        }
    }

    @Test
    public void testEmbeddedObject() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new ByteArrayInputStream(
                "{\"someField\":{\"class\":\"java.lang.String\",\"value\":\"Hello World!\"}}".getBytes(StandardCharsets.UTF_8)));
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(parser, ctxt);
        assertEquals("Hello World!", result);
    }
}