package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserialize_file() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        String value = "/path/to/file.txt";
        File expected = new File(value);
        File result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_url() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        String value = "http://www.example.com";
        URL expected = new URL(value);
        URL result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_uri() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        String value = "http://www.example.com";
        URI expected = URI.create(value);
        URI result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        String value = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_javaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        String value = "java.util.List";
        JavaType expected = fromCanonical(value);
        JavaType result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        String value = "USD";
        Currency expected = Currency.getInstance(value);
        Currency result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        String value = "\\d+";
        Pattern expected = Pattern.compile(value);
        Pattern result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_locale_singleArgument() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        String value = "en";
        Locale expected = new Locale(value);
        Locale result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_locale_twoArguments() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        String value = "en-US";
        Locale expected = new Locale("en", "US");
        Locale result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        String value = "UTF-8";
        Charset expected = Charset.forName(value);
        Charset result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_timeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        String value = "America/New_York";
        TimeZone expected = TimeZone.getTimeZone(value);
        TimeZone result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_inetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        String value = "127.0.0.1";
        InetAddress expected = InetAddress.getByName(value);
        InetAddress result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_inetSocketAddress_ipv6WithPortNumber() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        String value = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080";
        InetSocketAddress expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 8080);
        InetSocketAddress result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_inetSocketAddress_hostWithPortNumber() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        String value = "localhost:8080";
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_inetSocketAddress_hostWithoutPortNumber() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        String value = "localhost";
        InetSocketAddress expected = new InetSocketAddress("localhost", 0);
        InetSocketAddress result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_stringBuilder() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        String value = "Hello World";
        StringBuilder expected = new StringBuilder(value);
        StringBuilder result = deserializer._deserialize(value, null);
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDeserialize_uri_emptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        String value = "";
        URI expected = URI.create("");
        URI result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_locale_emptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        String value = "";
        Locale expected = Locale.ROOT;
        Locale result = deserializer._deserialize(value, null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_stringBuilder_emptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        String value = "";
        StringBuilder expected = new StringBuilder();
        StringBuilder result = deserializer._deserialize(value, null);
        assertEquals(expected.toString(), result.toString());
    }
    
    private JavaType fromCanonical(String canonical) {
        // Implementation omitted for brevity
    }

}