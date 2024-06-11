package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        File expected = new File("test.txt");
        File result = (File) deserializer.deserialize("test.txt", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL expected = new URL("http://www.example.com");
        URL result = (URL) deserializer.deserialize("http://www.example.com", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI expected = URI.create("http://www.example.com");
        URI result = (URI) deserializer.deserialize("http://www.example.com", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class expected = String.class;
        Class result = (Class) deserializer.deserialize("java.lang.String", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType expected = new JavaType(String.class);
        JavaType result = (JavaType) deserializer.deserialize("java.lang.String", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency expected = Currency.getInstance("USD");
        Currency result = (Currency) deserializer.deserialize("USD", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        Pattern expected = Pattern.compile("[a-z]+");
        Pattern result = (Pattern) deserializer.deserialize("[a-z]+", null);
        Assert.assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale expected = new Locale("en", "US");
        Locale result = (Locale) deserializer.deserialize("en_US", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset expected = Charset.forName("UTF-8");
        Charset result = (Charset) deserializer.deserialize("UTF-8", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone result = (TimeZone) deserializer.deserialize("GMT", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        InetAddress expected = InetAddress.getByName("localhost");
        InetAddress result = (InetAddress) deserializer.deserialize("localhost", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv4() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 8080);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize("127.0.0.1:8080", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv6() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress expected = new InetSocketAddress("[::1]", 8080);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize("[::1]:8080", null);
        Assert.assertEquals(expected, result);
    }

}