package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
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

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) deserializer.deserialize(null, createContext("file.txt"));
        assertEquals("file.txt", result.getPath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) deserializer.deserialize(null, createContext("http://example.com"));
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) deserializer.deserialize(null, createContext("http://example.com"));
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> result = (Class<?>) deserializer.deserialize(null, createContext(String.class.getName()));
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType result = (JavaType) deserializer.deserialize(null, createContext("java.lang.String"));
        assertEquals(JavaType.class, result.getClass());
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency result = (Currency) deserializer.deserialize(null, createContext("USD"));
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern result = (Pattern) deserializer.deserialize(null, createContext("[a-z]+"));
        assertTrue(result.matcher("abc").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) deserializer.deserialize(null, createContext("en_US"));
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset result = (Charset) deserializer.deserialize(null, createContext("UTF-8"));
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone result = (TimeZone) deserializer.deserialize(null, createContext("America/Los_Angeles"));
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress result = (InetAddress) deserializer.deserialize(null, createContext("127.0.0.1"));
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(null, createContext("localhost:8080"));
        assertEquals("localhost", result.getHostString());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testDeserializeInvalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            deserializer.deserialize(null, createContext(""));
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("not a valid textual representation", e.getMessage());
        }
    }

    private DeserializationContext createContext(String value) throws IOException {
        JsonParser jp = new DummyJsonParser(value);
        return new DeserializationContext(new DummyObjectCodec(), new MyDeserializationConfig(), jp);
    }

    private static class DummyJsonParser extends JsonParser {
        private final String value;

        public DummyJsonParser(String value) {
            this.value = value;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return null;
        }

        @Override
        public String getValueAsString() throws IOException {
            return value;
        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }

        @Override
        public Object getEmbeddedObject() throws IOException {
            return null;
        }
    }

    private static class DummyObjectCodec extends com.fasterxml.jackson.databind.ObjectCodec {
        @Override
        public <T> T readValue(JsonParser p, Class<T> valueType) throws IOException {
            return null;
        }
    }

    private static class MyDeserializationConfig extends com.fasterxml.jackson.databind.DeserializationConfig {
        @Override
        public DeserializationFeature getConfiguredDeserializationFeatures() {
            return null;
        }
    }
}