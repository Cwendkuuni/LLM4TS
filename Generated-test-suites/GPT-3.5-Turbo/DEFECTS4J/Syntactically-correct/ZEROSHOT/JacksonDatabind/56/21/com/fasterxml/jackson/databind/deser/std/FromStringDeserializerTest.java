package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        String value = "testFilePath";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(File.class, result.getClass());
            Assert.assertEquals(value, ((File) result).getPath());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing File object");
        }
    }

    @Test
    public void testURLDeserializer() {
        String value = "http://www.test.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(URL.class, result.getClass());
            Assert.assertEquals(value, ((URL) result).toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing URL object");
        }
    }

    @Test
    public void testURIDeserializer() {
        String value = "testURI";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(URI.class, result.getClass());
            Assert.assertEquals(value, ((URI) result).toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing URI object");
        }
    }

    @Test
    public void testClassDeserializer() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(Class.class, result.getClass());
            Assert.assertEquals(value, ((Class<?>) result).getName());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing Class object");
        }
    }

    @Test
    public void testJavaTypeDeserializer() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(JavaType.class, result.getClass());
            Assert.assertEquals(value, ((JavaType) result).toCanonical());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing JavaType object");
        }
    }

    @Test
    public void testCurrencyDeserializer() {
        String value = "USD";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(Currency.class, result.getClass());
            Assert.assertEquals(value, ((Currency) result).getCurrencyCode());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing Currency object");
        }
    }

    @Test
    public void testPatternDeserializer() {
        String value = "[a-zA-Z]+";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(Pattern.class, result.getClass());
            Assert.assertEquals(value, ((Pattern) result).pattern());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing Pattern object");
        }
    }

    @Test
    public void testLocaleDeserializer() {
        String value = "en_US";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(Locale.class, result.getClass());
            Assert.assertEquals(value, ((Locale) result).toLanguageTag());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing Locale object");
        }
    }

    @Test
    public void testCharsetDeserializer() {
        String value = "UTF-8";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(Charset.class, result.getClass());
            Assert.assertEquals(value, ((Charset) result).name());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing Charset object");
        }
    }

    @Test
    public void testTimeZoneDeserializer() {
        String value = "GMT";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(TimeZone.class, result.getClass());
            Assert.assertEquals(value, ((TimeZone) result).getID());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing TimeZone object");
        }
    }

    @Test
    public void testInetAddressDeserializer() {
        String value = "localhost";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(InetAddress.class, result.getClass());
            Assert.assertEquals(value, ((InetAddress) result).getHostName());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing InetAddress object");
        }
    }

    @Test
    public void testInetSocketAddressDeserializer() {
        String value = "localhost:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        try {
            Object result = deserializer._deserialize(value, null);
            Assert.assertEquals(InetSocketAddress.class, result.getClass());
            InetSocketAddress address = (InetSocketAddress) result;
            Assert.assertEquals("localhost", address.getHostString());
            Assert.assertEquals(8080, address.getPort());
        } catch (Exception e) {
            Assert.fail("Exception thrown while deserializing InetSocketAddress object");
        }
    }
}