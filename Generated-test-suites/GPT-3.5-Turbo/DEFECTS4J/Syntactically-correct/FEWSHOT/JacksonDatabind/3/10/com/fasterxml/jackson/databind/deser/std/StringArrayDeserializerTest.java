package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(elementDeserializer.getNullValue()).thenReturn(null);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo");

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(jp, ctxt);
        
        verify(elementDeserializer).deserialize(eq(jp), eq(ctxt));
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        verify(typeDeserializer).deserializeTypedFromArray(eq(jp), eq(ctxt));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertArrayEquals(new String[] {null}, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> elementDeserializer = mock(JsonDeserializer.class);

        when(ctxt.constructType(String.class)).thenReturn(TypeFactory.defaultInstance().constructType(String.class));

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        verify(ctxt).constructType(String.class);
        assertEquals(deserializer, result);
    }
}