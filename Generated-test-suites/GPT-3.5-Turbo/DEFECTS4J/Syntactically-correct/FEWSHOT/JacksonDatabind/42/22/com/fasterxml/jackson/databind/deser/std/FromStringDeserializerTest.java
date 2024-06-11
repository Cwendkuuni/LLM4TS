package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.apache.commons.cli.*;
import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        FromStringDeserializer.Std deserializer = null;
        Object result = null;

        // Test FILE
        deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        result = deserializer._deserialize("/path/to/file", null);
        assertEquals(File.class, result.getClass());
        assertEquals(new File("/path/to/file"), result);

        // Test URL
        deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        result = deserializer._deserialize("http://www.example.com", null);
        assertEquals(URL.class, result.getClass());
        assertEquals(new URL("http://www.example.com"), result);

        // Test URI
        deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        result = deserializer._deserialize("http://www.example.com", null);
        assertEquals(URI.class, result.getClass());
        assertEquals(new URI("http://www.example.com"), result);

        // Test CLASS
        deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        result = deserializer._deserialize("java.lang.String", null);
        assertEquals(Class.class, result.getClass());
        assertEquals(String.class, result);

        // Test JAVA_TYPE
        deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        result = deserializer._deserialize("java.util.List<java.lang.String>", null);
        assertEquals(JavaType.class, result.getClass());
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(List.class, String.class), result);

        // Test CURRENCY
        deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        result = deserializer._deserialize("USD", null);
        assertEquals(Currency.class, result.getClass());
        assertEquals(Currency.getInstance("USD"), result);

        // Test PATTERN
        deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        result = deserializer._deserialize("[a-z]+", null);
        assertEquals(Pattern.class, result.getClass());
        assertEquals(Pattern.compile("[a-z]+"), result);

        // Test LOCALE
        deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        result = deserializer._deserialize("en_US", null);
        assertEquals(Locale.class, result.getClass());
        assertEquals(Locale.US, result);

        // Test CHARSET
        deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        result = deserializer._deserialize("UTF-8", null);
        assertEquals(Charset.class, result.getClass());
        assertEquals(Charset.forName("UTF-8"), result);

        // Test TIME_ZONE
        deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        result = deserializer._deserialize("America/New_York", null);
        assertEquals(TimeZone.class, result.getClass());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);

        // Test INET_ADDRESS
        deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        result = deserializer._deserialize("127.0.0.1", null);
        assertEquals(InetAddress.class, result.getClass());
        assertEquals(InetAddress.getByName("127.0.0.1"), result);

        // Test INET_SOCKET_ADDRESS
        deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        result = deserializer._deserialize("localhost:8080", null);
        assertEquals(InetSocketAddress.class, result.getClass());
        assertEquals(new InetSocketAddress("localhost", 8080), result);
    }
}