package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(FromStringDeserializer.class);
        FromStringDeserializer deserializer = mapper.readValue(getClass().getResource("/fromstringdeserializer.json"), javaType);

        // Test deserialization for various types
        String fileString = "path/to/file.txt";
        assertEquals(new File(fileString), deserializer.deserialize(null, new TestContext()));

        String urlString = "http://example.com";
        assertEquals(new URL(urlString), deserializer.deserialize(null, new TestContext()));

        String uriString = "http://example.com/path?query=param";
        assertEquals(new URI(uriString), deserializer.deserialize(null, new TestContext()));

        Class<?> clazz = String.class;
        assertEquals(clazz, deserializer.deserialize(null, new TestContext(), "String"));

        JavaType javaType1 = mapper.getTypeFactory().constructType(Integer.class);
        assertEquals(javaType1, deserializer.deserialize(null, new TestContext(), "java.lang.Integer"));

        // Test deserialization for currency
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(null, new TestContext(), "USD"));

        // Test deserialization for pattern
        assertEquals(Pattern.compile("\\d{3}"), deserializer.deserialize(null, new TestContext(), "\\d{3}"));

        // Test deserialization for locale
        assertEquals(Locale.ENGLISH, deserializer.deserialize(null, new TestContext(), "en_US"));
        assertEquals(Locale.FRENCH, deserializer.deserialize(null, new TestContext(), "fr_FR"));
        assertEquals(Locale.ITALIAN, deserializer.deserialize(null, new TestContext(), "it_IT"));

        // Test deserialization for charset
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(null, new TestContext(), "UTF-8"));

        // Test deserialization for timezone
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(null, new TestContext(), "UTC"));

        // Test deserialization for InetAddress
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(null, new TestContext(), "127.0.0.1"));

        // Test deserialization for InetSocketAddress
        assertEquals(new InetSocketAddress("localhost", 80), deserializer.deserialize(null, new TestContext(), "localhost:80"));
        assertEquals(new InetSocketAddress("127.0.0.1", 80), deserializer.deserialize(null, new TestContext(), "127.0.0.1:80"));
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(null, new TestContext(), "localhost:8080"));
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), deserializer.deserialize(null, new TestContext(), "127.0.0.1:8080"));

        // Test deserialization for StringBuilder
        assertEquals(new StringBuilder(), deserializer.deserialize(null, new TestContext(), "new StringBuilder()"));
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = mapper.findValue("{}", FromStringDeserializer.class);

        // Test deserialization for various types
        assertEquals(null, deserializer.deserialize(null, new TestContext()));
        assertEquals(null, deserializer.deserialize(null, new TestContext(), ""));

        // Test deserialization for URI
        assertEquals(URI.create(""), deserializer.deserialize(null, new TestContext(), "URI.create(\"\")"));

        // Test deserialization for Locale
        assertEquals(Locale.ROOT, deserializer.deserialize(null, new TestContext(), "Locale.ROOT"));

        // Test deserialization for StringBuilder
        assertEquals(new StringBuilder(), deserializer.deserialize(null, new TestContext(), "new StringBuilder()"));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = mapper.findValue("{}", FromStringDeserializer.class);

        // Test invalid format for various types
        deserializer.deserialize(null, new TestContext(), "invalid_format");
        deserializer.deserialize(null, new TestContext(), "java.lang.InvalidFormatException");
        deserializer.deserialize(null, new TestContext(), "java.net.MalformedURLException");
        deserializer.deserialize(null, new TestContext(), "java.text.ParseException");
        deserializer.deserialize(null, new TestContext(), "java.util.NoSuchElementException");
    }

    @org.junit.Test
    public void testDeserializeFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);

        String jsonArray = "[1, 2, 3]";

        FromStringDeserializer deserializer = mapper.findValue(jsonArray, FromStringDeserializer.class);
        assertEquals(new int[]{1, 2, 3}, deserializer.deserializeFromArray(null, new TestContext(), jsonArray));
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{\"embedded\":{\"value\":\"test\"}}";

        FromStringDeserializer deserializer = mapper.findValue("{}", FromStringDeserializer.class);

        Object embeddedObject = mapper.readValue(getClass().getResource("/embedded_object.json"), Object.class);

        assertEquals(deserializer.deserializeEmbedded(embeddedObject, new TestContext()), jsonString.substring(8, jsonString.length()-2));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValue(Class<?> valueType, String valueAsString) throws IOException {
            return null;
        }

        @Override
        public Object findValue(Class<?> valueType) throws IOException {
            return null;
        }

        @Override
        public Object handleUnexpectedToken(Class<?> valueTypeReference, JsonParser parser) throws IOException {
            throw new IOException("Unexpected token: " + parser.getCurrentName());
        }
    }
}