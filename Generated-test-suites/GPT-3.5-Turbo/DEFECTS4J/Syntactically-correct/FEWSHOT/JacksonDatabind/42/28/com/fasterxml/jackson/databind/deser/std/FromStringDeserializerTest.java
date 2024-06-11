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

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File file = (File) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(file);
    }
    
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL url = (URL) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(url);
    }
    
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI uri = (URI) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(uri);
    }
    
    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        
        Class<?> clazz = (Class<?>) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(clazz);
    }
    
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        
        JavaType javaType = (JavaType) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(javaType);
    }
    
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency currency = (Currency) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(currency);
    }
    
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern pattern = (Pattern) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(pattern);
    }
    
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale locale = (Locale) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(locale);
    }
    
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset charset = (Charset) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(charset);
    }
    
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone timeZone = (TimeZone) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(timeZone);
    }
    
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(inetAddress);
    }
    
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(new FakeJsonParser(), null);
        
        assertNull(inetSocketAddress);
    }
    
    private class FakeJsonParser extends JsonParser {

        @Override
        public String getText() throws IOException {
            return "";
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return null;
        }
        
    }
}