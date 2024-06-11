package com.example.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class FromStringDeserializerTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"/path/to/file.txt\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof File);
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"http://example.com\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"uri://example.com/path?query#fragment\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof URI);
        assertEquals(new URI("uri://example.com/path?query#fragment"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"java.lang.String\""), objectMapper.getDeserializationContext());
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(javaType.getRawClass());
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"[1, 2, 3]\""), objectMapper.getDeserializationContext());
        assertEquals(javaType, result.getClass().getComponentType().getGenericComponentType());
        assertArrayEquals((int[]) new int[] {1, 2, 3}, (int[]) result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"USD\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof Currency);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"XCD\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"/\\w+\\.\\w+\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof Pattern);
        assertEquals(Pattern.compile("/\\w+\\.\\w+"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"/\\w+\\.\\w+\\w+\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"en_US\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"en_US_Invalid\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"UTF-8\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof Charset);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"InvalidCharset\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"America/Los_Angeles\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof TimeZone);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"InvalidTimeZone\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"192.168.1.1\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof InetAddress);
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"123.123.123.123.123.123\""), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser("\"[192.168.1.1]:8080\""), objectMapper.getDeserializationContext());
        assertTrue(result instanceof InetSocketAddress);
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"[192.168.1.1]:8080:8080\""), objectMapper.getDeserializationContext());
    }
}