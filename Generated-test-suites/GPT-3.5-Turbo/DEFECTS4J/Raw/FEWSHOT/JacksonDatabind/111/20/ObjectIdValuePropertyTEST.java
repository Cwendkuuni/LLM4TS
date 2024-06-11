package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {
  
    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private JsonDeserializer valueDeserializer;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private ReadableObjectId readableObjectId;

    @Mock
    private SettableBeanProperty idProperty;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(null);
        assertNull(newObjectIdValueProperty.getNullProvider());
    }

    @Test
    public void testGetName() {
        PropertyName expected = new PropertyName("name");
        objectIdValueProperty.setName(expected);
        PropertyName result = objectIdValueProperty.getName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetValueDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        JsonDeserializer<?> result = newObjectIdValueProperty.getValueDeserializer();
        assertEquals(valueDeserializer, result);
    }

    @Test
    public void testWithValueDeserializerSame() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        ObjectIdValueProperty result = newObjectIdValueProperty.withValueDeserializer(valueDeserializer);
        assertEquals(newObjectIdValueProperty, result);
    }

    @Test
    public void testWithValueDeserializerDifferent() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        JsonDeserializer<?> differentValueDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty result = newObjectIdValueProperty.withValueDeserializer(differentValueDeserializer);
        assertEquals(differentValueDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newObjectIdValueProperty.getName().getSimple());
    }

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object expected = new Object();
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(expected);
        when(deserializationContext.findObjectId(expected, readableObjectId.generator, objectIdReader.resolver)).thenReturn(readableObjectId);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(instance, result);
        verify(readableObjectId).bindItem(eq(instance));
    }

    @Test
    public void testSetWithNullIdProperty() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(null);

        try {
            objectIdValueProperty.set(instance, new Object());
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        Object expected = new Object();
        when(objectIdReader.getIdProperty()).thenReturn(idProperty);
        when(idProperty.setAndReturn(eq(instance), any())).thenReturn(expected);

        Object result = objectIdValueProperty.setAndReturn(instance, new Object());
        assertEquals(expected, result);
    }
}