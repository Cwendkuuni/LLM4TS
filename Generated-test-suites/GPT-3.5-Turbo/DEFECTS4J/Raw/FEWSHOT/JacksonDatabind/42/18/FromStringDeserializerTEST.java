
package com.fasterxml.jackson.databind.deser.std;

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

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

@SuppressWarnings("serial")
public class FromStringDeserializerTest {

    @Test
    public void testFile() throws IOException {
        Std deserializer = new Std(File.class, Std.STD_FILE);
        
        File file = (File) deserializer._deserialize("/path/to/file", null);
        
        assertEquals("/path/to/file", file.getAbsolutePath());
    }
    
    @Test
    public void testURL() throws IOException {
        Std deserializer = new Std(URL.class, Std.STD_URL);
        
        URL url = (URL) deserializer._deserialize("https://www.example.com", null);
        
        assertEquals("https://www.example.com", url.toString());
    }
    
    @Test
    public void testURI() throws IOException {
        Std deserializer = new Std(URI.class, Std.STD_URI);
        
        URI uri = (URI) deserializer._deserialize("https://www.example.com", null);
        
        assertEquals("https://www.example.com", uri.toString());
    }
    
    @Test
    public void testClass() throws IOException {
        Std deserializer = new Std(Class.class, Std.STD_CLASS);
        
        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
        
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testJavaType() throws IOException {
        Std deserializer = new Std(JavaType.class, Std.STD_JAVA_TYPE);
        
        JavaType javaType = (JavaType) deserializer._deserialize("java.util.List<String>", null);
        
        assertEquals(JavaType.class, javaType.getClass());
        assertEquals("java.util.List<String>", javaType.toCanonical());
    }
    
    @Test
    public void testCurrency() throws IOException {
        Std deserializer = new Std(Currency.class, Std.STD_CURRENCY);
        
        Currency currency = (Currency) deserializer._deserialize("USD", null);
        
        assertEquals("USD", currency.getCurrencyCode());
    }
    
    @Test
    public void testPattern() throws IOException {
        Std deserializer = new Std(Pattern.class, Std.STD_PATTERN);
        
        Pattern pattern = (Pattern) deserializer._deserialize("[a-z]+", null);
        
        assertEquals("[a-z]+", pattern.pattern());
    }
    
    @Test
    public void testLocale() throws IOException {
        Std deserializer = new Std(Locale.class, Std.STD_LOCALE);
        
        Locale locale = (Locale) deserializer._deserialize("en_US", null);
        
        assertEquals("en_US", locale.toString());
    }
    
    @Test
    public void testCharset() throws IOException {
        Std deserializer = new Std(Charset.class, Std.STD_CHARSET);
        
        Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
        
        assertEquals("UTF-8", charset.name());
    }
    
    @Test
    public void testTimeZone() throws IOException {
        Std deserializer = new Std(TimeZone.class, Std.STD_TIME_ZONE);
        
        TimeZone timeZone = (TimeZone) deserializer._deserialize("America/New_York", null);
        
        assertEquals("America/New_York", timeZone.getID());
    }
    
    @Test
    public void testInetAddress() throws IOException {
        Std deserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
        
        InetAddress address = (InetAddress) deserializer._deserialize("192.168.0.1", null);
        
        assertEquals("192.168.0.1", address.getHostAddress());
    }
    
    @Test
    public void testInetSocketAddress() throws IOException {
        Std deserializer = new Std(InetSocketAddress.class, Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress socketAddress = (InetSocketAddress) deserializer._deserialize("127.0.0.1:8080", null);
        
        assertEquals("127.0.0.1", socketAddress.getAddress().getHostAddress());
        assertEquals(8080, socketAddress.getPort());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws IOException {
        Std deserializer = new Std(File.class, Std.STD_FILE);
        
        deserializer._deserialize("invalid_input", null);
    }
    
    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        Std deserializer = new Std(URI.class, Std.STD_URI);
        
        URI uri = (URI) deserializer._deserializeFromEmptyString();
        
        assertEquals("", uri.toString());
    }

    @Test
    public void testJsonParserDeserialization() throws IOException {
        Std deserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
        
        JsonParser jp = new JsonFactory().createParser("\"192.168.0.1\"");
        
        InetAddress address = (InetAddress) deserializer.deserialize(jp, null);
        
        assertEquals("192.168.0.1", address.getHostAddress());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testWrongTokenException() throws IOException {
        Std deserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
        
        JsonParser jp = new JsonFactory().createParser("[\"192.168.0.1\"]");
        
        deserializer.deserialize(jp, null);
    }
    
    @Test
    public void testDeserializeEmbedded() throws IOException {
        Std deserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
        
        InetAddress address = (InetAddress) deserializer._deserializeEmbedded(InetAddress.getLocalHost(), null);
        
        assertEquals(InetAddress.getLocalHost(), address);
    }
    
    @Test
    public void testDeserializeFromEmptyStringEmbedded() throws IOException {
        Std deserializer = new Std(URI.class, Std.STD_URI);
        
        URI uri = (URI) deserializer._deserializeFromEmptyString();
        
        assertEquals("", uri.toString());
    }
}
