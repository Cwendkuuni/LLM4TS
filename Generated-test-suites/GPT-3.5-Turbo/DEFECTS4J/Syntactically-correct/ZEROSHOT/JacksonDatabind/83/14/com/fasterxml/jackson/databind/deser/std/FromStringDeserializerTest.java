
package com.fasterxml.jackson.databind.deser.std;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File result = (File)deserializer._deserialize("/path/to/file", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("/path/to/file", result.getPath());
    }
    
    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL result = (URL)deserializer._deserialize("http://www.example.com", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("http://www.example.com", result.toString());
    }
    
    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI result = (URI)deserializer._deserialize("http://www.example.com", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("http://www.example.com", result.toString());
    }
    
    @Test
    public void testDeserialize_Class() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        
        Class<?> result = (Class<?>)deserializer._deserialize("java.lang.String", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }
    
    @Test
    public void testDeserialize_JavaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        
        JavaType result = (JavaType)deserializer._deserialize("java.util.List<String>", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("java.util.List<java.lang.String>", result.toString());
    }
    
    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency result = (Currency)deserializer._deserialize("USD", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("USD", result.getCurrencyCode());
    }
    
    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern result = (Pattern)deserializer._deserialize("[a-z]+", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("[a-z]+", result.pattern());
    }
    
    @Test
    public void testDeserialize_Locale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale result = (Locale)deserializer._deserialize("en_US", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset result = (Charset)deserializer._deserialize("UTF-8", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("UTF-8", result.name());
    }
    
    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone result = (TimeZone)deserializer._deserialize("America/New_York", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("America/New_York", result.getID());
    }
    
    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress result = (InetAddress)deserializer._deserialize("127.0.0.1", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("127.0.0.1", result.getHostAddress());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress result = (InetSocketAddress)deserializer._deserialize("localhost:8080", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("localhost", result.getHostName());
        Assert.assertEquals(8080, result.getPort());
    }
    
    @Test
    public void testDeserialize_StringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        
        StringBuilder result = (StringBuilder)deserializer._deserialize("Hello, World!", null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("Hello, World!", result.toString());
    }
}
