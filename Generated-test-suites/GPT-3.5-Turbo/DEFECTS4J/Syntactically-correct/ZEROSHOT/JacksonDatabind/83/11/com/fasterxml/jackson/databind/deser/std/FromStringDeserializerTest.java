package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        File file = deserializer.deserialize(p, ctxt);

        assertNull(file);
    }

    @Test
    public void testDeserialize_url() throws IOException, MalformedURLException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        URL url = deserializer.deserialize(p, ctxt);

        assertNull(url);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        URI uri = deserializer.deserialize(p, ctxt);

        assertNull(uri);
    }

    @Test
    public void testDeserialize_class() throws IOException, ClassNotFoundException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        Class clazz = deserializer.deserialize(p, ctxt);

        assertNull(clazz);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        JavaType javaType = deserializer.deserialize(p, ctxt);

        assertNull(javaType);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        Currency currency = deserializer.deserialize(p, ctxt);

        assertNull(currency);
    }

    @Test
    public void testDeserialize_pattern() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        Pattern pattern = deserializer.deserialize(p, ctxt);

        assertNull(pattern);
    }

    @Test
    public void testDeserialize_locale() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Locale locale = deserializer.deserialize(p, ctxt);

        assertNull(locale);
    }

    @Test
    public void testDeserialize_charset() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        Charset charset = deserializer.deserialize(p, ctxt);

        assertNull(charset);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        TimeZone timeZone = deserializer.deserialize(p, ctxt);

        assertNull(timeZone);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        InetAddress inetAddress = deserializer.deserialize(p, ctxt);

        assertNull(inetAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_bracketedIPv6() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(p, ctxt);

        assertNull(inetSocketAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_host_port() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(p, ctxt);

        assertNull(inetSocketAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_host() throws IOException, InvalidFormatException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(p, ctxt);

        assertNull(inetSocketAddress);
    }

    @Test
    public void testDeserialize_stringBuilder() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        StringBuilder stringBuilder = deserializer.deserialize(p, ctxt);

        assertNull(stringBuilder);
    }

}