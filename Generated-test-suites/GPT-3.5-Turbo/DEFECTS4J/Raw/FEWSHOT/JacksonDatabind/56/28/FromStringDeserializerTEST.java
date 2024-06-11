package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Mock
    private JsonParser jsonParserMock;

    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File actual = (File) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL actual = (URL) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class,
                FromStringDeserializer.Std.STD_URI);
        URI actual = (URI) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class,
                FromStringDeserializer.Std.STD_CLASS);
        Class<?> actual = (Class<?>) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class,
                FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType actual = (JavaType) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class,
                FromStringDeserializer.Std.STD_CURRENCY);
        Currency actual = (Currency) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class,
                FromStringDeserializer.Std.STD_PATTERN);
        Pattern actual = (Pattern) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class,
                FromStringDeserializer.Std.STD_LOCALE);
        Locale actual = (Locale) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class,
                FromStringDeserializer.Std.STD_CHARSET);
        Charset actual = (Charset) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class,
                FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone actual = (TimeZone) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class,
                FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress actual = (InetAddress) deserializer.deserialize(jsonParserMock, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_inetSocketAddress_withBrackets() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class,
                FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        when(jsonParserMock.getEmbeddedObject()).thenReturn("[::1]:8080");
        InetSocketAddress actual = (InetSocketAddress) deserializer.deserialize(jsonParserMock,
                deserializationContext);
        assertNull(actual);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_inetSocketAddress_withBracketsMissingClosingBracket() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class,
                FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        when(jsonParserMock.getEmbeddedObject()).thenReturn("[::1):8080");
        deserializer.deserialize(jsonParserMock, deserializationContext);
    }

    @Test
    public void testDeserialize_inetSocketAddress_withoutBrackets() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class,
                FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        when(jsonParserMock.getEmbeddedObject()).thenReturn("localhost:8080");
        InetSocketAddress actual = (InetSocketAddress) deserializer.deserialize(jsonParserMock,
                deserializationContext);
        assertNull(actual);
    }

}