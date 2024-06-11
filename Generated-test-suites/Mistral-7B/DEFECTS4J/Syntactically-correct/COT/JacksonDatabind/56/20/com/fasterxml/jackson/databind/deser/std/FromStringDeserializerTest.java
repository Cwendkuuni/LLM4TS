package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper;

    @org.junit.BeforeClass
    public static void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.findDeserializer(File.class));
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
        module.addDeserializer(Class.class, FromStringDeserializer.findDeserializer(Class.class));
        module.addDeserializer(JavaType.class, FromStringDeserializer.findDeserializer(JavaType.class));
        module.addDeserializer(Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        module.addDeserializer(Pattern.class, FromStringDeserializer.findDeserializer(Pattern.class));
        module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        module.addDeserializer(Charset.class, FromStringDeserializer.findDeserializer(Charset.class));
        module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
        module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @org.junit.Test
    public void testDeserializeFromString() throws IOException, JsonProcessingException {
        // Test deserializing supported types from String
        testDeserialize("java.io.File", "path/to/file.txt", new File("path/to/file.txt"));
        testDeserialize("java.net.URL", "http://example.com", new URL("http://example.com"));
        testDeserialize("java.net.URI", "http://example.com", new URI("http://example.com"));
        testDeserialize("java.lang.Class", "java.util.Date", Date.class);
        testDeserialize("com.fasterxml.jackson.databind.JavaType", "java.util.List<java.lang.String>", JavaType.constructParametricType(List.class, String.class));
        testDeserialize("java.util.Currency", "USD", Currency.getInstance("USD"));
        testDeserialize("java.util.regex.Pattern", "/[a-z]+/i", Pattern.compile("/[a-z]+/i"));
        testDeserialize("java.util.Locale", "en_US", Locale.US);
        testDeserialize("java.nio.charset.Charset", "UTF-8", Charset.forName("UTF-8"));
        testDeserialize("java.util.TimeZone", "America/Los_Angeles", TimeZone.getTimeZone("America/Los_Angeles"));
        testDeserialize("java.net.InetAddress", "192.168.1.1", InetAddress.getByName("192.168.1.1"));
        testDeserialize("java.net.InetSocketAddress", "[::1]:8080", new InetSocketAddress(InetAddress.getByName("::1"), 8080));
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        // Test deserializing empty String for different types
        testDeserializeEmptyString(File.class, null);
        testDeserializeEmptyString(URL.class, null);
        testDeserializeEmptyString(URI.class, new URI(""));
        testDeserializeEmptyString(Class.class, null);
        testDeserializeEmptyString(JavaType.class, null);
        testDeserializeEmptyString(Currency.class, null);
        testDeserializeEmptyString(Pattern.class, null);
        testDeserializeEmptyString(Locale.class, Locale.ROOT);
        testDeserializeEmptyString(Charset.class, Charset.forName(""));
        testDeserializeEmptyString(TimeZone.class, TimeZone.getDefault());
        testDeserializeEmptyString(InetAddress.class, InetAddress.getByAddress(new byte[0]));
        testDeserializeEmptyString(InetSocketAddress.class, new InetSocketAddress(InetAddress.getByAddress(new byte[0]), 0));
    }

    @org.junit.Test
    public void testDeserializeNullString() throws IOException {
        // Test deserializing null String
        testDeserializeNullString(String.class, null);
        testDeserializeNullString(File.class, null);
        testDeserializeNullString(URL.class, null);
        testDeserializeNullString(URI.class, null);
        testDeserializeNullString(Class.class, null);
        testDeserializeNullString(JavaType.class, null);
        testDeserializeNullString(Currency.class, null);
        testDeserializeNullString(Pattern.class, null);
        testDeserializeNullString(Locale.class, null);
        testDeserializeNullString(Charset.class, null);
        testDeserializeNullString(TimeZone.class, null);
        testDeserializeNullString(InetAddress.class, null);
        testDeserializeNullString(InetSocketAddress.class, null);
    }

    @org.junit.Test
    public void testDeserializeInvalidFormat() throws IOException, JsonMappingException {
        // Test deserialization with invalid format
        testDeserializeInvalidFormat("java.io.File", "invalid", JsonMappingException.class);
        testDeserializeInvalidFormat("java.net.URL", "invalid:/", JsonMappingException.class);
        testDeserializeInvalidFormat("java.net.URI", "invalid", JsonMappingException.class);
        testDeserializeInvalidFormat("java.lang.Class", "java.util.Date", IllegalArgumentException.class);
        testDeserializeInvalidFormat("com.fasterxml.jackson.databind.JavaType", "java.util.List<java.lang.String>", IllegalArgumentException.class);
        testDeserializeInvalidFormat("java.util.Currency", "USD$", JsonMappingException.class);
        testDeserializeInvalidFormat("java.util.regex.Pattern", "/[a-z]+", JsonMappingException.class);
        testDeserializeInvalidFormat("java.util.Locale", "", JsonMappingException.class);
        testDeserializeInvalidFormat("java.nio.charset.Charset", "", JsonMappingException.class);
        testDeserializeInvalidFormat("java.util.TimeZone", "GMT-05:00", JsonMappingException.class);
        testDeserializeInvalidFormat("java.net.InetAddress", "192.168.1.1:80", JsonMappingException.class);
        testDeserializeInvalidFormat("java.net.InetSocketAddress", "[::1]:8080:80", JsonMappingException.class);
    }

    @org.junit.Test
    public void testDeserializeUnsupportedType() throws IOException, JsonMappingException {
        // Test deserialization with unsupported type
        testDeserializeUnsupportedType("java.util.ArrayList", new ArrayList<>(), JsonMappingException.class);
    }

    private void testDeserialize(String className, String jsonString, Object expectedObject) throws IOException, JsonProcessingException {
        Object actualObject = mapper.readValue(jsonString, Class.forName(className));
        assertEquals(expectedObject, actualObject);
    }

    private void testDeserializeEmptyString(Class<?> clazz, Object expectedObject) throws IOException {
        Object actualObject = mapper.readValue(new String(""), clazz);
        assertEquals(expectedObject, actualObject);
    }

    private void testDeserializeNullString(Class<?> clazz, Object expectedObject) throws IOException {
        Object actualObject = mapper.readValue(null, clazz);
        assertNull(actualObject);
    }

    private void testDeserializeInvalidFormat(String className, String jsonString, Class<?> expectedException) throws IOException, JsonMappingException {
        assertThrows(expectedException, () -> mapper.readValue(jsonString, Class.forName(className)));
    }

    private void testDeserializeUnsupportedType(String className, Object expectedObject, Class<?> expectedException) throws IOException, JsonMappingException {
        assertThrows(expectedException, () -> mapper.readValue(new String("[]"), Class.forName(className)));
    }
}