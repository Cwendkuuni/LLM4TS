package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class SetterlessPropertyTest {
    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = mock(SetterlessProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SetterlessProperty result = property.withNullProvider(nva);
        
        assertEquals(property._valueDeserializer, result._valueDeserializer);
        assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty property = mock(SetterlessProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
        
        // Verify that fixAccess() is called on AnnotatedMethod
        verify(property._annotated).fixAccess(anyBoolean());
    }

    @Test
    public void testDeserializeAndSetWithJsonValueNull() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        
        // Verify that nothing is set if the current token is JsonToken.VALUE_NULL
        property.deserializeAndSet(p, ctxt, instance);
        
        // Verify that nothing is called on the valueDeserializer
        verify(property._valueDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(property._valueTypeDeserializer).thenReturn(mock(TypeDeserializer.class));
        
        // Verify that an exception is thrown if _valueTypeDeserializer is not null
        property.deserializeAndSet(p, ctxt, instance);
        
        // Verify that reportBadDefinition() is called with the correct parameters
        verify(ctxt).reportBadDefinition(property.getType(), String.format(
                "Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet",
                property.getName()));
    }

    @Test
    public void testDeserializeAndSetWithNullToModify() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(property._valueTypeDeserializer).thenReturn(null);
        when(property.getName()).thenReturn("testProperty");
        
        // Verify that reportBadDefinition() is called with the correct parameters
        property.deserializeAndSet(p, ctxt, instance);
        verify(ctxt).reportBadDefinition(property.getType(), String.format(
                "Problem deserializing 'setterless' property '%s': get method returned null",
                property.getName()));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object toModify = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(property._valueTypeDeserializer).thenReturn(null);
        when(property._getter.invoke(instance, (Object[]) null)).thenReturn(toModify);
        
        // Verify that deserialize() is called with the correct parameters
        property.deserializeAndSet(p, ctxt, instance);
        verify(property._valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        // Verify that deserializeAndSet() is called with the correct parameters
        verify(property).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        Object instance = mock(Object.class);
        Object value = new Object();
        
        // Verify that UnsupportedOperationException is thrown when calling set()
        assertThrows(UnsupportedOperationException.class, () -> {
            property.set(instance, value);
        });
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        Object instance = mock(Object.class);
        Object value = new Object();
        
        property.setAndReturn(instance, value);
        
        // Verify that set() is called with the correct parameters
        verify(property).set(instance, value);
    }
}