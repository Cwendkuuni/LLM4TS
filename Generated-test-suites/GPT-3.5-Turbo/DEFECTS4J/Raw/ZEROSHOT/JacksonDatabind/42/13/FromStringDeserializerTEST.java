package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer.FileDeserializer fileDeserializer = new FromStringDeserializer.FileDeserializer();
        try {
            File file = fileDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(file);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.URLDeserializer urlDeserializer = new FromStringDeserializer.URLDeserializer();
        try {
            URL url = urlDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(url);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.URIDeserializer uriDeserializer = new FromStringDeserializer.URIDeserializer();
        try {
            URI uri = uriDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(uri);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.ClassDeserializer classDeserializer = new FromStringDeserializer.ClassDeserializer();
        try {
            Class<?> clazz = classDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(clazz);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.JavaTypeDeserializer javaTypeDeserializer = new FromStringDeserializer.JavaTypeDeserializer();
        try {
            JavaType javaType = javaTypeDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(javaType);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.CurrencyDeserializer currencyDeserializer = new FromStringDeserializer.CurrencyDeserializer();
        try {
            Currency currency = currencyDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(currency);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.PatternDeserializer patternDeserializer = new FromStringDeserializer.PatternDeserializer();
        try {
            Pattern pattern = patternDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(pattern);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.LocaleDeserializer localeDeserializer = new FromStringDeserializer.LocaleDeserializer();
        try {
            Locale locale = localeDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(locale);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.CharsetDeserializer charsetDeserializer = new FromStringDeserializer.CharsetDeserializer();
        try {
            Charset charset = charsetDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(charset);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.TimeZoneDeserializer timeZoneDeserializer = new FromStringDeserializer.TimeZoneDeserializer();
        try {
            TimeZone timeZone = timeZoneDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(timeZone);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.InetAddressDeserializer inetAddressDeserializer = new FromStringDeserializer.InetAddressDeserializer();
        try {
            InetAddress inetAddress = inetAddressDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(inetAddress);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.InetSocketAddressDeserializer inetSocketAddressDeserializer = new FromStringDeserializer.InetSocketAddressDeserializer();
        try {
            InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(inetSocketAddress);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindDeserializer() {
        assertNull(FromStringDeserializer.findDeserializer(File.class));
        assertNull(FromStringDeserializer.findDeserializer(URL.class));
        assertNull(FromStringDeserializer.findDeserializer(URI.class));
        assertNull(FromStringDeserializer.findDeserializer(Class.class));
        assertNull(FromStringDeserializer.findDeserializer(JavaType.class));
        assertNull(FromStringDeserializer.findDeserializer(Currency.class));
        assertNull(FromStringDeserializer.findDeserializer(Pattern.class));
        assertNull(FromStringDeserializer.findDeserializer(Locale.class));
        assertNull(FromStringDeserializer.findDeserializer(Charset.class));
        assertNull(FromStringDeserializer.findDeserializer(TimeZone.class));
        assertNull(FromStringDeserializer.findDeserializer(InetAddress.class));
        assertNull(FromStringDeserializer.findDeserializer(InetSocketAddress.class));
    }

    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.URIStd uriStd = new FromStringDeserializer.URIStd();
        try {
            URI uri = uriStd.deserialize(JsonToken.VALUE_STRING, null);
            assertEquals("", uri.toString());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.LocaleStd localeStd = new FromStringDeserializer.LocaleStd();
        try {
            Locale locale = localeStd.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(locale);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Class.class, 4);
        try {
            Object object = std.deserialize(JsonToken.VALUE_STRING, null);
            assertNull(object);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
}