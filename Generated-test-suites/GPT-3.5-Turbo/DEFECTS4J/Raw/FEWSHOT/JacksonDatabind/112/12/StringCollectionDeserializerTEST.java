package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.junit.Test;

public class StringCollectionDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaTypeCollection,
                ValueDeserializer,
                NullValueProvider,
                Boolean
        );
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<String>();

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        Mockito.when(parser.nextTextValue()).thenReturn("foo", null);

        deserializer.deserialize(parser, context, result);

        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaTypeCollection,
                ValueDeserializer,
                NullValueProvider,
                Boolean
        );
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        Mockito.when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn("foo");

        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        assertEquals("foo", result);
    }

    @Test
    public void testHandleNonArrayUnwrapped() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaTypeCollection,
                ValueDeserializer,
                NullValueProvider,
                Boolean.FALSE
        );
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<String>();

        Mockito.when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        result = deserializer.handleNonArray(parser, context, result);

        assertEquals(0, result.size());
    }

    @Test
    public void testHandleNonArrayWrapped() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaTypeCollection,
                ValueDeserializer,
                NullValueProvider,
                Boolean.TRUE
        );
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<String>();

        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText()).thenReturn("foo");

        result = deserializer.handleNonArray(parser, context, result);

        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }
}