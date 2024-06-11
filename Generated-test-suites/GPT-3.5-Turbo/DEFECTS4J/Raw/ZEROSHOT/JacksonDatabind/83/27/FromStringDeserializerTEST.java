package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File("path/to/file");
        File actual = (File) deserializer._deserialize("path/to/file", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_url() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL("http://www.example.com");
        URL actual = (URL) deserializer._deserialize("http://www.example.com", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_uri() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = new URI("http://www.example.com");
        URI actual = (URI) deserializer._deserialize("http://www.example.com", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_class() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expected = String.class;
        Class<?> actual = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_javaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = TypeFactory.defaultInstance().constructFromCanonical("java.util.List");
        JavaType actual = (JavaType) deserializer._deserialize("java.util.List", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_currency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance("USD");
        Currency actual = (Currency) deserializer._deserialize("USD", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_pattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile("[a-z]+");
        Pattern actual = (Pattern) deserializer._deserialize("[a-z]+", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_locale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale("en", "US");
        Locale actual = (Locale) deserializer._deserialize("en_US", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_charset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.forName("UTF-8");
        Charset actual = (Charset) deserializer._deserialize("UTF-8", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_timeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone actual = (TimeZone) deserializer._deserialize("UTC", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_inetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        InetAddress actual = (InetAddress) deserializer._deserialize("127.0.0.1", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress actual = (InetSocketAddress) deserializer._deserialize("localhost:8080", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_stringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder expected = new StringBuilder("test");
        StringBuilder actual = (StringBuilder) deserializer._deserialize("test", null);
        assertEquals(expected.toString(), actual.toString());
    }

}