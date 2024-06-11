package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TestBase;
import com.fasterxml.jackson.databind.json.JsonFactory;

public class FromStringDeserializerTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static <T> JavaType getType(Class<T> clazz) {
        return new ObjectMapper().getTypeFactory().constructType(clazz);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"file.txt\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        File deser = parser.readValueAs(File.class, getContext(parser));
        assertEquals("file.txt", deser.getName());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"http://example.com\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        URL deser = parser.readValueAs(URL.class, getContext(parser));
        assertEquals("http://example.com", deser.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"http://example.com/path/to/file\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        URI deser = parser.readValueAs(URI.class, getContext(parser));
        assertEquals("http://example.com/path/to/file", deser.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"java.lang.String\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        Class<?> deser = parser.readValueAs(Class.class, getContext(parser));
        assertEquals(String.class, deser);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"java.lang.String.class\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        JavaType deser = parser.readValueAs(JavaType.class, getContext(parser));
        assertEquals(String.class, deser.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"USD\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        Currency deser = parser.readValueAs(Currency.class, getContext(parser));
        assertEquals(Currency.getInstance("USD"), deser);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"\\w+\\.\\w+\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        Pattern deser = parser.readValueAs(Pattern.class, getContext(parser));
        String regex = deser.pattern();
        assertEquals("\\w+\\.\\w+", regex);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"en_US\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        Locale deser = parser.readValueAs(Locale.class, getContext(parser));
        assertEquals(Locale.US, deser);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"UTF-8\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        Charset deser = parser.readValueAs(Charset.class, getContext(parser));
        assertEquals(Charset.forName("UTF-8"), deser);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"America/Los_Angeles\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        TimeZone deser = parser.readValueAs(TimeZone.class, getContext(parser));
        assertEquals("America/Los_Angeles", deser.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"192.168.1.1\"";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        InetAddress deser = parser.readValueAs(InetAddress.class, getContext(parser));
        assertEquals("192.168.1.1", deser.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "[\"192.168.1.1\", 80]";
        JsonParser parser = factory.createParser(json);
        parser.nextToken();
        InetSocketAddress deser = parser.readValueAs(InetSocketAddress.class, getContext(parser));
        assertEquals("192.168.1.1", deser.getAddress().getHostAddress());
        assertEquals(80, deser.getPort());
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonFactory factory = mapper.getFactory();
        String json = "\"not_a_valid_type\"";
        JsonParser parser = factory.createParser(json);
        try {
            parser.nextToken();
            parser.readValueAs(File.class, getContext(parser));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation: not_a_valid_type (class java.io.File, not a recognized Java type)", e.getMessage());
        }
    }

    private DeserializationContext getContext(JsonParser parser) {
        return new DeserializationContext(parser, null, null);
    }
}