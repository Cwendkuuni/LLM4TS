package com.example.test.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("path/to/file.txt");

        File result = deserializer.deserialize(parser, context);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testDeserialize_URL() throws IOException, URISyntaxException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("http://example.com");

        URL result = deserializer.deserialize(parser, context);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserialize_URI() throws IOException, URISyntaxException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("http://example.com/path/to");

        URI result = deserializer.deserialize(parser, context);
        assertEquals(new URI("http://example.com/path/to"), result);
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("java.lang.String");

        String result = deserializer.deserialize(parser, context);
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType expectedType = objectMapper.getTypeFactory().constructParametrizedType(List.class, Integer.class);

        when(parser.getValueAsString()).thenReturn("java.util.List<java.lang.Integer>");

        JavaType result = deserializer.deserialize(parser, context);
        assertEquals(expectedType, result);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("EUR");

        Currency result = deserializer.deserialize(parser, context);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("\\d+");

        Pattern result = deserializer.deserialize(parser, context);
        assertEquals(Pattern.compile("\\d+"), result);
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("en_US");

        Locale result = deserializer.deserialize(parser, context);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("UTF-8");

        Charset result = deserializer.deserialize(parser, context);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("UTC");

        TimeZone result = deserializer.deserialize(parser, context);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("127.0.0.1");

        InetAddress result = deserializer.deserialize(parser, context);
        assertEquals(InetAddress.getByName("127.0.0.1"), result);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("[255.255.255.255]:8080");

        InetSocketAddress result = deserializer.deserialize(parser, context);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), result);
    }

    @Test
    public void testDeserialize_StringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("");

        StringBuilder result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testEmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("");

        String result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFormatException() throws IOException {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("invalid");

        deserializer.deserialize(parser, context);
    }

    @Test
    public void testUnexpectedToken() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn("");
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        assertNull(deserializer.deserialize(parser, context));
    }
}