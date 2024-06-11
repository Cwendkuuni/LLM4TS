package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        DeserializationContext ctxt = null;
        try {
            fileDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validFilePath = "validFilePath";
        File file = (File) fileDeserializer._deserialize(validFilePath, ctxt);
        assert(file != null);
        assert(file.getPath().equals(validFilePath));
    }

    @Test
    public void testURLDeserializer() {
        FromStringDeserializer.Std URLDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        DeserializationContext ctxt = null;
        try {
            URLDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validURL = "https://www.example.com";
        URL url = (URL) URLDeserializer._deserialize(validURL, ctxt);
        assert(url != null);
        assert(url.toString().equals(validURL));
    }

    @Test
    public void testURIDeserializer() {
        FromStringDeserializer.Std URIDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        DeserializationContext ctxt = null;
        try {
            URIDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validURI = "https://www.example.com";
        URI uri = (URI) URIDeserializer._deserialize(validURI, ctxt);
        assert(uri != null);
        assert(uri.toString().equals(validURI));
    }

    @Test
    public void testClassDeserializer() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        DeserializationContext ctxt = null;
        try {
            classDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validClassName = "java.lang.String";
        Class<?> clazz = (Class<?>) classDeserializer._deserialize(validClassName, ctxt);
        assert(clazz != null);
        assert(clazz.getName().equals(validClassName));
    }

    @Test
    public void testJavaTypeDeserializer() {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        DeserializationContext ctxt = null;
        try {
            javaTypeDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validJavaTypeName = "java.util.List";
        JavaType javaType = (JavaType) javaTypeDeserializer._deserialize(validJavaTypeName, ctxt);
        assert(javaType != null);
        assert(javaType.toString().equals(validJavaTypeName));
    }

    @Test
    public void testCurrencyDeserializer() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        DeserializationContext ctxt = null;
        try {
            currencyDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validCurrencyCode = "USD";
        Currency currency = (Currency) currencyDeserializer._deserialize(validCurrencyCode, ctxt);
        assert(currency != null);
        assert(currency.getCurrencyCode().equals(validCurrencyCode));
    }

    @Test
    public void testPatternDeserializer() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        DeserializationContext ctxt = null;
        try {
            patternDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validPattern = ".*";
        Pattern pattern = (Pattern) patternDeserializer._deserialize(validPattern, ctxt);
        assert(pattern != null);
        assert(pattern.pattern().equals(validPattern));
    }

    @Test
    public void testLocaleDeserializer() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        DeserializationContext ctxt = null;
        try {
            localeDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validLocale = "en_US";
        Locale locale = (Locale) localeDeserializer._deserialize(validLocale, ctxt);
        assert(locale != null);
        assert(locale.toString().equals(validLocale));
    }

    @Test
    public void testCharsetDeserializer() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        DeserializationContext ctxt = null;
        try {
            charsetDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validCharset = "UTF-8";
        Charset charset = (Charset) charsetDeserializer._deserialize(validCharset, ctxt);
        assert(charset != null);
        assert(charset.name().equals(validCharset));
    }

    @Test
    public void testTimeZoneDeserializer() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        DeserializationContext ctxt = null;
        try {
            timeZoneDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validTimeZone = "America/Los_Angeles";
        TimeZone timeZone = (TimeZone) timeZoneDeserializer._deserialize(validTimeZone, ctxt);
        assert(timeZone != null);
        assert(timeZone.getID().equals(validTimeZone));
    }

    @Test
    public void testInetAddressDeserializer() {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        DeserializationContext ctxt = null;
        try {
            inetAddressDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validInetAddress = "127.0.0.1";
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer._deserialize(validInetAddress, ctxt);
        assert(inetAddress != null);
        assert(inetAddress.getHostAddress().equals(validInetAddress));
    }

    @Test
    public void testInetSocketAddressDeserializer() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        DeserializationContext ctxt = null;
        try {
            inetSocketAddressDeserializer.deserialize(null, ctxt);
        } catch (Exception e) {
            assert(e instanceof JsonMappingException);
        }
        String validIPv4SocketAddress = "127.0.0.1:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize(validIPv4SocketAddress, ctxt);
        assert(inetSocketAddress != null);
        assert(inetSocketAddress.getAddress().getHostAddress().equals("127.0.0.1"));
        assert(inetSocketAddress.getPort() == 8080);

        String validIPv6SocketAddress = "[::1]:8080";
        inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize(validIPv6SocketAddress, ctxt);
        assert(inetSocketAddress != null);
        assert(inetSocketAddress.getAddress().getHostAddress().equals("::1"));
        assert(inetSocketAddress.getPort() == 8080);

        String validHostOnlySocketAddress = "localhost";
        inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize(validHostOnlySocketAddress, ctxt);
        assert(inetSocketAddress != null);
        assert(inetSocketAddress.getAddress().getHostAddress().equals("localhost"));
        assert(inetSocketAddress.getPort() == 0);
    }
}