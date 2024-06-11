package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;

import org.junit.Test;
import org.mockito.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializationAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, Mockito.CALLS_REAL_METHODS);

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(jsonParser);
        verify(deserializationContext, times(1)).findObjectId(any(), any(), any());
        verifyNoMoreInteractions(deserializationContext);
        verify(property, never()).setAndReturn(any(), any());

    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, Mockito.CALLS_REAL_METHODS);
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(property._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(deserializationContext.findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver)).thenReturn(readableObjectId);

        property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(jsonParser);
        verify(property._valueDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        verify(deserializationContext, times(1)).findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver);
        verify(readableObjectId, times(1)).bindItem(instance);
        verifyNoMoreInteractions(deserializationContext);
        verify(property, never()).setAndReturn(any(), any());

    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, Mockito.CALLS_REAL_METHODS);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        property._objectIdReader.idProperty = idProp;

        property.set(instance, value);

        verify(idProp, times(1)).setAndReturn(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, Mockito.CALLS_REAL_METHODS);
        property.set(instance, value);
    }

}