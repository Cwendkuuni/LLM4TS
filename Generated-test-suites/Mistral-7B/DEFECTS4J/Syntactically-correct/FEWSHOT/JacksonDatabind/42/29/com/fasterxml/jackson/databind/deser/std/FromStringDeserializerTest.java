package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization for various types
        for (Class<?> clazz : FromStringDeserializer.types()) {
            String json = null;
            if (clazz == File.class) {
                json = "path/to/file.txt";
            } else if (clazz == URL.class) {
                json = "http://example.com";
            } else if (clazz == URI.class) {
                json = "http://example.com";
            } else if (clazz == Class.class) {
                json = "java.lang.String.class";
            } else if (clazz == JavaType.class) {
                json = "com.fasterxml.jackson.core.type.TypeReference<java.util.List<java.lang.String>>";
            } else if (clazz == Currency.class) {
                json = "USD";
            } else if (clazz == Pattern.class) {
                json = "(.+)@example.com";
            } else if (clazz == Locale.class) {
                json = "en_US";
            } else if (clazz == Charset.class) {
                json = "UTF-8";
            } else if (clazz == TimeZone.class) {
                json = "America/New_York";
            } else if (clazz == InetAddress.class) {
                json = "192.168.0.1";
            } else if (clazz == InetSocketAddress.class) {
                json = "192.168.0.1:8080";
            }

            JsonParser jp = mapper.getFactory().createParser(new StringReader(json));
            DeserializationContext ctxt = mapper.getDeserializationContext();
            FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(clazz);
            Object result = deserializer.deserialize(jp, ctxt);

            // Validate the result based on the expected type
            if (clazz == File.class) {
                assertInstanceOf(File.class, result);
            } else if (clazz == URL.class) {
                assertInstanceOf(URL.class, result);
                assertEquals("http://example.com", result.toString());
            } else if (clazz == URI.class) {
                assertInstanceOf(URI.class, result);
                assertEquals("http://example.com", result.toURI().toURL().toString());
            } else if (clazz == Class.class) {
                assertInstanceOf(Class.class, result);
                assertEquals(String.class, result.asSubClass(null));
            } else if (clazz == JavaType.class) {
                assertInstanceOf(JavaType.class, result);
                assertEquals(new TypeReference<List<String>>(){}.getType(), result);
            } else if (clazz == Currency.class) {
                assertInstanceOf(Currency.class, result);
                assertEquals(Currency.getInstance("USD"), result);
            } else if (clazz == Pattern.class) {
                assertInstanceOf(Pattern.class, result);
                assertEquals("(.+)@example.com", result.pattern().toString());
            } else if (clazz == Locale.class) {
                assertInstanceOf(Locale.class, result);
                assertEquals(Locale.US, result);
            } else if (clazz == Charset.class) {
                assertInstanceOf(Charset.class, result);
                assertEquals(Charset.forName("UTF-8"), result);
            } else if (clazz == TimeZone.class) {
                assertInstanceOf(TimeZone.class, result);
                assertEquals(TimeZone.getTimeZone("America/New_York"), result);
            } else if (clazz == InetAddress.class) {
                assertInstanceOf(InetAddress.class, result);
                assertEquals("192.168.0.1", result.getHostAddress());
            } else if (clazz == InetSocketAddress.class) {
                assertInstanceOf(InetSocketAddress.class, result);
                InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
                assertEquals("192.168.0.1", inetSocketAddress.getAddress().getHostAddress());
                assertEquals(8080, inetSocketAddress.getPort());
            }
        }
    }
}