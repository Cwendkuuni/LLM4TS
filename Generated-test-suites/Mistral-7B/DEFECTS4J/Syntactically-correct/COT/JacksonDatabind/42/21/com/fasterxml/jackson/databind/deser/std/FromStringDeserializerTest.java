package com.example.test.jackson;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JavaType TYPE_URL = MAPPER.getTypeFactory().constructType(URL.class);

    @Test
    public void testDeserializeURL() throws IOException, URISyntaxException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("http://example.com");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        when(ctxt.findClass(Mockito.anyString())).thenReturn(URL.class);
        when(ctxt.getTypeFactory()).thenReturn(MAPPER.getTypeFactory());

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        T result = deserializer.deserialize(parser, ctxt);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test(expected = MismatchedInputException.class)
    public void testDeserializeURLWithTwoValues() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("[\"http://example.com\", \"https://example.com\"]");

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeURLWithInvalidString() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("invalid_string");

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeURI() throws IOException, URISyntaxException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("http://example.com/path/to/resource");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        T result = deserializer.deserialize(parser, ctxt);
        assertEquals(new URI("http://example.com/path/to/resource"), result);
    }

    @Test(expected = URISyntaxException.class)
    public void testDeserializeURIWithInvalidString() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("invalid_string");

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnknownType() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn("unknown_type");

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class, 0);
        deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    private static class MockitoJUnitRunner {}
}