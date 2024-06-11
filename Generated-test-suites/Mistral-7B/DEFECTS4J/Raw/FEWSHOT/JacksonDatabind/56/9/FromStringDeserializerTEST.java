package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        String json = "{\"className\":\"java.io.File\",\"value\":\"/test.txt\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(File.class);
        File result = (File) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("/test.txt", result.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeURL() throws IOException {
        String json = "{\"className\":\"java.net.URL\",\"value\":\"http://test.com\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        URL result = (URL) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("http://test.com", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeURI() throws IOException {
        String json = "{\"className\":\"java.net.URI\",\"value\":\"http://test.com/path/to/file\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("http://test.com/path/to/file", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeClass() throws IOException {
        String json = "{\"className\":\"java.lang.String\",\"value\":\"java.util.Date\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Class<?> result = (Class<?>) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(Date.class, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "{\"className\":\"com.fasterxml.jackson.databind.JsonNode\",\"value\":\"[1, 2, 3]\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(JsonNode.class);
        JavaType result = (JavaType) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertNotNull(result.getRawClass());
        assertEquals(JsonNode.class, result.getRawClass());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "{\"className\":\"java.util.Currency\",\"value\":\"USD\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = (Currency) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializePattern() throws IOException {
        String json = "{\"className\":\"java.util.regex.Pattern\",\"value\":\"\\w+\\.\\w+\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = (Pattern) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(Pattern.compile("\\w+\\.\\w+"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeLocale() throws IOException {
        String json1 = "{\"className\":\"java.util.Locale\",\"value\":\"en_US\"}";
        String json2 = "{\"className\":\"java.util.Locale\",\"value\":\"en_US_POSIX\"}";
        JsonParser parser = mapper.getFactory().createParser(json1);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result1 = (Locale) fs.deserialize(parser, ctxt);

        assertNotNull(result1);
        assertEquals(Locale.US, result1);

        parser = mapper.getFactory().createParser(json2);
        Locale result2 = (Locale) fs.deserialize(parser, ctxt);

        assertNotNull(result2);
        assertEquals(Locale.US, result2);
        assertNotNull(result2.getVariants());
        assertFalse(result2.getVariants().isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "{\"className\":\"java.nio.charset.StandardCharsets\",\"value\":\"US_ASCII\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = (Charset) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(StandardCharsets.US_ASCII, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "{\"className\":\"java.util.TimeZone\",\"value\":\"UTC\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = (TimeZone) fs.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("UTC", result.getID());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetAddress() throws IOException {
        String jsonIpv4 = "{\"className\":\"java.net.InetAddress\",\"value\":\"192.168.1.1\"}";
        String jsonIpv6 = "{\"className\":\"java.net.InetAddress\",\"value\":\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonIpv4);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress resultIpv4 = (InetAddress) fs.deserialize(parser, ctxt);

        assertNotNull(resultIpv4);
        assertEquals("192.168.1.1", resultIpv4.getHostAddress());

        parser = mapper.getFactory().createParser(jsonIpv6);
        InetAddress resultIpv6 = (InetAddress) fs.deserialize(parser, ctxt);

        assertNotNull(resultIpv6);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", resultIpv6.getHostAddress());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String jsonIpv4 = "{\"className\":\"java.net.InetSocketAddress\",\"value\":\"localhost:8080\"}";
        String jsonIpv6 = "{\"className\":\"java.net.InetSocketAddress\",\"value\":\"[2001:0db8:85a3:0:0:8a2e:370:7334]:8080\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonIpv4);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress resultIpv4 = (InetSocketAddress) fs.deserialize(parser, ctxt);

        assertNotNull(resultIpv4);
        assertEquals("localhost", resultIpv4.getAddress().getHostAddress());
        assertEquals(8080, resultIpv4.getPort());

        parser = mapper.getFactory().createParser(jsonIpv6);
        InetSocketAddress resultIpv6 = (InetSocketAddress) fs.deserialize(parser, ctxt);

        assertNotNull(resultIpv6);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", resultIpv6.getAddress().getHostAddress());
        assertEquals(8080, resultIpv6.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "{\"className\":\"java.net.URI\",\"value\":\"\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) fs.deserialize(parser, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        String json = "{\"className\":\"java.util.Locale\",\"value\":null}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer fs = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = (Locale) fs.deserialize(parser, ctxt);

        assertNull(result);
    }
}