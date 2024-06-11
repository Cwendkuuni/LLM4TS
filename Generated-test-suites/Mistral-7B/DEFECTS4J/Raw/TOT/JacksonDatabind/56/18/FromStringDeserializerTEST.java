package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"path/to/file.txt\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        File result = deserializer.deserialize(parser, new TestContext());
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testUrlDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"http://www.example.com\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
        URL result = deserializer.deserialize(parser, new TestContext());
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testUriDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"http://www.example.com/path?query#fragment\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        URI result = deserializer.deserialize(parser, new TestContext());
        assertEquals(new URI("http://www.example.com/path?query#fragment"), result);
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"java.lang.String\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        String result = deserializer.deserialize(parser, new TestContext()).toString();
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"java.util.List<java.lang.Integer>\"".replaceAll("\\\\", "").replaceAll("\"", "\\\\\"");
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType result = deserializer.deserialize(parser, new TestContext());
        assertEquals(JavaType.constructType(Integer[].class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"EUR\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
        Currency result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"\\d+(\\.\\d+)?";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern result = deserializer.deserialize(parser, new TestContext());
        assertTrue(result.matcher("123.45").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"en_US\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"UTF-8\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
        Charset result = deserializer.deserialize(parser, new TestContext());
        assertEquals(StandardCharsets.UTF_8, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"America/New_York\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, new TestContext());
        assertEquals("America/New_York", result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "[\"192.168.0.1\"]";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress[].class);
        InetAddress[] result = deserializer.deserialize(parser, new TestContext());
        assertEquals(1, result.length);
        assertEquals("192.168.0.1", result[0].getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "[\"192.168.0.1\", 80]";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress[].class);
        InetSocketAddress[] result = deserializer.deserialize(parser, new TestContext());
        assertEquals(1, result.length);
        assertEquals("192.168.0.1", result[0].getAddress().getHostAddress());
        assertEquals(80, result[0].getPort());
    }

    @Test
    public void testInvalidDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"invalidType\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, new TestContext()));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "\"\"";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        String result = deserializer.deserialize(parser, new TestContext()).toString();
        assertNull(result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "[]";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String[].class);
        String[] result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testNullDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        String json = "null";
        parser.setValue(json);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        String result = deserializer.deserialize(parser, new TestContext()).toString();
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public BaseJsonParser _parser() {
            return null;
        }

        @Override
        public ObjectReader _valueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader _findRootValueDeserializer() {
            return null;
        }

        @Override
        public ObjectReader _config(DeserializationConfig config) {
            return null;
        }
    }
}