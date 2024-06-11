package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"/path/to/file.txt\"");
        File deserializedObject = (File) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", deserializedObject.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://example.com\"");
        URL deserializedObject = (URL) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("http", deserializedObject.getProtocol());
        assertEquals("example.com", deserializedObject.getHost());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://example.com/path/to/file.txt\"");
        URI deserializedObject = (URI) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("http", deserializedObject.getScheme());
        assertEquals("example.com", deserializedObject.getHost());
        assertEquals("/path/to/file.txt", deserializedObject.getPath());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.lang.String\"");
        Class<?> deserializedObject = (Class<?>) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(String.class, deserializedObject);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"com.example.MyClass\"");
        JavaType deserializedObject = (JavaType) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        // Perform additional assertions based on the structure of 'MyClass'
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"EUR\"");
        Currency deserializedObject = (Currency) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Currency.EUR, deserializedObject);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"/^[a-z]+\\.java$\"");
        Pattern deserializedObject = (Pattern) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertTrue(deserializedObject.matcher("MyClass.java").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"en_US\"");
        Locale deserializedObject = (Locale) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedObject);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"UTF-8\"");
        Charset deserializedObject = (Charset) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), deserializedObject);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"America/Los_Angeles\"");
        TimeZone deserializedObject = (TimeZone) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("America/Los_Angeles", deserializedObject.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"127.0.0.1\"");
        InetAddress deserializedObject = (InetAddress) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("127.0.0.1", deserializedObject.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[::1]:80\"");
        InetSocketAddress deserializedObject = (InetSocketAddress) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("[::1]", deserializedObject.getAddress().getHostAddress());
        assertEquals(80, deserializedObject.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<URI> serializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\"");
        URI deserializedObject = (URI) serializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(URI.create(""), deserializedObject);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Integer.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"abc\"");
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> serializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
        assertEquals("Not a valid number: [\"abc\"]", exception.getMessage());
    }

}