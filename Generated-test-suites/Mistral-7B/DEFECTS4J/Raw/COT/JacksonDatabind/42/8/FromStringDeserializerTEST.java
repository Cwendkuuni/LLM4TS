package com.yourpackage; // replace with your testing package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Locale;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"path/to/file.txt\"");
        File result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"http://example.com\"");
        URL result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"http://example.com/path\"");
        URI result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals("http://example.com/path", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"java.lang.String\"");
        String result = deserializer.deserialize(new JsonParser(node), new DeserializationContext()).getValueAsString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"java.lang.Integer\"");
        JavaType result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals(Integer.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"USD\"");
        Currency result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"/^[a-zA-Z]+$/\"");
        Pattern result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals(Pattern.compile("/^[a-zA-Z]+$/"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectNode nodeEN = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"en_US\"");
        ObjectNode nodeFR = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"fr_FR\"");
        Locale resultEN = deserializer.deserialize(new JsonParser(nodeEN), new DeserializationContext());
        Locale resultFR = deserializer.deserialize(new JsonParser(nodeFR), new DeserializationContext());
        assertEquals(Locale.US, resultEN);
        assertEquals(Locale.FRENCH, resultFR);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        ObjectNode nodeUTF8 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"UTF-8\"");
        ObjectNode nodeISO8859_1 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"ISO-8859-1\"");
        Charset resultUTF8 = deserializer.deserialize(new JsonParser(nodeUTF8), new DeserializationContext());
        Charset resultISO8859_1 = deserializer.deserialize(new JsonParser(nodeISO8859_1), new DeserializationContext());
        assertEquals(Charset.forName("UTF-8"), resultUTF8);
        assertEquals(Charset.forName("ISO-8859-1"), resultISO8859_1);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectNode nodeUTC = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"UTC\"");
        ObjectNode nodeEST = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"America/New_York\"");
        TimeZone resultUTC = deserializer.deserialize(new JsonParser(nodeUTC), new DeserializationContext());
        TimeZone resultEST = deserializer.deserialize(new JsonParser(nodeEST), new DeserializationContext());
        assertEquals(TimeZone.getTimeZone("UTC"), resultUTC);
        assertEquals(TimeZone.getTimeZone("America/New_York"), resultEST);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectNode nodeIPv4 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"192.168.0.1\"");
        ObjectNode nodeIPv6 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"");
        InetAddress resultIPv4 = deserializer.deserialize(new JsonParser(nodeIPv4), new DeserializationContext());
        InetAddress resultIPv6 = deserializer.deserialize(new JsonParser(nodeIPv6), new DeserializationContext());
        assertEquals(InetAddress.getByName("192.168.0.1"), resultIPv4);
        assertEquals(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), resultIPv6);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectNode nodeIPv4 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"192.168.0.1:80\"");
        ObjectNode nodeIPv6 = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"[2001:0db8:85a3:0:0:8a2e:370:7334]:80\"");
        InetSocketAddress resultIPv4 = deserializer.deserialize(new JsonParser(nodeIPv4), new DeserializationContext());
        InetSocketAddress resultIPv6 = deserializer.deserialize(new JsonParser(nodeIPv6), new DeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 80), resultIPv4);
        assertEquals(new InetSocketAddress(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), 80), resultIPv6);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String[].class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("[\"hello\", \"world\"]");
        String[] result = deserializer.deserialize(new JsonParser(node), new DeserializationContext());
        assertEquals(Arrays.asList("hello", "world").toArray(new String[0]), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"invalid\"");
        deserializer.deserialize(new JsonParser(node), new DeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectNode node = JsonParserFactory.getFromClass(ObjectNode.class).parse("\"\"");
        deserializer.deserialize(new JsonParser(node), new DeserializationContext());
    }
}