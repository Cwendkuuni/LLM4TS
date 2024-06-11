package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer.deserialize(null, null);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void test_deserialize_successful() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer.deserialize(new JsonParser(null, null), new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void test_deserialize_Exception() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer.deserialize(new JsonParser(null, null), null);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    @Test
    public void test_deserializeEmbedded_Success() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserializeEmbedded(null, new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void test_deserializeFromEmptyString_Success() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserializeFromEmptyString();
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void test_Std_deserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("test string", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_EmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("");
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("USD", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("[0-9]{3}", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("en_US", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("UTF-8", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("GMT+5:30", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("127.0.0.1", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_InetSocketAddress_IPv4() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("127.0.0.1:8080", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_Std_deserialize_InetSocketAddress_IPv6() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        try {
            deserializer._deserialize("[::1]:8080", new DeserializationContext(null, null));
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }

    @Test
    public void test_Std_deserialize_FromEmptyString_WithURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_URI);
        
        try {
            deserializer._deserializeFromEmptyString();
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_findDeserializer() {
        Class<?> rawType = File.class;
        
        try {
            FromStringDeserializer.findDeserializer(rawType);
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
    
    @Test
    public void test_findDeserializer_Null() {
        Class<?> rawType = null;
        
        try {
            FromStringDeserializer.findDeserializer(rawType);
            fail("Expected NullPointerException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
}