package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        File file = (File) deserializer._deserialize("test.txt", null);

        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        URL url = (URL) deserializer._deserialize("http://www.example.com", null);

        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        URI uri = (URI) deserializer._deserialize("http://www.example.com", null);

        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);

        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        // Test serialization and deserialization of JavaType
        ObjectReader reader = new ObjectMapper().readerFor(JavaType.class);
        JavaType originalValue = reader.readValue("\"java.lang.String\"");
        String serializedValue = reader.writeValueAsString(originalValue);
        JavaType deserializedValue = (JavaType) deserializer._deserialize(serializedValue, null);

        assertEquals(originalValue, deserializedValue);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        Currency currency = (Currency) deserializer._deserialize("USD", null);

        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        Pattern pattern = (Pattern) deserializer._deserialize("\\d{3}-\\d{3}", null);

        assertEquals("\\d{3}-\\d{3}", pattern.pattern());
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Locale locale = (Locale) deserializer._deserialize("en_US", null);

        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        Charset charset = (Charset) deserializer._deserialize("UTF-8", null);

        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        TimeZone timeZone = (TimeZone) deserializer._deserialize("GMT", null);

        assertEquals(TimeZone.getTimeZone("GMT"), timeZone);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        InetAddress inetAddress = (InetAddress) deserializer._deserialize("127.0.0.1", null);

        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        // Test deserialization of IPv4 address
        InetSocketAddress inetSocketAddress1 = (InetSocketAddress) deserializer._deserialize("127.0.0.1:8080", null);
        assertEquals("127.0.0.1", inetSocketAddress1.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress1.getPort());

        // Test deserialization of IPv6 address
        InetSocketAddress inetSocketAddress2 = (InetSocketAddress) deserializer._deserialize("[::1]:8080", null);
        assertEquals("::1", inetSocketAddress2.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress2.getPort());
    }
}