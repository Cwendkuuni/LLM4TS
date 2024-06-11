package com.example.test; // replace with your test package

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Mock
    private DeserializationContext context;
    @Mock
    private JsonParser parser;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeSuccess() throws IOException {
        when(parser.getValueAsString()).thenReturn("validString");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getEmbeddedObject()).thenReturn(new Object());

        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        Object result = deserializer.deserialize(parser, context);
        assertInstanceOf(Object.class, result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        when(parser.getValueAsString()).thenReturn("");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        Object result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.getValueAsString()).thenReturn("validString");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("anotherValidString");
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        Object[] result = deserializer.deserialize(parser, context);
        assertInstanceOf(Object[].class, result);
        assertEquals(2, result.length);
        assertInstanceOf(String.class, result[0]);
        assertInstanceOf(String.class, result[1]);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        when(parser.getValueAsString()).thenReturn("invalidFormat");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class, 0);
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        when(parser.getValueAsString()).thenReturn("path/to/file.txt");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, 0);
        File result = deserializer.deserialize(parser, context);
        assertInstanceOf(File.class, result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        when(parser.getValueAsString()).thenReturn("http://example.com");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class, 0);
        URL result = deserializer.deserialize(parser, context);
        assertInstanceOf(URL.class, result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        when(parser.getValueAsString()).thenReturn("http://example.com?param1=value1");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class, 0);
        URI result = deserializer.deserialize(parser, context);
        assertInstanceOf(URI.class, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownClass() throws IOException {
        when(parser.getValueAsString()).thenReturn("java.util.Date");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        when(parser.getValueAsString()).thenReturn("java.util.Date.class");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class, 0);
        Class<?> result = deserializer.deserialize(parser, context);
        assertInstanceOf(Class.class, result);
        assertEquals(Class.class, result.getClass());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        when(parser.getValueAsString()).thenReturn("java.util.List<java.util.String>");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class, 0);
        JavaType result = deserializer.deserialize(parser, context);
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        when(parser.getValueAsString()).thenReturn("USD");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class, 0);
        Currency result = deserializer.deserialize(parser, context);
        assertInstanceOf(Currency.class, result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        when(parser.getValueAsString()).thenReturn(".*");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class, 0);
        Pattern result = deserializer.deserialize(parser, context);
        assertInstanceOf(Pattern.class, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        when(parser.getValueAsString()).thenReturn("en_US");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class, 0);
        Locale result = deserializer.deserialize(parser, context);
        assertInstanceOf(Locale.class, result);
        assertEquals(Locale.US, result);

        when(parser.getValueAsString()).thenReturn("fr_FR_u_FR");
        result = deserializer.deserialize(parser, context);
        assertEquals(Locale.FRENCH, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        when(parser.getValueAsString()).thenReturn("UTF-8");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class, 0);
        Charset result = deserializer.deserialize(parser, context);
        assertInstanceOf(Charset.class, result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        when(parser.getValueAsString()).thenReturn("America/New_York");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class, 0);
        TimeZone result = deserializer.deserialize(parser, context);
        assertInstanceOf(TimeZone.class, result);
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        when(parser.getValueAsString()).thenReturn("192.168.0.1");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class, 0);
        InetAddress result = deserializer.deserialize(parser, context);
        assertInstanceOf(InetAddress.class, result);
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        when(parser.getValueAsString()).thenReturn("[255.255.255.255]:80");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 0);
        InetSocketAddress result = deserializer.deserialize(parser, context);
        assertInstanceOf(InetSocketAddress.class, result);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);

        when(parser.getValueAsString()).thenReturn("80");
        result = deserializer.deserialize(parser, context);
        assertEquals(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 80), result);
    }
}