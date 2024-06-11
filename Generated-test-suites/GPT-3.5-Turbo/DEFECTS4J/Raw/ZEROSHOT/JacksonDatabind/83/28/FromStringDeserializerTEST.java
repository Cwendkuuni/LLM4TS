package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        File file = (File) deserializer.deserialize(null, null);
        assertNull(file);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL url = (URL) deserializer.deserialize(null, null);
        assertNull(url);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI uri = (URI) deserializer.deserialize(null, null);
        assertNull(uri);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class<?> cls = (Class<?>) deserializer.deserialize(null, null);
        assertNull(cls);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType javaType = (JavaType) deserializer.deserialize(null, null);
        assertNull(javaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        Pattern pattern = (Pattern) deserializer.deserialize(null, null);
        assertNull(pattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) deserializer.deserialize(null, null);
        assertNull(locale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset charset = (Charset) deserializer.deserialize(null, null);
        assertNull(charset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(null, null);
        assertNull(timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(null, null);
        assertNull(inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
        assertNull(inetSocketAddress);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(null, null);
        assertNull(stringBuilder);
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        Object obj = deserializer.deserialize(new TestJsonParser(""), null);
        assertNull(obj);
    }

    @Test
    public void testDeserializeIllegalValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        TestJsonParser parser = new TestJsonParser("illegal");
        try {
            deserializer.deserialize(parser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }
    }

    private static class TestJsonParser extends JsonParser {
        
        private final String value;
        
        public TestJsonParser(String value) {
            this.value = value;
        }
        
        @Override
        public String getValueAsString() {
            return value;
        }
        
        @Override
        public JsonToken getCurrentToken() {
            return null;
        }
        
        @Override
        public Object getEmbeddedObject() {
            return null;
        }
        
        @Override
        public void clearCurrentToken() {
            
        }
        
        @Override
        public boolean isExpectedStartArrayToken() {
            return false;
        }
        
        @Override
        public boolean isExpectedStartObjectToken() {
            return false;
        }
        
        @Override
        public void close() throws IOException {
            
        }
        
        @Override
        public Version version() {
            return null;
        }
        
        @Override
        public JsonParser skipChildren() {
            return null;
        }
        
    }

}