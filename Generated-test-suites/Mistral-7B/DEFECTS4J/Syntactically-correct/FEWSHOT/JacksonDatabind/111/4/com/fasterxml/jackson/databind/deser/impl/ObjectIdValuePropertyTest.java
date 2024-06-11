package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.PropertyName;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(IdType.STRING, objectIdValueProperty.getIdType());
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getNullProvider());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(IdType.STRING, objectIdValueProperty.getIdType());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getNullProvider());
    }

    @Test
    public void testObjectIdValuePropertyWithNullProvider() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, nullValueProvider);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(IdType.STRING, objectIdValueProperty.getIdType());
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertEquals(nullValueProvider, objectIdValueProperty.getNullProvider());
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));

        assertEquals("newName", newObjectIdValueProperty.getName());
        assertEquals("id", newObjectIdValueProperty.getBackingField().getName());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializerAndName() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"), valueDeserializer);

        assertEquals("newName", newObjectIdValueProperty.getName());
        assertEquals("id", newObjectIdValueProperty.getBackingField().getName());
        assertEquals(valueDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyWithNullProviderAndName() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"), nullValueProvider);

        assertEquals("newName", newObjectIdValueProperty.getName());
        assertEquals("id", newObjectIdValueProperty.getBackingField().getName());
        assertNull(newObjectIdValueProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newObjectIdValueProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class).getClass()));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getValueAsString()).thenReturn("123");

        objectIdValueProperty.deserializeAndSet(p, mock(DeserializationContext.class), instance);

        assertEquals("123", instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsString()).thenReturn("123");

        assertEquals(instance, objectIdValueProperty.deserializeSetAndReturn(p, mock(DeserializationContext.class), instance));
        assertEquals("123", instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        Object instance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        Object id = new Object();

        objectIdValueProperty.set(instance, id);

        assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        Object instance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        Object id = new Object();

        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, id));
        assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
    }
}