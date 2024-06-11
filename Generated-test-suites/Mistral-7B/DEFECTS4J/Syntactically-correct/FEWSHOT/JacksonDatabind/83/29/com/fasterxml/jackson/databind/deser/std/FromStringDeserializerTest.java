package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest extends JsonTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("/path/to/file"), deserializer.deserialize(parser, context()));
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://example.com"), deserializer.deserialize(parser, context()));
    }

    @Test
    public void testUriDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"uri://example.com\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("uri://example.com"), deserializer.deserialize(parser, context()));
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("java.lang.String");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals(String.class, deserializer.deserialize(parser, context()).getClass());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("com.example.Foo");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = (JavaType) deserializer.deserialize(parser, context());
        assertEquals(JavaType.class, result.getClass());
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"EUR\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, context());
        assertEquals(Currency.EUR, result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"[a-z]+\\.json$\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = (Pattern) deserializer.deserialize(parser, context());
        assertInstanceof(Pattern.class, result);
        assertEquals("[a-z]+\\.json$", result.pattern().toString());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, context());
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, context());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, context());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"192.168.0.1\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, context());
        assertInstanceof(InetAddress.class, result);
        assertEquals("192.168.0.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(parser, context());
        assertEquals("255.255.255.255", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser("\"Hello World!\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder result = (StringBuilder) deserializer.deserialize(parser, context());
        assertEquals("Hello World!", result.toString());
    }

    @Test
    public void testInvalidStringDeserialization() throws IOException {
        ObjectMapper mapper = objectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer.deserialize(parser, context()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormatException() throws IOException {
        ObjectMapper mapper = objectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"01-01-2023T01:01:01Z\""); // Invalid date format

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(LocalDateTime.class);
        deserializer.deserialize(parser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidClassException() throws IOException {
        ObjectMapper mapper = objectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"invalid_class\"");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(parser, context());
    }
}