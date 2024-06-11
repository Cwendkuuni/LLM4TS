package com.fasterxml.jackson.databind.deser.std;

import java.io.File;
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer deserializer;

    @Mock
    private JsonParser jp;

    @Mock
    private DeserializationContext ctxt;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deserializer = new FromStringDeserializer();
    }

    @Test
    public void testDeserialize_File() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("/path/to/file");
        File result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getAbsolutePath(), "/path/to/file");
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("http://www.example.com");
        URL result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.toString(), "http://www.example.com");
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("http://www.example.com");
        URI result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.toString(), "http://www.example.com");
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("com.example.MyClass");
        Class result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getName(), "com.example.MyClass");
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("com.example.MyClass");
        JavaType result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getRawClass().getName(), "com.example.MyClass");
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("USD");
        Currency result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getCurrencyCode(), "USD");
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("\\d{3}-\\d{3}-\\d{4}");
        Pattern result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.pattern(), "\\d{3}-\\d{3}-\\d{4}");
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("en_US");
        Locale result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.toString(), "en_US");
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("UTF-8");
        Charset result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.displayName(), "UTF-8");
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("America/New_York");
        TimeZone result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getID(), "America/New_York");
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("127.0.0.1");
        InetAddress result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getHostAddress(), "127.0.0.1");
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getValueAsString()).thenReturn("127.0.0.1");
        InetSocketAddress result = deserializer.deserialize(jp, ctxt);
        assertEquals(result.getAddress().getHostAddress(), "127.0.0.1");
    }
}