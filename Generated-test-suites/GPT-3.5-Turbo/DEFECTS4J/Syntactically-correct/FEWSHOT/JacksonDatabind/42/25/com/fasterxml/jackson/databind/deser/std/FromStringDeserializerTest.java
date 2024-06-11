package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("filepath"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expectedURL = new URL("https://www.example.com");
        assertEquals(expectedURL, deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("https://www.example.com"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("[0-9]+"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en"), deserializer.deserialize(null, null));
        assertEquals(new Locale("en", "US"), deserializer.deserialize(null, null));
        assertEquals(new Locale("en", "US", "NY"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("GMT"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("localhost"), deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(null, null));
        assertEquals(new InetSocketAddress("example.com", 0), deserializer.deserialize(null, null));
        assertEquals(new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 8080), deserializer.deserialize(null, null));
        // test error case
        FromStringDeserializer.Std deserializer2 = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        try {
            deserializer2.deserialize(null, null);
            assertFalse(true);
        } catch (InvalidFormatException e) {
            assertEquals("Bracketed IPv6 address must contain closing bracket", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeUnknown() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        assertNull(deserializer.deserialize(null, null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeIllegalArgumentException() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
        deserializer.deserialize(null, null);
    }
    
    @Test
    public void testDeserializeFromString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        deserializer.deserialize("value", null);
        // You should perform some assertions on the behavior of the deserializeFromString method
    }
    
    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), deserializer.deserialize(null, null));
    }
    
}