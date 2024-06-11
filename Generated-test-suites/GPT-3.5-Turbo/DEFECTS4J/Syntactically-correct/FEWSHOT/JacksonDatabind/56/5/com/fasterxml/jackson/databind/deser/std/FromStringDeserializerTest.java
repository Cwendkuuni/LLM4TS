package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("example.txt"), deserializer._deserialize("example.txt", null));
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, deserializer._deserialize("java.lang.String", null));
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        // Implement this test case after knowing the expected value
        fail("Not implemented yet");
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), deserializer._deserialize("USD", null));
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("\\d+"), deserializer._deserialize("\\d+", null));
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en"), deserializer._deserialize("en", null));
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer._deserialize("UTF-8", null));
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer._deserialize("UTC", null));
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("localhost"), deserializer._deserialize("localhost", null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPV4() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), deserializer._deserialize("127.0.0.1:8080", null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPV6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("[::1]", 8080), deserializer._deserialize("[::1]:8080", null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_Hostname() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer._deserialize("localhost:8080", null));
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_Malformed() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        deserializer._deserialize(":8080", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_UnknownType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 13); // Invalid kind
        deserializer._deserialize("123", null);
    }
}