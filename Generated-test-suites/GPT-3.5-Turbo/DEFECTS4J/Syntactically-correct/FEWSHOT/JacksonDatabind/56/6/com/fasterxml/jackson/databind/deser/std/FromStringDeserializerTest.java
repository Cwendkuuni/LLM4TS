package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
    @Test
    public void testStdFile() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File file = (File) stdDeserializer._deserialize("/path/to/file", null);
        
        assertEquals(new File("/path/to/file"), file);
    }
    
    @Test
    public void testStdURL() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL url = (URL) stdDeserializer._deserialize("http://www.example.com", null);
        
        assertEquals(new URL("http://www.example.com"), url);
    }
    
    @Test
    public void testStdURI() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI uri = (URI) stdDeserializer._deserialize("http://www.example.com", null);
        
        assertEquals(new URI("http://www.example.com"), uri);
    }
    
    @Test
    public void testStdJavaType() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        
        JavaType javaType = (JavaType) stdDeserializer._deserialize("java.util.List", null);
        
        assertEquals(JavaType.construct(Collection.class), javaType);
    }
    
    @Test
    public void testStdCurrency() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency currency = (Currency) stdDeserializer._deserialize("USD", null);
        
        assertEquals(Currency.getInstance("USD"), currency);
    }
    
    @Test
    public void testStdPattern() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern pattern = (Pattern) stdDeserializer._deserialize("[a-z]+", null);
        
        assertEquals(Pattern.compile("[a-z]+"), pattern);
    }
    
    @Test
    public void testStdLocale() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale locale = (Locale) stdDeserializer._deserialize("en_US", null);
        
        assertEquals(new Locale("en", "US"), locale);
    }
    
    @Test
    public void testStdCharset() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset charset = (Charset) stdDeserializer._deserialize("UTF-8", null);
        
        assertEquals(Charset.forName("UTF-8"), charset);
    }
    
    @Test
    public void testStdTimeZone() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone timeZone = (TimeZone) stdDeserializer._deserialize("GMT", null);
        
        assertEquals(TimeZone.getTimeZone("GMT"), timeZone);
    }
    
    @Test
    public void testStdInetAddress() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress inetAddress = (InetAddress) stdDeserializer._deserialize("127.0.0.1", null);
        
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddress);
    }
    
    @Test
    public void testStdInetSocketAddress() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress inetSocketAddress = (InetSocketAddress) stdDeserializer._deserialize("127.0.0.1:8080", null);
        
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddress);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStdInvalid() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Object.class, 999);
        
        stdDeserializer._deserialize("", null);
    }
    
    @Test
    public void testStdDeserializeFromEmptyStringForUri() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI uri = (URI) stdDeserializer._deserializeFromEmptyString();
        
        assertEquals(URI.create(""), uri);
    }
    
    @Test
    public void testStdDeserializeFromEmptyStringForLocale() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale locale = (Locale) stdDeserializer._deserializeFromEmptyString();
        
        assertEquals(Locale.ROOT, locale);
    }
    
    @Test
    public void testFromEmptyString() throws Exception {
        FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer<Object>(Object.class) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };
        
        assertNull(fromStringDeserializer._deserializeFromEmptyString());
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testStdDeserialize() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Object.class, 123);
        
        stdDeserializer.deserialize(null, null);
    }
}