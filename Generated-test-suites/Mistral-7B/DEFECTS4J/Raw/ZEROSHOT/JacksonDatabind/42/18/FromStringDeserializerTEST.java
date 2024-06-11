package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private JavaType getType(ObjectMapper mapper, Class<?> clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File input = new File("test.txt");
        String inputStr = "test.txt";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        File expected = input;
        File actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL input = new URL("http://localhost:8080/test.html");
        String inputStr = "http://localhost:8080/test.html";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        URL expected = input;
        URL actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI input = new URI("http://localhost:8080/test.html");
        String inputStr = "http://localhost:8080/test.html";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        URI expected = input;
        URI actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> input = String[].class;
        String inputStr = "java.lang.String[]";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        Class<?> expected = input;
        Class<?> actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getName(), actual.getName());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType input = mapper.getTypeFactory().constructType(String[].class);
        String inputStr = "class [Ljava.lang.String;";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        JavaType expected = input;
        JavaType actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getType(), actual.getType());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency input = Currency.getInstance("USD");
        String inputStr = "USD";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        Currency expected = input;
        Currency actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern input = Pattern.compile("(.*):(.*)");
        String inputStr = "(.*):(.*)";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        Pattern expected = input;
        Pattern actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.pattern(), actual.pattern());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale input = Locale.US;
        String inputStr = "en_US";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        Locale expected = input;
        Locale actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getLanguage(), actual.getLanguage());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getVariant(), actual.getVariant());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset input = Charset.forName("UTF-8");
        String inputStr = "UTF-8";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        Charset expected = input;
        Charset actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(inputStr, actual().name());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone input = TimeZone.getTimeZone("UTC");
        String inputStr = "UTC";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        TimeZone expected = input;
        TimeZone actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getID(), actual.getID());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress input = InetAddress.getByName("127.0.0.1");
        String inputStr = "127.0.0.1";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        InetAddress expected = input;
        InetAddress actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getHostAddress(), actual.getHostAddress());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress input = new InetSocketAddress("localhost", 8080);
        String inputStr = "[localhost]:8080";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        InetSocketAddress expected = input;
        InetSocketAddress actual = deserializer.deserialize(mapper.treeToValues(node, null), null);
        assertEquals(expected.getAddress().getHostAddress(), actual.getAddress().getHostAddress());
        assertEquals(expected.getPort(), actual.getPort());
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String inputStr = "test";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        deserializer.deserialize(mapper.treeToValues(node, null), null);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<List<Integer>> deserializer = FromStringDeserializer.findDeserializer(List.class);
        String inputStr = "[1,2,3]";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", inputStr);
        deserializer.deserialize(mapper.treeToValues(node, null), null);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "");
        deserializer.deserialize(mapper.treeToValues(node, null), null);
    }

    @SuppressWarnings("deprecation")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "invalid");
        deserializer.deserialize(mapper.treeToValues(node, null), null);
    }
}