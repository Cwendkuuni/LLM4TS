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

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialize() {
        String value = "testfile.txt";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File(value);
        File actual = (File) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testURLDeserialize() {
        String value = "https://www.example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL(value);
        URL actual = (URL) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testURIDeserialize() {
        String value = "https://www.example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create(value);
        URI actual = (URI) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClassDeserialize() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expected = String.class;
        Class<?> actual = (Class<?>) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testJavaTypeDeserialize() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = JavaType.constructFromClass(String.class);
        JavaType actual = (JavaType) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCurrencyDeserialize() {
        String value = "USD";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance(value);
        Currency actual = (Currency) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPatternDeserialize() {
        String value = "\\d+";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile(value);
        Pattern actual = (Pattern) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLocaleDeserialize() {
        String value = "en_US";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale("en", "US");
        Locale actual = (Locale) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCharsetDeserialize() {
        String value = "UTF-8";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.forName(value);
        Charset actual = (Charset) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTimeZoneDeserialize() {
        String value = "Europe/Paris";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getTimeZone(value);
        TimeZone actual = (TimeZone) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInetAddressDeserialize() {
        String value = "localhost";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getByName(value);
        InetAddress actual = (InetAddress) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInetSocketAddressDeserialize() {
        String value = "localhost:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress(value, 8080);
        InetSocketAddress actual = (InetSocketAddress) deserializer._deserialize(value, null);
        assertEquals(expected, actual);
    }
}