package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(File.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(File.class);
        TextNode node = TextNode.valueOf("path/to/file.txt");
        File result = deserializer.deserialize(node, ctxt);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testURLDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(URL.class);
        TextNode node = TextNode.valueOf("http://example.com");
        URL result = deserializer.deserialize(node, ctxt);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testURIDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(URI.class);
        TextNode node = TextNode.valueOf("http://example.com/path");
        URI result = deserializer.deserialize(node, ctxt);
        assertEquals(new URI("http://example.com/path"), result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(String.class);
        TextNode node = TextNode.valueOf("java.lang.String");
        String result = deserializer.deserialize(node, ctxt).getValueAsString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Integer[].class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(JavaType.class);
        TextNode node = TextNode.valueOf("[I");
        JavaType result = deserializer.deserialize(node, ctxt);
        assertEquals(JavaType.constructParametricType(Integer[].class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Currency.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(Currency.class);
        TextNode node = TextNode.valueOf("USD");
        Currency result = deserializer.deserialize(node, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(Pattern.class);
        TextNode node = TextNode.valueOf("\\w+");
        Pattern result = deserializer.deserialize(node, ctxt);
        assertEquals(Pattern.compile("\\w+"), result);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(Locale.class);
        TextNode node = TextNode.valueOf("en_US");
        Locale result = deserializer.deserialize(node, ctxt);
        assertEquals(Locale.US, result);

        node = TextNode.valueOf("en_US_POSIX");
        result = deserializer.deserialize(node, ctxt);
        assertEquals(Locale.US, result);

        node = TextNode.valueOf("en_US-u-ca-zapf-latin");
        result = deserializer.deserialize(node, ctxt);
        assertEquals(new Locale("en", "US", "u", "ca", "ZapfLatin"), result);
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Charset.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(Charset.class);
        TextNode node = TextNode.valueOf("UTF-8");
        Charset result = deserializer.deserialize(node, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(TimeZone.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(TimeZone.class);
        TextNode node = TextNode.valueOf("America/New_York");
        TimeZone result = deserializer.deserialize(node, ctxt);
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(InetAddress.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(InetAddress.class);
        TextNode node = TextNode.valueOf("192.168.0.1");
        InetAddress result = deserializer.deserialize(node, ctxt);
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(InetSocketAddress.class);
        TextNode node = TextNode.valueOf("[192.168.0.1]:8080");
        InetSocketAddress result = deserializer.deserialize(node, ctxt);
        assertEquals(new InetSocketAddress("192.168.0.1", 8080), result);

        node = TextNode.valueOf("192.168.0.1:8080");
        result = deserializer.deserialize(node, ctxt);
        assertEquals(new InetSocketAddress("192.168.0.1", 8080), result);
    }

    @Test
    public void testStringBuilderDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(StringBuilder.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(StringBuilder.class);
        TextNode node = TextNode.valueOf("abc");
        StringBuilder result = (StringBuilder) deserializer.deserialize(node, ctxt);
        assertEquals("abc", result.toString());
    }

    @Test
    public void testInvalidDeserialization() throws Exception {
        FromStringDeserializer deserializer = findDeserializer(Integer.class);
        DeserializationContext ctxt = new DeserializationContext(new TestAnnotationIntrospector(), new ObjectMapper());
        ObjectReader reader = ctxt.readerFor(Integer.class);
        TextNode node = TextNode.valueOf("abc");
        try {
            deserializer.deserialize(node, ctxt);
            fail();
        } catch (InvalidFormatException e) {
            assertEquals("not a valid number", e.getMessage());
        }
    }

    private static FromStringDeserializer findDeserializer(Class<?> rawType) {
        StdDeserializerFactory factory = new StdDeserializerFactory();
        return factory.findValueDeserializer(RawTypeValidator.parseRawType(rawType), null);
    }
}