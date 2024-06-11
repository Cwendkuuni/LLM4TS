package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("/path/to/file.txt"), fileDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), urlDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), uriDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(Class.class, classDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, javaTypeDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("[a-z]+"), patternDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.US, localeDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), charsetDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("GMT"), timeZoneDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getLocalHost(), inetAddressDeserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        // Test IPv4 address
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddressDeserializer.deserialize(null, null));

        // Test IPv6 address
        assertEquals(new InetSocketAddress("[::1]", 8080), inetSocketAddressDeserializer.deserialize(null, null));

        // Test host without port
        assertEquals(new InetSocketAddress("localhost", 0), inetSocketAddressDeserializer.deserialize(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidType() throws IOException {
        FromStringDeserializer.Std stdClassDeserializer = new FromStringDeserializer.Std(Std.class, FromStringDeserializer.Std.STD_CLASS);
        stdClassDeserializer.deserialize(null, null);
    }

    @Test
    public void testDeserializeEmbeddedNull() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNull(fileDeserializer._deserializeEmbedded(null, null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeEmbeddedError() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        fileDeserializer._deserializeEmbedded(new Object(), null);
    }

    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), uriDeserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithLocale() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertNull(localeDeserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithOtherClass() throws IOException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertNull(classDeserializer._deserializeFromEmptyString());
    }

}