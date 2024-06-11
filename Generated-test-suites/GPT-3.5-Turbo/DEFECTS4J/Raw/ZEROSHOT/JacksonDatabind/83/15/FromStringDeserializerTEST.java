package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), deserializer._deserialize("test.txt", null));
    }
    
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }
    
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }
    
    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, deserializer._deserialize("java.lang.String", null));
    }
    
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, deserializer._deserialize("com.fasterxml.jackson.databind.JavaType", null));
    }
    
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), deserializer._deserialize("USD", null));
    }
    
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("\\d+"), deserializer._deserialize("\\d+", null));
    }
    
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.US, deserializer._deserialize("en-US", null));
    }
    
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer._deserialize("UTF-8", null));
    }
    
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer._deserialize("America/New_York", null));
    }
    
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer._deserialize("127.0.0.1", null));
    }
    
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer._deserialize("localhost:8080", null));
    }
    
    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        assertEquals(new StringBuilder("test"), deserializer._deserialize("test", null));
    }
    
    @Test
    public void testDeserializeInvalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
        try {
            deserializer._deserialize("invalid", null);
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation, problem: invalid", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeNoStringValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
        assertNull(deserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeEmbedded() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals("embedded", deserializer._deserializeEmbedded("embedded", null));
    }
    
    @Test
    public void testDeserializeFromEmptyStringURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), deserializer._deserializeFromEmptyString());
    }
    
    @Test
    public void testDeserializeFromEmptyStringLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.ROOT, deserializer._deserializeFromEmptyString());
    }
    
    @Test
    public void testDeserializeFromEmptyStringStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        assertNotNull(deserializer._deserializeFromEmptyString());
    }
}