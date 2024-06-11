package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        File result = (File) deserializer._deserialize("test.txt", null);
        assertNotNull(result);
        assertEquals("test.txt", result.getPath());
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        URL result = (URL) deserializer._deserialize("https://www.example.com", null);
        assertNotNull(result);
        assertEquals("https://www.example.com", result.toString());
    }
    
    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        URI result = (URI) deserializer._deserialize("https://www.example.com/path", null);
        assertNotNull(result);
        assertEquals("https://www.example.com/path", result.toString());
    }
    
    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        Class<?> result = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        JavaType result = (JavaType) deserializer._deserialize("java.util.List<java.lang.String>", null);
        assertNotNull(result);
        assertEquals(JavaType.class, result.getClass());
        assertEquals("java.util.List<java.lang.String>", result.getGenericSignature());
    }
    
    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        Currency result = (Currency) deserializer._deserialize("USD", null);
        assertNotNull(result);
        assertEquals("USD", result.getCurrencyCode());
    }
    
    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        Pattern result = (Pattern) deserializer._deserialize("^\\d+$", null);
        assertNotNull(result);
        assertEquals("^\\d+$", result.pattern());
    }
    
    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Locale result = (Locale) deserializer._deserialize("en-US", null);
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        Charset result = (Charset) deserializer._deserialize("UTF-8", null);
        assertNotNull(result);
        assertEquals("UTF-8", result.name());
    }
    
    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        TimeZone result = (TimeZone) deserializer._deserialize("GMT+00:00", null);
        assertNotNull(result);
        assertEquals("GMT+00:00", result.getID());
    }
    
    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        InetAddress result = (InetAddress) deserializer._deserialize("192.0.2.1", null);
        assertNotNull(result);
        assertEquals("192.0.2.1", result.getHostAddress());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_IPv4() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress result = (InetSocketAddress) deserializer._deserialize("192.0.2.1:8080", null);
        assertNotNull(result);
        assertEquals("192.0.2.1", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_IPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress result = (InetSocketAddress) deserializer._deserialize("[2001:db8::1]:8080", null);
        assertNotNull(result);
        assertEquals("2001:db8::1", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }
    
    @Test
    public void testDeserialize_StringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        StringBuilder result = (StringBuilder) deserializer._deserialize("Hello, world!", null);
        assertNotNull(result);
        assertEquals("Hello, world!", result.toString());
    }
}