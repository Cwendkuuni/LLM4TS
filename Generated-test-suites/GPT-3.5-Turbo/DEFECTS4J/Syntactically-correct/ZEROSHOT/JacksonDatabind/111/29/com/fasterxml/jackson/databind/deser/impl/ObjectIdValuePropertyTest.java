package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private PropertyName propertyName;

    @Before
    public void setUp() {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        metadata = Mockito.mock(PropertyMetadata.class);
        deserializer = Mockito.mock(JsonDeserializer.class);
        nullValueProvider = Mockito.mock(NullValueProvider.class);
        propertyName = new PropertyName("test");

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getPropertyName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty newObjectIdValueProperty =
                new ObjectIdValueProperty(objectIdValueProperty, deserializer, nullValueProvider);

        assertEquals(objectIdValueProperty, newObjectIdValueProperty.getSource());
        assertEquals(deserializer, newObjectIdValueProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newObjectIdValueProperty.getNullValueProvider());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty newObjectIdValueProperty =
                new ObjectIdValueProperty(objectIdValueProperty, propertyName);

        assertEquals(propertyName, newObjectIdValueProperty.getFullName());
        assertEquals(objectIdValueProperty, newObjectIdValueProperty.getSource());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(propertyName);

        assertEquals(propertyName, newObjectIdValueProperty.getFullName());
        assertEquals(objectIdValueProperty, newObjectIdValueProperty.getSource());
    }

    @Test
    public void testWithValueDeserializerWhenSameDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty =
                objectIdValueProperty.withValueDeserializer(deserializer);

        assertEquals(objectIdValueProperty, newObjectIdValueProperty);
    }

    @Test
    public void testWithValueDeserializerWhenDifferentDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty =
                objectIdValueProperty.withValueDeserializer(Mockito.mock(JsonDeserializer.class));

        assertEquals(objectIdValueProperty, newObjectIdValueProperty.getSource());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(nullValueProvider);

        assertEquals(objectIdValueProperty, newObjectIdValueProperty.getSource());
        assertEquals(nullValueProvider, newObjectIdValueProperty.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Mockito.any()));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSetWhenValueNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(jsonParser.hasToken(Mockito.any())).thenReturn(true);
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testDeserializeAndSetWhenValueNotNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);

        Mockito.when(jsonParser.hasToken(Mockito.any())).thenReturn(false);
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        Mockito.when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        Mockito.when(deserializationContext.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        Mockito.when(roid.bindItem(instance)).thenReturn(null);
        Mockito.when(objectIdReader.idProperty).thenReturn(idProp);
        Mockito.when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        assertEquals(instance, objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testSetWhenIdPropertyIsNull() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        objectIdReader.idProperty = null;

        try {
            objectIdValueProperty.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}