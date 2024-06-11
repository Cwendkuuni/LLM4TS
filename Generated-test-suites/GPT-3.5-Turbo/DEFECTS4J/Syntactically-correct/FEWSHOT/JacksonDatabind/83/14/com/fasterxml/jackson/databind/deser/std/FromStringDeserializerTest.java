package com.fasterxml.jackson.databind.deser.std.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), deserializer._deserialize("test.txt", null));
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }
    
    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }
    
    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(Integer.class, deserializer._deserialize("java.lang.Integer", null));
    }
    
    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, deserializer._deserialize("com.fasterxml.jackson.databind.JavaType", null));
    }
    
    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals("USD", deserializer._deserialize("USD", null).getCurrencyCode());
    }
    
    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals("[a-z]+", deserializer._deserialize("[a-z]+", null).pattern());
    }
    
    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en", "US"), deserializer._deserialize("en-US", null));
        assertEquals(new Locale("en", "US", "NY"), deserializer._deserialize("en-US-NY", null));
    }
    
    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer._deserialize("UTF-8", null));
    }
    
    @Test
    public void testDeserialize_timezone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer._deserialize("America/New_York", null));
    }
    
    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("localhost"), deserializer._deserialize("localhost", null));
    }
    
    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer._deserialize("localhost:8080", null));
    }
    
    @Test
    public void testDeserialize_stringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        assertEquals(new StringBuilder("test"), deserializer._deserialize("test", null));
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_unknownType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(null, 0);
        deserializer._deserialize("test", null);
    }
}