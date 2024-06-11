package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithDeser() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = src.withName(newName);

        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, CALLS_REAL_METHODS);

        property.deserializeAndSet(jp, ctxt, instance);

        verify(property, times(1)).deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = mock(Object.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        when(property._objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(property._objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        when(roid.bindItem(instance)).thenReturn(null);
        when(property._objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, CALLS_REAL_METHODS);

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
        verify(property._valueDeserializer, times(1)).deserialize(jp, ctxt);
        verify(roid, times(1)).bindItem(instance);
        verify(idProp, times(1)).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProp() throws IOException {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, CALLS_REAL_METHODS);

        property.setAndReturn(null, null);
    }

    @Test
    public void testSetAndReturnWithIdProp() throws IOException {
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class, CALLS_REAL_METHODS);
        when(property._objectIdReader.idProperty).thenReturn(idProp);

        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.setAndReturn(instance, value);

        verify(idProp, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mock(ObjectIdReader.class),mock(PropertyMetadata.class));

        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mock(ObjectIdReader.class),mock(PropertyMetadata.class));

        assertNull(property.getMember());
    }

}