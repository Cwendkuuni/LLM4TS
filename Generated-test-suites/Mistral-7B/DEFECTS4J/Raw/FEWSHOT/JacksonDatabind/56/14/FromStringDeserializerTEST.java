package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"/tmp/test.txt\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, ctxt);

        assertEquals("/tmp/test.txt", result.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://example.com\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, ctxt);

        assertEquals("http://example.com", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://example.com/path/to/file\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, ctxt);

        assertEquals("http://example.com/path/to/file", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "java.lang.String";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Class<?> result = deserializer.deserialize(parser, ctxt);

        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "com.example.MyClass";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(MyClass.class);
        JavaType result = deserializer.deserialize(parser, ctxt);

        assertEquals(MyClass.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "USD";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, ctxt);

        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "\\w+@\\w+\\.\\w+";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, ctxt);

        assertEquals(Pattern.compile("\\w+@\\w+\\.\\w+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "en_US";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, ctxt);

        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "UTF-8";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, ctxt);

        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "America/Los_Angeles";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, ctxt);

        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "192.168.1.1";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, ctxt);

        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[::1]:8080";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);

        assertEquals("[::1]", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(File.class);
        Object result = deserializer.deserialize(parser, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidType() throws IOException {
        String json = "42";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Object result = deserializer.deserialize(parser, ctxt);

        assertInstanceOf(JsonMappingException.class, result);
    }
}