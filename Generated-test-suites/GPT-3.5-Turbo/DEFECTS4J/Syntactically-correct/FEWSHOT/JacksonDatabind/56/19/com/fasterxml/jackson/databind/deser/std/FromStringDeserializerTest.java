
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer.deserialize(createJsonParser("test.txt"), createContext());
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer.deserialize(createJsonParser("http://example.com"), createContext());
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer.deserialize(createJsonParser("http://example.com"), createContext());
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    public void testDeserialize_Class() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer.deserialize(createJsonParser("java.lang.String"), createContext());
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_Object_Embedded_Null() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_BUFFERED_IMAGE);
        Object obj = deserializer.deserialize(createJsonParser("null"), createContext());
        assertNull(obj);
    }

    @Test
    public void testDeserialize_Object_Embedded_NonNull() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_BUFFERED_IMAGE);
        Object obj = deserializer.deserialize(createJsonParser("{\"width\": 100, \"height\": 100}"), createContext());
        assertTrue(obj instanceof Object);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InvalidFormat() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        deserializer.deserialize(createJsonParser("@#$%"), createContext());
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer.deserialize(createJsonParser("en_US"), createContext());
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer.deserialize(createJsonParser("UTF-8"), createContext());
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(createJsonParser("GMT+2"), createContext());
        assertEquals("GMT+02:00", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(createJsonParser("localhost"), createContext());
        assertEquals("localhost", inetAddress.getHostName());
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv4() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(createJsonParser("127.0.0.1:80"), createContext());
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(createJsonParser("[::1]:8080"), createContext());
        assertEquals("::1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    private JsonParser createJsonParser(String json) throws JsonParseException, IOException {
        return new com.fasterxml.jackson.core.json.ReaderBasedJsonParser(null, null, null, null, null);
    }

    private DeserializationContext createContext() {
        return null;
    }

}
