package com.example.junit; // replace "com.example.junit" with the actual package name of your test file

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestInstance(Class<T> valueType) {
        return (T) new FromStringDeserializer<T>(valueType) {
            @Override
            protected T _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return null; // this method is not used in testing
            }
        }.withoutSerialization();
    }

    @Test
    public void testDeserializeSupportedTypes() throws Exception {
        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = findDeserializer(supportedType);
            assertNotNull(deserializer);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(int[].class);
        assertNull(deserializer);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"invalid_type\"");
        deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(node.toString().getBytes(StandardCharsets.UTF_8))), null);
        fail("Expected JsonMappingException but got " + result.getClass().getName());
    }

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(File.class);
        File expected = new File("test.txt");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("test.txt".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected.getAbsolutePath(), result.toString());
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        String urlString = "https://www.example.com/";
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(urlString.getBytes(StandardCharsets.UTF_8))), null);
        assertInstanceOf(URL.class, result);
        URL url = (URL) result;
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testDeserializeUri() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        String uriString = "https://www.example.com/path/to/resource";
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(uriString.getBytes(StandardCharsets.UTF_8))), null);
        assertInstanceOf(URI.class, result);
        URI uri = (URI) result;
        assertEquals(uriString, uri.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeMalformedUrl() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        String urlString = "invalid_url_format";
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(urlString.getBytes(StandardCharsets.UTF_8))), null);
        fail("Expected MalformedURLException but got " + result.getClass().getName());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        Class<?> expectedClass = String.class;
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("java.lang.String.class".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expectedClass.getName(), result.toString());
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(String[].class);
        JavaType expectedType = mapper.getTypeFactory().constructArrayType(String.class);
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("java.lang.String[]".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expectedType, result);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("USD".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        String patternString = "\\d{3}-\\d{2}-\\d{3}";
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(patternString.getBytes(StandardCharsets.UTF_8))), null);
        assertInstanceOf(Pattern.class, result);
        Pattern pattern = (Pattern) result;
        assertEquals(patternString, pattern.pattern());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeInvalidPattern() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        String patternString = "\\d{3}-\\d{3}-\\d{3}"; // invalid number of hyphens
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(patternString.getBytes(StandardCharsets.UTF_8))), null);
        fail("Expected IllegalStateException but got " + result.getClass().getName());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        Locale expected = new Locale("en_US");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("en_US".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeLocaleWithTwoParts() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        Locale expected = new Locale("fr_FR");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("fr_FR".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("UTF-8".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("UTC".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("192.168.1.1".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(InetSocketAddress.class);
        String addressString = "[255.255.255.255]:80";
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream(addressString.getBytes(StandardCharsets.UTF_8))), null);
        assertInstanceOf(InetSocketAddress.class, result);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
        assertEquals(inetSocketAddress.getAddress().getHostAddress(), "255.255.255.255");
        assertEquals(inetSocketAddress.getPort(), 80);
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("test");
        Object result = deserializer.deserialize(mapper.parserForObject(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8))), null);
        assertEquals(expected, result);
    }

    private FromStringDeserializer findDeserializer(Class<?> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.findValueDeserializer(valueType, null);
    }

    private ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SerializationConfig config) throws Exception {
                config.findAndRegisterModules();
            }
        });
        mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
        return mapper;
    }
}