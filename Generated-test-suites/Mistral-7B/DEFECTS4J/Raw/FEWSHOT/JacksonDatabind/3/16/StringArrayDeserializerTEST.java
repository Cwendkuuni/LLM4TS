package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final StringArrayDeserializer STD_DESERIALIZER = StringArrayDeserializer.instance;
    private static final ObjectBuffer BUFFER = new ObjectBuffer(16);

    @Test
    public void testDeserializeStandard() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("foo");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("bar");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = STD_DESERIALIZER.deserialize(mockParser, ctxt);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("foo");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("bar");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(ctxt.findContextualValueDeserializer(String[].class, (BeanProperty) null)).thenReturn(mockElementDeserializer);

        when(mockElementDeserializer.deserialize(mockParser, ctxt)).thenReturn("baz");

        String[] result = STD_DESERIALIZER.deserialize(mockParser, ctxt);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("foo");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("bar");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(ctxt.deserializeTypedFromArray(mockParser, ctxt)).thenReturn(new TextNode[]{"foo", "bar"});

        Object[] array = STD_DESERIALIZER.deserializeWithType(mockParser, ctxt, mockTypeDeserializer);
        assertArrayEquals(new String[]{"foo", "bar"}, (String[]) array);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("foo");

        String[] result = STD_DESERIALIZER.handleNonArray(mockParser, ctxt);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(String.class, property)).thenReturn(mockElementDeserializer);

        JsonDeserializer<String> result = STD_DESERIALIZER.createContextual(ctxt, property);

        assertEquals(mockElementDeserializer, result);
    }
}