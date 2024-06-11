
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer._deserialize("/path/to/file", null);
        assertEquals("/path/to/file", file.getPath());
    }

    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer._deserialize("http://example.com", null);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserialize("http://example.com", null);
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_JavaType(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) deserializer._deserialize("java.util.List", null);
        assertEquals(JavaType.class, javaType.getClass());
    }

    @Test
    public void testDeserialize_Currency(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) deserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) deserializer._deserialize("[A-Za-z]+", null);
        assertEquals("[A-Za-z]+", pattern.pattern());
    }

    @Test
    public void testDeserialize_Locale(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserialize_Charset(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.toString());
    }

    @Test
    public void testDeserialize_TimeZone(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer._deserialize("America/Los_Angeles", null);
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws UnknownHostException{
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserialize_InetSocketAddress(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(0, inetSocketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidInput(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer._deserialize("1234", null);
    }

    @Test
    public void testDeserializeFromEmptyString_File(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyString_InvalidInput(){
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserializeFromEmptyString();
        assertEquals("", uri.toString());
    }
}
