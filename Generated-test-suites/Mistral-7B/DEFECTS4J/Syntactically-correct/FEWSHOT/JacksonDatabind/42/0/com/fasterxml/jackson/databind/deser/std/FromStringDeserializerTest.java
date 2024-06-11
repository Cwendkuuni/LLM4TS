package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.net.*;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File deserialized = mapper.readValue(new TextNode("path/to/file.txt").asText(), FromStringDeserializer.findDeserializer(File.class).getType());
        assertEquals(new File("path/to/file.txt"), deserialized);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL deserialized = mapper.readValue(new TextNode("http://example.com").asText(), FromStringDeserializer.findDeserializer(URL.class).getType());
        assertEquals("http://example.com", deserialized.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URI deserialized = mapper.readValue(new TextNode("http://example.com/path/to/file.txt").asText(), FromStringDeserializer.findDeserializer(URI.class).getType());
        assertEquals("http://example.com/path/to/file.txt", deserialized.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> deserialized = mapper.readValue(new TextNode("java.lang.String").asText(), FromStringDeserializer.findDeserializer(Class.class).getType());
        assertEquals(String.class, deserialized);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType deserialized = mapper.readValue(new TextNode("java.util.List<java.lang.String>").asText(), FromStringDeserializer.findDeserializer(JavaType.class).getType());
        assertEquals(new TypeFactory().constructType(List.class, String.class), deserialized);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Currency deserialized = mapper.readValue(new TextNode("EUR").asText(), FromStringDeserializer.findDeserializer(Currency.class).getType());
        assertEquals(Currency.getInstance("EUR"), deserialized);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Pattern deserialized = mapper.readValue(new TextNode("\\w+@\\w+\\.\\w+").asText(), FromStringDeserializer.findDeserializer(Pattern.class).getType());
        assertEquals(Pattern.compile("\\w+@\\w+\\.\\w+"), deserialized);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Locale deserialized = mapper.readValue(new TextNode("en_US").asText(), FromStringDeserializer.findDeserializer(Locale.class).getType());
        assertEquals(Locale.US, deserialized);

        deserialized = mapper.readValue(new TextNode("en_US_POSIX").asText(), FromStringDeserializer.findDeserializer(Locale.class).getType());
        assertEquals(Locale.US, deserialized);

        deserialized = mapper.readValue(new TextNode("en_US_POSIX.utf8").asText(), FromStringDeserializer.findDeserializer(Locale.class).getType());
        assertEquals(new Locale("en", "US", ""), deserialized);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Charset deserialized = mapper.readValue(new TextNode("US-ASCII").asText(), FromStringDeserializer.findDeserializer(Charset.class).getType());
        assertEquals(Charset.forName("US-ASCII"), deserialized);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TimeZone deserialized = mapper.readValue(new TextNode("UTC"), FromStringDeserializer.findDeserializer(TimeZone.class).getType());
        assertEquals(TimeZone.getTimeZone("UTC"), deserialized);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InetAddress deserialized = mapper.readValue(new TextNode("192.168.1.1").asText(), FromStringDeserializer.findDeserializer(InetAddress.class).getType());
        assertEquals(InetAddress.getByName("192.168.1.1"), deserialized);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InetSocketAddress deserialized = mapper.readValue(new TextNode("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80").asText(), FromStringDeserializer.findDeserializer(InetSocketAddress.class).getType());
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 80), 80), deserialized);

        deserialized = mapper.readValue(new TextNode("localhost:8080").asText(), FromStringDeserializer.findDeserializer(InetSocketAddress.class).getType());
        assertEquals(new InetSocketAddress(InetAddress.getByName("localhost"), 8080), deserialized);
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Test for File
        try {
            mapper.readValue(new TextNode("").asText(), FromStringDeserializer.findDeserializer(File.class).getType());
            fail("Expected an exception when deserializing empty string to File");
        } catch (InvalidFormatException e) {
            // Expected
        }

        // Test for URL, URI, Class, JavaType, Currency, Pattern, Locale, Charset, TimeZone, InetAddress and InetSocketAddress
        String[] supportedTypes = {
                "java.net.URL",
                "java.net.URI",
                "java.lang.Class",
                "com.fasterxml.jackson.core.type.JavaType",
                "java.util.Currency",
                "java.util.regex.Pattern",
                "java.util.Locale",
                "java.nio.charset.Charset",
                "java.util.TimeZone",
                "java.net.InetAddress",
                "java.net.InetSocketAddress"
        };

        for (String type : supportedTypes) {
            try {
                mapper.readValue(new TextNode("").asText(), FromStringDeserializer.findDeserializer(getClassFromName(type)).getType());
                fail("Expected an exception when deserializing empty string to " + type.substring(type.lastIndexOf(".")+1));
            } catch (InvalidFormatException e) {
                // Expected
            }
        }
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object embeddedObject = new Object();
        JsonNode node = mapper.valueToTree(embeddedObject);

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Class.forName("java.lang.String"));
        String deserialized = deserializer.deserializeEmbedded(embeddedObject, mapper.getDeserializationContext()).toString();
        assertEquals("java.lang.String@" + Integer.toHexString(System.identityHashCode(embeddedObject)), deserialized);
    }

    @Test
    public void testDeserializeEmptyEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object embeddedObject = new Object();

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Class.forName("java.lang.String"));
        try {
            deserializer.deserializeEmbedded(null, mapper.getDeserializationContext());
            fail("Expected an exception when deserializing null to String");
        } catch (JsonMappingException e) {
            // Expected
        }

        JsonNode node = mapper.valueToTree(embeddedObject);
        try {
            deserializer.deserializeEmbedded(node.asNull(), mapper.getDeserializationContext());
            fail("Expected an exception when deserializing null JsonNode to String");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static Class<?> getClassFromName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}