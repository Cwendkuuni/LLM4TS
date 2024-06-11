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

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer.FileDeserializer fileDeserializer = new FromStringDeserializer.FileDeserializer();

        File file = fileDeserializer._deserialize("test.txt", null);
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserialize_EmptyString() {
        FromStringDeserializer.URIDeserializer uriDeserializer = new FromStringDeserializer.URIDeserializer();

        URI uri = uriDeserializer._deserialize("", null);
        assertEquals("", uri.toString());

        FromStringDeserializer.LocaleDeserializer localeDeserializer = new FromStringDeserializer.LocaleDeserializer();

        Locale locale = localeDeserializer._deserialize("", null);
        assertEquals(Locale.ROOT, locale);
    }

    @Test
    public void testDeserialize_EmbeddedObject() {
        FromStringDeserializer.InetAddressDeserializer inetAddressDeserializer = new FromStringDeserializer.InetAddressDeserializer();

        InetAddress inetAddress = inetAddressDeserializer._deserializeEmbedded("localhost", null);
        assertEquals("localhost", inetAddress.getHostName());
    }

    @Test
    public void testDeserialize_InvalidString() {
        FromStringDeserializer.PatternDeserializer patternDeserializer = new FromStringDeserializer.PatternDeserializer();

        try {
            patternDeserializer._deserialize("****", null);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStd_Deserialize() {
        FromStringDeserializer.Std.fileDeserializer = new FromStringDeserializer.Std.FileDeserializer();
        File file = FromStringDeserializer.Std.fileDeserializer._deserialize("test.txt", null);
        assertEquals("test.txt", file.getName());

        FromStringDeserializer.Std.urlDeserializer = new FromStringDeserializer.Std.URLDeserializer();
        URL url = FromStringDeserializer.Std.urlDeserializer._deserialize("http://example.com", null);
        assertEquals("http://example.com", url.toString());

        FromStringDeserializer.Std.uriDeserializer = new FromStringDeserializer.Std.URIDeserializer();
        URI uri = FromStringDeserializer.Std.uriDeserializer._deserialize("http://example.com", null);
        assertEquals("http://example.com", uri.toString());

        FromStringDeserializer.Std.classDeserializer = new FromStringDeserializer.Std.ClassDeserializer();
        try {
            Class<?> clazz = FromStringDeserializer.Std.classDeserializer._deserialize("com.example.MyClass", null);
            assertEquals("com.example.MyClass", clazz.getName());
        } catch (Exception e) {
            assertTrue(e instanceof ClassNotFoundException);
        }

        FromStringDeserializer.Std.javaTypeDeserializer = new FromStringDeserializer.Std.JavaTypeDeserializer();
        // Mock getTypeFactory to return expected JavaType
        FromStringDeserializer.Std.javaTypeDeserializer._deserialize("java.util.List<java.lang.String>", null);

        FromStringDeserializer.Std.currencyDeserializer = new FromStringDeserializer.Std.CurrencyDeserializer();
        Currency currency = FromStringDeserializer.Std.currencyDeserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());

        FromStringDeserializer.Std.patternDeserializer = new FromStringDeserializer.Std.PatternDeserializer();
        Pattern pattern = FromStringDeserializer.Std.patternDeserializer._deserialize("[a-zA-Z]", null);
        assertEquals("[a-zA-Z]", pattern.pattern());

        FromStringDeserializer.Std.localeDeserializer = new FromStringDeserializer.Std.LocaleDeserializer();
        Locale locale = FromStringDeserializer.Std.localeDeserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());

        FromStringDeserializer.Std.charsetDeserializer = new FromStringDeserializer.Std.CharsetDeserializer();
        Charset charset = FromStringDeserializer.Std.charsetDeserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.name());

        FromStringDeserializer.Std.timeZoneDeserializer = new FromStringDeserializer.Std.TimeZoneDeserializer();
        TimeZone timeZone = FromStringDeserializer.Std.timeZoneDeserializer._deserialize("GMT", null);
        assertEquals("GMT", timeZone.getID());

        FromStringDeserializer.Std.inetAddressDeserializer = new FromStringDeserializer.Std.InetAddressDeserializer();
        InetAddress inetAddress = FromStringDeserializer.Std.inetAddressDeserializer._deserialize("localhost", null);
        assertEquals("localhost", inetAddress.getHostName());

        FromStringDeserializer.Std.inetSocketAddressDeserializer = new FromStringDeserializer.Std.InetSocketAddressDeserializer();
        InetSocketAddress inetSocketAddress = FromStringDeserializer.Std.inetSocketAddressDeserializer._deserialize("localhost:8080", null);
        assertEquals("localhost", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testStd_DeserializeFromEmptyString() {
        FromStringDeserializer.Std.fileDeserializer = new FromStringDeserializer.Std.FileDeserializer();
        assertNull(FromStringDeserializer.Std.fileDeserializer._deserializeFromEmptyString());

        FromStringDeserializer.Std.uriDeserializer = new FromStringDeserializer.Std.URIDeserializer();
        URI uri = FromStringDeserializer.Std.uriDeserializer._deserializeFromEmptyString();
        assertEquals("", uri.toString());

        FromStringDeserializer.Std.localeDeserializer = new FromStringDeserializer.Std.LocaleDeserializer();
        Locale locale = FromStringDeserializer.Std.localeDeserializer._deserializeFromEmptyString();
        assertEquals(Locale.ROOT, locale);
    }

}