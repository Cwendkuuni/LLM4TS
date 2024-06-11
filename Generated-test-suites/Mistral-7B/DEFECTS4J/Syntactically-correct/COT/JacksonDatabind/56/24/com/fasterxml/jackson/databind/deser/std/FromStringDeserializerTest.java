package com.example.test; // replace with your test package if needed

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static final Class<?>[] supportedTypes = FromStringDeserializer.types();

    @Test
    public void testTypes() {
        for (Class<?> type : supportedTypes) {
            StdSerializer<?> deserializer = FromStringDeserializer.findDeserializer(type);
            assertEquals(type, deserializer.getValueClass());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeNullParser() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserialize(null, mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object result = deserializer.deserialize(createMockParser(), ctxt);
        assertNull(result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findClass("InvalidClass")).thenThrow(new IOException("Test error"));

        deserializer.deserialize(createMockParser(), ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidType() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser parser = createMockParser();
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.setValue(new Object());

        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeStringToFile() throws IOException {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);

        File result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(new File("test"), result);
    }

    @Test
    public void testDeserializeStringToURL() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);

        URL result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(new URL("http://test.com"), result);
    }

    @Test
    public void testDeserializeStringToURI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);

        URI result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(new URI("http://test.com"), result);
    }

    @Test
    public void testDeserializeStringToClass() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);

        Class<?> result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeStringToJavaType() throws IOException {
        FromStringDeserializer<Integer.TYPE> deserializer = new FromStringDeserializer<Integer.TYPE>(Integer.TYPE.class);

        JavaType<Integer> result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(Integer.TYPE, result.getType());
    }

    @Test
    public void testDeserializeStringToCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);

        Currency result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializeStringToPattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);

        Pattern result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(Pattern.compile("[^a-z]+"), result);
    }

    @Test
    public void testDeserializeStringToLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);

        Locale result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeStringToCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);

        Charset result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);

        TimeZone result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeStringToInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        InetAddress result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testDeserializeStringToInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        InetSocketAddress result = deserializer.deserialize(createMockParser(), mock(DeserializationContext.class));
        assertEquals(new InetSocketAddress("192.168.0.1", 80), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJsonString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.mappingException(_valueClass)).thenThrow(new JsonMappingException("Test error"));

        deserializer.deserialize(createMockParser(), ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectNull() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object obj = null;

        try {
            deserializer._deserializeEmbedded(obj, mock(DeserializationContext.class));
        } catch (IOException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }

        JsonMappingException expectedException = new JsonMappingException("Don't know how to convert embedded Object of type null into Object");
        expectedException.initCause(new NullPointerException());
        throw expectedException;
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectUnsupportedType() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object obj = new Object();

        deserializer._deserializeEmbedded(obj, mock(DeserializationContext.class));
    }

    private JsonParser createMockParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        return parser;
    }
}