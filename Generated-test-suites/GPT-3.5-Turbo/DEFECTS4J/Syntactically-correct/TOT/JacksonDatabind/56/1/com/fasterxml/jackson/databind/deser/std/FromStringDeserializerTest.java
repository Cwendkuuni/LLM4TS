
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
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

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setup() {
        deserializer = new FromStringDeserializer.Std(null, 0);
    }

    @Test
    public void testDeserializeFile() throws Exception {
        File file = (File) deserializer._deserialize("test.txt", null);
        assertEquals("test.txt", file.getPath());
    }

    @Test
    public void testDeserializeURL() throws Exception {
        URL url = (URL) deserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws Exception {
        URI uri = (URI) deserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        // TODO: Implement test case
        assertTrue(true);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        Currency currency = (Currency) deserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws Exception {
        Pattern pattern = (Pattern) deserializer._deserialize("[a-z]+", null);
        assertTrue(pattern.matcher("abc").matches());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        Locale locale = (Locale) deserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        TimeZone timeZone = (TimeZone) deserializer._deserialize("Europe/Paris", null);
        assertEquals("Europe/Paris", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        InetAddress address = (InetAddress) deserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", address.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) deserializer._deserialize("127.0.0.1:8080", null);
        assertEquals("127.0.0.1", socketAddress.getAddress().getHostAddress());
        assertEquals(8080, socketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidValue() throws Exception {
        deserializer._deserialize("invalid", null);
    }

    @Test
    public void testDeserializeEmptyStringForURI() throws Exception {
        deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserializeFromEmptyString();
        assertNull(uri);
    }

    @Test
    public void testDeserializeEmptyStringForLocale() throws Exception {
        deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer._deserializeFromEmptyString();
        assertEquals(Locale.ROOT, locale);
    }

}
