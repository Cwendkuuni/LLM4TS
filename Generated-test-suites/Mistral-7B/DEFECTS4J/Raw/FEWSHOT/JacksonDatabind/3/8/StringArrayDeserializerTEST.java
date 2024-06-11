package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, context);

        verify(parser, times(2)).nextToken();
        verify(context).leaseObjectBuffer();
        verifyNoMoreInteractions(parser, context, buffer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);

        when(parser.getText()).thenReturn("");
        when(parser.isEnabled(eq(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))).thenReturn(true);

        String[] result = deserializer.deserialize(parser, context);

        verify(parser, times(1)).getText();
        verify(parser, times(1)).isEnabled(eq(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT));
        verifyNoMoreInteractions(parser, context, buffer);

        assertNull(result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("test");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("test2");
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, context);

        verify(parser, times(6)).nextToken();
        verify(context).leaseObjectBuffer();
        verifyNoMoreInteractions(parser, context, buffer);

        assertArrayEquals(new String[]{"test", null, "test2"}, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("test");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("test2");
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn(new String[]{"test", null, "test2"});

        String[] result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        verify(parser, times(6)).nextToken();
        verify(context).leaseObjectBuffer();
        verify(typeDeserializer).deserializeTypedFromArray(parser, context);

        assertArrayEquals(new String[]{"test", null, "test2"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("test");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserialize(parser, context);

        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser, context);

        // Test should throw an IOException
    }
}