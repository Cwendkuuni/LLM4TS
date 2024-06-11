package com.fasterxml.jackson.databind.deser.std;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() {
        FromStringDeserializer.Std fileDeserializer = FromStringDeserializer.Std.findDeserializer(File.class);
        assertNotNull(fileDeserializer);
        String filePath = "/path/to/file.txt";
        File file = (File) fileDeserializer._deserialize(filePath, null);
        assertEquals(filePath, file.getPath());
    }

    @Test
    public void testURLDeserialization() throws MalformedURLException {
        FromStringDeserializer.Std urlDeserializer = FromStringDeserializer.Std.findDeserializer(URL.class);
        assertNotNull(urlDeserializer);
        String urlString = "http://www.example.com";
        URL url = (URL) urlDeserializer._deserialize(urlString, null);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testURIDeserialization() throws URISyntaxException {
        FromStringDeserializer.Std uriDeserializer = FromStringDeserializer.Std.findDeserializer(URI.class);
        assertNotNull(uriDeserializer);
        String uriString = "http://www.example.com";
        URI uri = (URI) uriDeserializer._deserialize(uriString, null);
        assertEquals(uriString, uri.toString());
    }

    @Test
    public void testClassDeserialization() throws ClassNotFoundException {
        FromStringDeserializer.Std classDeserializer = FromStringDeserializer.Std.findDeserializer(Class.class);
        assertNotNull(classDeserializer);
        String className = "java.util.ArrayList";
        Class<?> clazz = (Class<?>) classDeserializer._deserialize(className, null);
        assertEquals(className, clazz.getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws ClassNotFoundException {
        FromStringDeserializer.Std javaTypeDeserializer = FromStringDeserializer.Std.findDeserializer(JavaType.class);
        assertNotNull(javaTypeDeserializer);
        String javaTypeString = "java.util.Map<String,Integer>";
        JavaType javaType = (JavaType) javaTypeDeserializer._deserialize(javaTypeString, null);
        assertEquals(javaTypeString, javaType.toCanonical());
    }

    @Test
    public void testCurrencyDeserialization() {
        FromStringDeserializer.Std currencyDeserializer = FromStringDeserializer.Std.findDeserializer(Currency.class);
        assertNotNull(currencyDeserializer);
        String currencyCode = "USD";
        Currency currency = (Currency) currencyDeserializer._deserialize(currencyCode, null);
        assertEquals(currencyCode, currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserialization() {
        FromStringDeserializer.Std patternDeserializer = FromStringDeserializer.Std.findDeserializer(Pattern.class);
        assertNotNull(patternDeserializer);
        String patternString = "[a-z]+";
        Pattern pattern = (Pattern) patternDeserializer._deserialize(patternString, null);
        assertEquals(patternString, pattern.pattern());
    }

    @Test
    public void testLocaleDeserialization() {
        FromStringDeserializer.Std localeDeserializer = FromStringDeserializer.Std.findDeserializer(Locale.class);
        assertNotNull(localeDeserializer);
        String localeString = "en-US";
        Locale locale = (Locale) localeDeserializer._deserialize(localeString, null);
        assertEquals(localeString, locale.toString());
    }

    @Test
    public void testCharsetDeserialization() {
        FromStringDeserializer.Std charsetDeserializer = FromStringDeserializer.Std.findDeserializer(Charset.class);
        assertNotNull(charsetDeserializer);
        String charsetName = "UTF-8";
        Charset charset = (Charset) charsetDeserializer._deserialize(charsetName, null);
        assertEquals(charsetName, charset.name());
    }

    @Test
    public void testTimeZoneDeserialization() {
        FromStringDeserializer.Std timeZoneDeserializer = FromStringDeserializer.Std.findDeserializer(TimeZone.class);
        assertNotNull(timeZoneDeserializer);
        String timeZoneID = "GMT";
        TimeZone timeZone = (TimeZone) timeZoneDeserializer._deserialize(timeZoneID, null);
        assertEquals(timeZoneID, timeZone.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws UnknownHostException {
        FromStringDeserializer.Std inetAddressDeserializer = FromStringDeserializer.Std.findDeserializer(InetAddress.class);
        assertNotNull(inetAddressDeserializer);
        String hostAddress = "192.0.2.1";
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer._deserialize(hostAddress, null);
        assertEquals(hostAddress, inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws UnknownHostException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = FromStringDeserializer.Std.findDeserializer(InetSocketAddress.class);
        assertNotNull(inetSocketAddressDeserializer);
        String hostPort = "localhost:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize(hostPort, null);
        assertEquals("localhost", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testStringBuilderDeserialization() {
        FromStringDeserializer.Std stringBuilderDeserializer = FromStringDeserializer.Std.findDeserializer(StringBuilder.class);
        assertNotNull(stringBuilderDeserializer);
        String value = "test";
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer._deserialize(value, null);
        assertEquals(value, stringBuilder.toString());
    }
}