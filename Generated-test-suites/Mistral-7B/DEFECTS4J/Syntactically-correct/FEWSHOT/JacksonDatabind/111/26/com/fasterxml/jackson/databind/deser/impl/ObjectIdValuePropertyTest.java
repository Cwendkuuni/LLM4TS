package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() throws Exception {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        objectIdReader = new ObjectIdReader(new PropertyName("id"), new TypeIdentifier("java.lang.String"), null, deserializer, nullValueProvider);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newObjectIdValueProperty.getName());
        assertEquals(objectIdReader, newObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(newValueDeserializer);
        assertSame(newValueDeserializer, newObjectIdValueProperty._valueDeserializer);
        assertEquals(objectIdReader, newObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider newNullValueProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(newNullValueProvider);
        assertSame(newNullValueProvider, newObjectIdValueProperty._nullProvider);
        assertEquals(objectIdReader, newObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "idValue";
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(_objectIdValueProperty._valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(id);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonParser.getValueAsString()).thenReturn("idValueString");

        Object instance = mock(Object.class);

        objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "idValue";
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(_objectIdValueProperty._valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(id);

        JsonParser jsonParser = mock(JsonParser.class);
        Object instance = mock(Object.class);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
        assertSame(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object id = "idValue";
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        Object instance = mock(Object.class);

        objectIdValueProperty.set(instance, id);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = "idValue";
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        Object instance = mock(Object.class);

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
        assertSame(instance, result);
    }
}