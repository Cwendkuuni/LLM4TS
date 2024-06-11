package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertInstanceof;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(File.class);
        JsonParser parser = setupContext().createParser("\"/path/to/file.txt\"");
        File result = deserializer.deserialize(parser, setupContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        JsonParser parser = setupContext().createParser("\"http://example.com\"");
        URL result = deserializer.deserialize(parser, setupContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testUriDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        JsonParser parser = setupContext().createParser("\"http://example.com/path/to/resource\"");
        URI result = deserializer.deserialize(parser, setupContext());
        assertEquals("http://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        JsonParser parser = setupContext().createParser("\"java.lang.String\"");
        String result = deserializer.deserialize(parser, setupContext()).toString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(JavaType.class);
        JsonParser parser = setupContext().createParser("\"com.example.MyClass\"");
        JavaType result = deserializer.deserialize(parser, setupContext());
        assertEquals("com.example.MyClass", result.getRawClass().getName());
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Currency.class);
        JsonParser parser = setupContext().createParser("\"USD\"");
        Currency result = deserializer.deserialize(parser, setupContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        JsonParser parser = setupContext().createParser("\"/^[A-Za-z]+$/\"");
        Pattern result = deserializer.deserialize(parser, setupContext());
        assertInstanceof(Pattern.class, result);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        JsonParser parser = setupContext().createParser("\"en_US\"");
        Locale result = deserializer.deserialize(parser, setupContext());
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Charset.class);
        JsonParser parser = setupContext().createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(parser, setupContext());
        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(TimeZone.class);
        JsonParser parser = setupContext().createParser("\"America/Los_Angeles\"");
        TimeZone result = deserializer.deserialize(parser, setupContext());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(InetAddress.class);
        JsonParser parser = setupContext().createParser("\"127.0.0.1\"");
        InetAddress result
                = deserializer.deserialize(parser, setupContext());
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(InetSocketAddress.class);
        JsonParser parser = setupContext().createParser("\"[255.255.255.255]:80\"");
        InetSocketAddress result
                = deserializer.deserialize(parser, setupContext());
        assertEquals("[255.255.255.255]:80", result.toString());
    }

    @Test
    public void testStringBuilderDeserializer() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = findDeserializer(StringBuilder.class);
        JsonParser parser = setupContext().createParser("\"Hello, world!\"");
        StringBuilder result = deserializer.deserialize(parser, setupContext());
        assertEquals("Hello, world!", result.toString());
    }

    @Test
    public void testEmptyStringDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        JsonParser parser = setupContext().createParser("\"\"");
        String result = deserializer.deserialize(parser, setupContext()).toString();
        assertNull(result);
    }

    @Test
    public void testNullDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        JsonParser parser = setupContext().createParser(null);
        String result = deserializer.deserialize(parser, setupContext()).toString();
        assertNull(result);
    }

    @Test
    public void testUnsupportedTypeDeserializer() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Integer.class);
        JsonParser parser = setupContext().createParser("\"java.lang.String\"");
        assertNull(deserializer.deserialize(parser, setupContext()));
    }

    @Test
    public void testEmbeddedObjectDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<FromStringDeserializerTest> deserializer = findDeserializer(FromStringDeserializerTest.class);
        String jsonString = "{\"class\": \"FromStringDeserializerTest\", \"value\": \"Test String\"}";
        JsonNode node = objectMapper.readTree(new TextNode(jsonString).asText());
        Object readValue = deserializer.deserialize(node, setupContext());
        assertEquals(FromStringDeserializerTest.class, readValue.getClass());
    }

    @Test
    public void testArrayDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer deserializer = findDeserializer(Integer[].class);
        String jsonString = "[1, 2, 3]";
        JsonNode node = objectMapper.readTree(new TextNode(jsonString).asText());
        Integer[] result = deserializer.deserialize(node, setupContext());
        assertArrayEquals(new Integer[]{1, 2, 3}, result);
    }

    private DeserializationContext setupContext() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getDeserializationContext();
    }

    private static FromStringDeserializer findDeserializer(Class<?> rawType) {
        return (FromStringDeserializer) FromStringDeserializer.findDeserializer(rawType);
    }

    private void assertArrayEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Expected and actual arrays have different lengths.");
        }
        for (int i = 0; i < expected.length; ++i) {
            if (expected[i] != actual[i]) {
                throw new AssertionError("Expected [" + Arrays.toString(expected) + "] but got [" + Arrays.toString(actual) + "].");
            }
        }
    }
}