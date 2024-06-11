package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader.propertyName, property.propertyName);
        assertSame(objectIdReader.getIdType(), property.getType());
        assertSame(metadata, property.getMetadata());
        assertSame(deserializer, property.getValueDeserializer());
        assertSame(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer);

        assertSame(src, property.getSrcProperty());
        assertSame(deserializer, property.getValueDeserializer());
        assertSame(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertSame(src, property.getSrcProperty());
        assertSame(newName, property.propertyName);
        assertSame(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        ObjectIdValueProperty newProperty = property.withName(newName);

        assertSame(src, newProperty.getSrcProperty());
        assertSame(newName, newProperty.propertyName);
        assertSame(src._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertSame(objectIdReader.propertyName, newProperty.propertyName);
        assertSame(objectIdReader.getIdType(), newProperty.getType());
        assertSame(metadata, newProperty.getMetadata());
        assertSame(deserializer, newProperty.getValueDeserializer());
        assertSame(objectIdReader, newProperty._objectIdReader);
    }

    @SuppressWarnings("unused")
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));

        assertNull(property.getAnnotation(null));
    }

    @SuppressWarnings("unused")
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));

        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));

        property.set(instance, value);
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, Mockito.mock(PropertyMetadata.class));

        Object result = property.setAndReturn(instance, value);

        assertSame(instance, result);
        verify(objectIdReader.idProperty).setAndReturn(instance, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(instance);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, Mockito.mock(PropertyMetadata.class));
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);

        property.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).findObjectId(instance, objectIdReader.generator, objectIdReader.resolver);
        verify(roid).bindItem(instance);
        verify(objectIdReader.idProperty, never()).setAndReturn(instance, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSetWithIdProperty() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, Mockito.mock(PropertyMetadata.class));
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);

        property.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).findObjectId(instance, objectIdReader.generator, objectIdReader.resolver);
        verify(roid).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, instance);
    }
}