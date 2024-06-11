package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeStringToFile() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"/path/to/file.txt\"");

        File result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);

        String expectedPath = "/path/to/file.txt";
        assertEquals(expectedPath, result.getAbsolutePath());
    }

    @Test
    public void testDeserializeStringToUrl() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"http://example.com\"");

        URL result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);

        String expectedProtocol = "http";
        String expectedHost = "example.com";
        assertEquals(expectedProtocol, result.getProtocol());
        assertEquals(expectedHost, result.getHost());
    }

    @Test
    public void testDeserializeStringToUri() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"http://example.com/path/to/resource\"");

        URI result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);

        String expectedProtocol = "http";
        String expectedHost = "example.com";
        String expectedPath = "/path/to/resource";
        assertEquals(expectedProtocol, result.getScheme());
        assertEquals(expectedHost, result.getHost());
        assertEquals(expectedPath, result.getPath());
    }

    @Test
    public void testDeserializeStringToClass() throws IOException, JsonProcessingException, ClassNotFoundException {
        Std serializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"java.lang.String\"");

        Class<?> result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(String.class, result);

        Class<?> result2 = serializer.deserialize(jp, ctxt);
        assertEquals(String.class, result2);
    }

    @Test
    public void testDeserializeStringToJavaType() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"java.util.List<java.lang.String>\":[\"apple\",\"banana\",\"orange\"]");

        JavaType result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(JavaType.constructParametricType(List.class, String.class), result);
    }

    @Test
    public void testDeserializeStringToCurrency() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"USD\"");

        Currency result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializeStringToPattern() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"\\w+(\\.\\w+)*@\\w+([\\.-]?[\\w]{2,})(:\\d{1,5})?\\$\"");

        Pattern result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeStringToLocale() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"en_US\"");

        Locale result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Locale.US, result);

        JsonParser jp2 = objectMapper.createParser("\"fr_FR\"");
        Locale result2 = serializer.deserialize(jp2, ctxt);
        assertEquals(Locale.French, result2);
    }

    @Test
    public void testDeserializeStringToCharset() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"UTF-8\"");

        Charset result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"Europe/Paris\"");

        TimeZone result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result);
    }

    @Test
    public void testDeserializeStringToInetAddress() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"192.168.0.1\"");

        InetAddress result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("192.168.0.1", result.getHostAddress());

        JsonParser jp2 = objectMapper.createParser("\"2001:db8:85a3:0:0:8a2e:370:7334\"");
        InetAddress result2 = serializer.deserialize(jp2, ctxt);
        assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", result2.getHostAddress());
    }

    @Test
    public void testDeserializeStringToInetSocketAddress() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"[2001:db8:85a3:0:0:8a2e:370:7334]:80\"");

        InetSocketAddress result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        JsonParser jp2 = objectMapper.createParser("\"192.168.0.1:80\"");
        InetSocketAddress result2 = serializer.deserialize(jp2, ctxt);
        assertEquals("192.168.0.1", result2.getAddress().getHostAddress());
        assertEquals(80, result2.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"invalid_string\"");

        serializer.deserialize(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("\"\"");

        serializer.deserialize(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("null");

        serializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeArray() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Object[].class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("[\"apple\",\"banana\",\"orange\"]");

        Object[] result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("apple", result[0]);
        assertEquals("banana", result[1]);
        assertEquals("orange", result[2]);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Object[].class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("[]");

        Object[] result = serializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException, JsonProcessingException {
        Std serializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.createParser("{}");

        Object result = serializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @BeforeClass
    public static void setup() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }
}