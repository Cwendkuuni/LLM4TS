
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
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

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = fileDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(file);
        assertTrue(file instanceof File);
    }

    @Test
    public void testURLDeserializer() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = urlDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(url);
        assertTrue(url instanceof URL);
    }

    @Test
    public void testURIDeserializer() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = uriDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(uri);
        assertTrue(uri instanceof URI);
    }

    @Test
    public void testClassDeserializer() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = classDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(clazz);
        assertTrue(clazz instanceof Class);
    }

    @Test
    public void testJavaTypeDeserializer() {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = javaTypeDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(javaType);
        assertTrue(javaType instanceof JavaType);
    }

    @Test
    public void testCurrencyDeserializer() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = currencyDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(currency);
        assertTrue(currency instanceof Currency);
    }

    @Test
    public void testPatternDeserializer() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = patternDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(pattern);
        assertTrue(pattern instanceof Pattern);
    }

    @Test
    public void testLocaleDeserializer() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = localeDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(locale);
        assertTrue(locale instanceof Locale);
    }

    @Test
    public void testCharsetDeserializer() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = charsetDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(charset);
        assertTrue(charset instanceof Charset);
    }

    @Test
    public void testTimeZoneDeserializer() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = timeZoneDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(timeZone);
        assertTrue(timeZone instanceof TimeZone);
    }

    @Test
    public void testInetAddressDeserializer() {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(inetAddress);
        assertTrue(inetAddress instanceof InetAddress);
    }

    @Test
    public void testInetSocketAddressDeserializer() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(JsonParser.ValueType.STRING, DeserializationContext.DEFAULT);
        assertNotNull(inetSocketAddress);
        assertTrue(inetSocketAddress instanceof InetSocketAddress);
    }

    @Test
    public void testFindDeserializer() {
        FromStringDeserializer.Std fileDeserializer = (FromStringDeserializer.Std)FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(fileDeserializer);
        assertTrue(fileDeserializer instanceof FromStringDeserializer.Std);
    }

    @Test
    public void testDeserialize() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = fileDeserializer.deserialize(null, DeserializationContext.DEFAULT);
        assertNull(file);
    }

    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        Exception e = null;
        try {
            fileDeserializer._deserializeEmbedded(null, DeserializationContext.DEFAULT);
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof IOException);
    }

    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = null;
        try {
            file = fileDeserializer._deserializeFromEmptyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(file);
    }

    @Test
    public void testDeserializeFromEmptyStringWithURI() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = null;
        try {
            uri = uriDeserializer._deserializeFromEmptyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(uri);
        assertEquals("", uri.toString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithLocale() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = null;
        try {
            locale = localeDeserializer._deserializeFromEmptyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(locale);
        assertEquals(Locale.ROOT, locale);
    }
}

