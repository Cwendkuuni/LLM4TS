package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest extends BaseTest {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToFile() throws IOException {
        String json = "\"/tmp/test.txt\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        File expected = new File("/tmp/test.txt");
        File deserialized = parser.readValueAs(File.class, ctxt);
        assertEquals(expected.getAbsolutePath(), deserialized.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToURL() throws IOException {
        String json = "\"http://example.com\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        URL expected = new URL("http://example.com");
        URL deserialized = parser.readValueAs(URL.class, ctxt);
        assertEquals(expected.toString(), deserialized.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToURI() throws IOException {
        String json = "\"uri:/test/path\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        URI expected = new URI("uri:/test/path");
        URI deserialized = parser.readValueAs(URI.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToClass() throws IOException {
        String json = "\"java.lang.String\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        Class<String> expected = String.class;
        Class<?> deserialized = parser.readValueAs(String.class, ctxt);
        assertEquals(expected, deserialized.getClass());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToJavaType() throws IOException {
        String json = "\"class com.example.MyClass\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();
        JavaType expected = mapper.getTypeFactory().constructType(MyClass.class);
        JavaType deserialized = parser.readValueAs(JavaType.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToCurrency() throws IOException {
        String json = "\"EUR\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        Currency expected = Currency.getInstance("EUR");
        Currency deserialized = parser.readValueAs(Currency.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToPattern() throws IOException {
        String json = "\"\\w+\\.\\w+\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        Pattern expected = Pattern.compile("\\w+\\.\\w+");
        Pattern deserialized = parser.readValueAs(Pattern.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToLocale() throws IOException {
        String json = "\"en_US\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        Locale expected = Locale.US;
        Locale deserialized = parser.readValueAs(Locale.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToCharset() throws IOException {
        String json = "\"UTF-8\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        Charset expected = Charset.forName("UTF-8");
        Charset deserialized = parser.readValueAs(Charset.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToTimeZone() throws IOException {
        String json = "\"America/New_York\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        TimeZone expected = TimeZone.getTimeZone("America/New_York");
        TimeZone deserialized = parser.readValueAs(TimeZone.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToInetAddress() throws IOException {
        String json = "\"192.168.1.1\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        InetAddress expected = InetAddress.getByName("192.168.1.1");
        InetAddress deserialized = parser.readValueAs(InetAddress.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToInetSocketAddress() throws IOException {
        String json = "\"[255.255.255.255]:80\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 80);
        InetSocketAddress deserialized = parser.readValueAs(InetSocketAddress.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeStringToStringBuilder() throws IOException {
        String json = "\"hello world\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        StringBuilder expected = new StringBuilder("hello world");
        StringBuilder deserialized = parser.readValueAs(StringBuilder.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "\"\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class) {
            @Override
            protected StringBuilder _deserialize(String text, DeserializationContext ctxt) throws IOException {
                return new StringBuilder();
            }
        };

        StringBuilder expected = new StringBuilder();
        StringBuilder deserialized = parser.readValueAs(StringBuilder.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeEmbeddedObject() throws IOException {
        String json = "{\"class\":\"java.net.URI\", \"value\":\"uri:/test/path\"}";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        JavaType javaType = mapper.getTypeFactory().constructType(URI.class);
        URI expected = new URI("uri:/test/path");
        URI deserialized = parser.readValueAs(URI.class, ctxt);
        assertEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeArray() throws IOException {
        String json = "[\"file:/path/to/file1.txt\", \"file:/path/to/file2.txt\"]";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        JavaType javaType = mapper.getTypeFactory().constructCollectionType(File[].class);
        File[] expected = new File[]{"file:/path/to/file1.txt", "file:/path/to/file2.txt"};
        File[] deserialized = parser.readValueAs(File[].class, ctxt);
        assertArrayEquals(expected, deserialized);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeInvalidFormat() throws IOException {
        String json = "\"invalid\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class) {
            @Override
            protected StringBuilder _deserialize(String text, DeserializationContext ctxt) throws IOException {
                throw new InvalidFormatException(ctxt.getParser(), "Invalid text format", text, StringBuilder.class);
            }
        };

        try {
            parser.readValueAs(StringBuilder.class, ctxt);
            fail();
        } catch (InvalidFormatException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeUnknownType() throws IOException {
        String json = "\"MyUnknownClass\"";
        JsonParser parser = createParser(mapper.writeValueAsString(json));
        DeserializationContext ctxt = createContext();

        try {
            parser.readValueAs(JavaType.class, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type MyUnknownClass into java.lang.Class<java.lang.Object>", e.getMessage());
        }
    }

    private JsonFactory createFactory() {
        return new JsonFactory();
    }

    private JsonParser createParser(String json) throws IOException {
        return createFactory().createParser(json);
    }

    private DeserializationContext createContext() {
        return mapper.getDeserializationContext();
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Expected and actual arrays have different lengths");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private static class MyClass {
        // empty class for testing
    }
}