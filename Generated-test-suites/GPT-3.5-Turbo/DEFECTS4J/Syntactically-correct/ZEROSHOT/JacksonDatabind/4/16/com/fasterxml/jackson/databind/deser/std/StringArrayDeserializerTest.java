
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import static org.junit.Assert.assertEquals;


public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] expected = {"hello", "world"};
        Mockito.when(jp.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jp.nextToken())
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.END_ARRAY);

        Mockito.when(jp.getText()).thenReturn("hello").thenReturn("world");

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<String> elementDeserializer = Mockito.mock(JsonDeserializer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        String[] expected = {"hello", "world"};
        Mockito.when(jp.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jp.nextToken())
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.END_ARRAY);

        Mockito.when(jp.getText()).thenReturn("hello").thenReturn("world");
        Mockito.when(elementDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .thenReturn("hello").thenReturn("world");

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        Mockito.when(typeDeserializer.deserializeTypedFromArray(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
                .thenReturn(new String[]{"hello", "world"});

        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        assertEquals(new String[]{"hello", "world"}, result);
    }
}

