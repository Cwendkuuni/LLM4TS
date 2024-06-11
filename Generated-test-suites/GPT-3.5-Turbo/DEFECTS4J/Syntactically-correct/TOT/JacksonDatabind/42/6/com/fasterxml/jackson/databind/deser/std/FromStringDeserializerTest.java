package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @SuppressWarnings("serial")
    private static final class TestFromStringDeserializer<T> extends FromStringDeserializer<T> {
        protected TestFromStringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        protected T _deserialize(String value, DeserializationContext ctxt)
                throws JsonMappingException {
            return null; // TODO: Implement
        }
    }
    
    @Test
    public void testDeserialize_File() {
        String fileString = "/path/to/file.txt";
        TestFromStringDeserializer<File> deserializer = new TestFromStringDeserializer<File>(File.class);
        File file = deserializer.deserialize(fileString, null);
        assertEquals(fileString, file.getPath());
    }
    
    @Test
    public void testDeserialize_URL() {
        String urlString = "http://www.example.com";
        TestFromStringDeserializer<URL> deserializer = new TestFromStringDeserializer<URL>(URL.class);
        URL url = deserializer.deserialize(urlString, null);
        assertEquals(urlString, url.toString());
    }
    
    @Test
    public void testDeserialize_URI() {
        String uriString = "http://www.example.com";
        TestFromStringDeserializer<URI> deserializer = new TestFromStringDeserializer<URI>(URI.class);
        URI uri = deserializer.deserialize(uriString, null);
        assertEquals(uriString, uri.toString());
    }
    
    @Test
    public void testDeserialize_Class() {
        String classString = "java.lang.String";
        TestFromStringDeserializer<Class<?>> deserializer = new TestFromStringDeserializer<Class<?>>(Class.class);
        Class<?> clazz = deserializer.deserialize(classString, null);
        assertEquals(classString, clazz.getName());
    }
    
    @Test
    public void testDeserialize_JavaType() {
        String javaTypeString = "java.util.List";
        TestFromStringDeserializer<JavaType> deserializer = new TestFromStringDeserializer<JavaType>(JavaType.class);
        JavaType javaType = deserializer.deserialize(javaTypeString, null);
        assertEquals(javaTypeString, javaType.getRawClass().getName());
    }
    
    @Test
    public void testDeserialize_Currency() {
        String currencyString = "USD";
        TestFromStringDeserializer<Currency> deserializer = new TestFromStringDeserializer<Currency>(Currency.class);
        Currency currency = deserializer.deserialize(currencyString, null);
        assertEquals(currencyString, currency.getCurrencyCode());
    }
    
    @Test
    public void testDeserialize_Pattern() {
        String patternString = "\\d{3}-\\d{3}-\\d{4}";
        TestFromStringDeserializer<Pattern> deserializer = new TestFromStringDeserializer<Pattern>(Pattern.class);
        Pattern pattern = deserializer.deserialize(patternString, null);
        assertEquals(patternString, pattern.pattern());
    }
    
    @Test
    public void testDeserialize_Locale_SingleArg() {
        String localeString = "en";
        TestFromStringDeserializer<Locale> deserializer = new TestFromStringDeserializer<Locale>(Locale.class);
        Locale locale = deserializer.deserialize(localeString, null);
        assertEquals(localeString, locale.getLanguage());
        assertNull(locale.getCountry());
        assertNull(locale.getVariant());
    }
    
    @Test
    public void testDeserialize_Locale_TwoArgs() {
        String localeString = "en_US";
        TestFromStringDeserializer<Locale> deserializer = new TestFromStringDeserializer<Locale>(Locale.class);
        Locale locale = deserializer.deserialize(localeString, null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertNull(locale.getVariant());
    }
    
    @Test
    public void testDeserialize_Locale_ThreeArgs() {
        String localeString = "en_US_WINDOWS";
        TestFromStringDeserializer<Locale> deserializer = new TestFromStringDeserializer<Locale>(Locale.class);
        Locale locale = deserializer.deserialize(localeString, null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("WINDOWS", locale.getVariant());
    }
    
    @Test
    public void testDeserialize_Charset() {
        String charsetString = "UTF-8";
        TestFromStringDeserializer<Charset> deserializer = new TestFromStringDeserializer<Charset>(Charset.class);
        Charset charset = deserializer.deserialize(charsetString, null);
        assertEquals(charsetString, charset.name());
    }
    
    @Test
    public void testDeserialize_TimeZone() {
        String timeZoneString = "GMT";
        TestFromStringDeserializer<TimeZone> deserializer = new TestFromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(timeZoneString, null);
        assertEquals(timeZoneString, timeZone.getID());
    }
    
    @Test
    public void testDeserialize_InetAddress() {
        String inetAddressString = "127.0.0.1";
        TestFromStringDeserializer<InetAddress> deserializer = new TestFromStringDeserializer<InetAddress>(InetAddress.class);
        InetAddress inetAddress = deserializer.deserialize(inetAddressString, null);
        assertEquals(inetAddressString, inetAddress.getHostAddress());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_BracketedIPv6() {
        String inetSocketAddressString = "[2001:db8::1]:8080";
        TestFromStringDeserializer<InetSocketAddress> deserializer = new TestFromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(inetSocketAddressString, null);
        assertEquals("2001:db8::1", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_HostPort() {
        String inetSocketAddressString = "localhost:8080";
        TestFromStringDeserializer<InetSocketAddress> deserializer = new TestFromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(inetSocketAddressString, null);
        assertEquals("localhost", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_UnbracketedIPv6() {
        String inetSocketAddressString = "2001:db8::1";
        TestFromStringDeserializer<InetSocketAddress> deserializer = new TestFromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(inetSocketAddressString, null);
        assertEquals(inetSocketAddressString, inetSocketAddress.getHostString());
        assertEquals(0, inetSocketAddress.getPort());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidKind() {
        String invalidKindString = "invalidKindString";
        TestFromStringDeserializer<Object> deserializer = new TestFromStringDeserializer<>(Object.class);
        deserializer.deserialize(invalidKindString, null);
    }

}