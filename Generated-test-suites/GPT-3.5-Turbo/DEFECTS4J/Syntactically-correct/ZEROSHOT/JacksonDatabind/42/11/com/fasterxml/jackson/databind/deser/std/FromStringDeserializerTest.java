
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws Exception {
        File expected = new File("test.txt");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) deserializer._deserialize("test.txt", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        URL expected = new URL("http://www.example.com");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) deserializer._deserialize("http://www.example.com", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        URI expected = new URI("http://www.example.com");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) deserializer._deserialize("http://www.example.com", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Class() throws Exception {
        Class<?> expected = String.class;
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> result = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_JavaType() throws Exception {
        JavaType expected = new JavaType(String.class);
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType result = (JavaType) deserializer._deserialize("java.lang.String", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Currency() throws Exception {
        Currency expected = Currency.getInstance("USD");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency result = (Currency) deserializer._deserialize("USD", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Pattern() throws Exception {
        Pattern expected = Pattern.compile("\\d+");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern result = (Pattern) deserializer._deserialize("\\d+", null);
        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserialize_Locale() throws Exception {
        Locale expected = new Locale("en", "US");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) deserializer._deserialize("en_US", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        Charset expected = Charset.forName("UTF-8");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset result = (Charset) deserializer._deserialize("UTF-8", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        TimeZone expected = TimeZone.getTimeZone("UTC");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone result = (TimeZone) deserializer._deserialize("UTC", null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        InetAddress expected = InetAddress.getByName("localhost");
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress result = (InetAddress) deserializer._deserialize("localhost", null);
        assertArrayEquals(expected.getAddress(), result.getAddress());
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress result = (InetSocketAddress) deserializer._deserialize("localhost:8080", null);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidType() throws Exception {
        FromStringDeserializer.Std deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer._deserialize("test", null);
    }
}
