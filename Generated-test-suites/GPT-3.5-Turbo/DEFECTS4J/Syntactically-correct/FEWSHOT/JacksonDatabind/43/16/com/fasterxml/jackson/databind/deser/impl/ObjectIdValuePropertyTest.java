package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import com.fasterxml.jackson.databind.util.ReadableObjectId;
import com.fasterxml.jackson.databind.PropertyMetadata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata metadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ReadableObjectId readableObjectId;

    @Mock
    private SettableBeanProperty idProperty;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newId");
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);

        assertEquals(newName, newProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);

        assertEquals(objectIdReader.propertyName, newProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();

        when(deserializationContext.findObjectId(any(), any(), any())).thenReturn(readableObjectId);

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(deserializer, times(1)).deserialize(jsonParser, deserializationContext);
        verify(readableObjectId, times(1)).bindItem(instance);
        verify(idProperty, times(1)).setAndReturn(instance, null);
    }

    @Test
    public void testDeserializeAndSetWithNonNullId() throws IOException {
        Object instance = new Object();
        Object id = new Object();

        when(deserializationContext.findObjectId(eq(id), any(), any())).thenReturn(readableObjectId);

        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(readableObjectId, times(1)).bindItem(instance);
        verify(idProperty, times(1)).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.idProperty).thenReturn(null);

        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(idProperty.setAndReturn(instance, value)).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
    }
}