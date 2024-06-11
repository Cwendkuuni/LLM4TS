
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
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
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            File result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            URL result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            URI result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            Class<?> result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            JavaType result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            Currency result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            Pattern result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            Locale result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            Charset result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            TimeZone result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            InetAddress result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            InetSocketAddress result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (InvalidFormatException ife) {
            if (ife.getType().equals(InetSocketAddress.class)) {
                fail(ife.getMessage());
            }
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void testDeserializeStringBuilder() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        try {
            JsonParser parser = null; // Replace with valid JsonParser
            DeserializationContext context = null; // Replace with valid DeserializationContext
            StringBuilder result = deserializer.deserialize(parser, context);
            // TODO: Add appropriate assertions
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}

