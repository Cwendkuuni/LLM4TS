package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;

import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class SetterlessPropertyTest {
    
    // Test deserializeAndSet method with JsonToken.VALUE_NULL
    @Test
    public void testDeserializeAndSetWithJsonNull() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SetterlessProperty property = mock(SetterlessProperty.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserializeAndSet(parser, context, null);

        verifyZeroInteractions(context);
        verifyZeroInteractions(property);
    }

    // Test deserializeAndSet method with null valueDeserializer
    @Test
    public void testDeserializeAndSetWithNullValueDeserializer() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SetterlessProperty property = mock(SetterlessProperty.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(property.getValueTypeDeserializer()).thenReturn(null);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        verify(property, times(1)).getValueTypeDeserializer();
        verify(property, times(1)).getValueDeserializer();
        verify(property, times(1)).getName();
        verify(parser, times(1)).getCurrentToken();
        verify(parser, times(1)).getText();
        verify(parser, times(1)).readValueAs(any(), any());
    }

    // Test deserializeAndSet method with non-null valueDeserializer
    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SetterlessProperty property = mock(SetterlessProperty.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);            
        when(property.getValueTypeDeserializer()).thenReturn(mock(JsonDeserializer.class));

        Object instance = new Object();
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("Exception message does not match", "Problem deserializing 'setterless' property (\"null\"): no way to handle typed deser with setterless yet", e.getMessage());
        }

        verify(property, times(1)).getValueTypeDeserializer();
        verifyZeroInteractions(parser);
        verifyZeroInteractions(context);
    }

    // Test deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SetterlessProperty property = mock(SetterlessProperty.class);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        verifyZeroInteractions(parser);
        verifyZeroInteractions(context);
        verifyZeroInteractions(property);
    }

    // Test set method
    @Test
    public void testSet() throws Exception {
        SetterlessProperty property = new SetterlessProperty(mock(PropertyMetadata.class), mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));

        try {
            property.set(new Object(), new Object());
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message does not match", "Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }

    // Test setAndReturn method
    @Test
    public void testSetAndReturn() throws Exception {
        SetterlessProperty property = new SetterlessProperty(mock(PropertyMetadata.class), mock(JavaType.class), mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));

        try {
            property.setAndReturn(new Object(), new Object());
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message does not match", "Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }
    
}