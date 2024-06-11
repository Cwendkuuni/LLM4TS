package com.fasterxml.jackson.databind.deser.std;

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

import org.junit.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            File file = (File) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(file);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            URL url = (URL) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(url);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            URI uri = (URI) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(uri);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            Class<?> clazz = (Class<?>) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(clazz);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            JavaType javaType = (JavaType) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(javaType);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            Currency currency = (Currency) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(currency);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            Pattern pattern = (Pattern) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(pattern);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            Locale locale = (Locale) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(locale);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            Charset charset = (Charset) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(charset);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            TimeZone timeZone = (TimeZone) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(timeZone);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            InetAddress inetAddress = (InetAddress) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(inetAddress);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        try {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(JsonParser.VALUE_STRING, context);
            assertNotNull(inetSocketAddress);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}