package com.fasterxml.jackson.databind.deser.std;

import java.io.*;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
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
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    private FromStringDeserializer<File> fileDeserializer;
    private FromStringDeserializer<URL> urlDeserializer;
    private FromStringDeserializer<URI> uriDeserializer;
    private FromStringDeserializer<Class<?>> classDeserializer;
    private FromStringDeserializer<JavaType> javaTypeDeserializer;
    private FromStringDeserializer<Currency> currencyDeserializer;
    private FromStringDeserializer<Pattern> patternDeserializer;
    private FromStringDeserializer<Locale> localeDeserializer;
    private FromStringDeserializer<Charset> charsetDeserializer;
    private FromStringDeserializer<TimeZone> timeZoneDeserializer;
    private FromStringDeserializer<InetAddress> inetAddressDeserializer;
    private FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer;

    @Before
    public void setUp() {
        fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
    }

    @Test
    public void testFileDeserializer() throws IOException {
        String filePath = "/path/to/file";
        File file = fileDeserializer._deserialize(filePath, null);
        assertEquals(filePath, file.getPath());
    }

    @Test
    public void testURLDeserializer() throws IOException {
        String urlString = "https://www.example.com";
        URL url = urlDeserializer._deserialize(urlString, null);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testURIDeserializer() throws IOException, URISyntaxException {
        String uriString = "https://www.example.com";
        URI uri = uriDeserializer._deserialize(uriString, null);
        assertEquals(uriString, uri.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException, ClassNotFoundException {
        String className = "com.example.TestClass";
        Class<?> clazz = classDeserializer._deserialize(className, null);
        assertEquals(className, clazz.getName());
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException, ClassNotFoundException {
        String javaTypeName = "java.util.List<java.lang.String>";
        JavaType javaType = javaTypeDeserializer._deserialize(javaTypeName, null);
        assertEquals(javaTypeName, javaType.toCanonical());
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        String currencyCode = "USD";
        Currency currency = currencyDeserializer._deserialize(currencyCode, null);
        assertEquals(currencyCode, currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        String patternString = "\\d+";
        Pattern pattern = patternDeserializer._deserialize(patternString, null);
        assertEquals(patternString, pattern.pattern());
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        String localeString = "en_US";
        Locale locale = localeDeserializer._deserialize(localeString, null);
        assertEquals(localeString, locale.toString());
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        String charsetName = "UTF-8";
        Charset charset = charsetDeserializer._deserialize(charsetName, null);
        assertEquals(charsetName, charset.name());
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        String timeZoneID = "America/New_York";
        TimeZone timeZone = timeZoneDeserializer._deserialize(timeZoneID, null);
        assertEquals(timeZoneID, timeZone.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        String inetAddressString = "127.0.0.1";
        InetAddress inetAddress = inetAddressDeserializer._deserialize(inetAddressString, null);
        assertEquals(inetAddressString, inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        String inetSocketAddressString = "127.0.0.1:8080";
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer._deserialize(inetSocketAddressString, null);
        assertEquals(inetSocketAddressString, inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeserializer() throws IOException {
        FromStringDeserializer.Std.findDeserializer(String.class);
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidURI() throws IOException, URISyntaxException {
        String uriString = "not_a_valid_uri";
        uriDeserializer._deserialize(uriString, null);
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURL() throws IOException {
        String urlString = "not_a_valid_url";
        urlDeserializer._deserialize(urlString, null);
    }

}