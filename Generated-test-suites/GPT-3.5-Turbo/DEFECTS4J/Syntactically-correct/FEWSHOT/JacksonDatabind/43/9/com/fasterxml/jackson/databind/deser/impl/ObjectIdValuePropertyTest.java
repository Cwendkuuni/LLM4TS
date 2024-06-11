package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getInternalName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertEquals(src, property);
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertEquals(src, property);
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = src.withName(newName);

        assertEquals(src, property);
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        assertEquals(src, property);
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        Class<?> acls = mock(Class.class);

        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        when(property.deserializeSetAndReturn(p, ctxt, instance)).thenCallRealMethod();
        when(property.getValueDeserializer()).thenReturn(deserializer);

        property.deserializeAndSet(p, ctxt, instance);

        verify(deserializer).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = mock(Object.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);

        when(property.getValueDeserializer()).thenReturn(deserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        verify(deserializer).deserialize(p, ctxt);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);

        when(property.getInternalIdProperty()).thenReturn(idProp);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);

        Object result = property.setAndReturn(instance, value);

        verify(idProp).setAndReturn(instance, value);
        assertEquals(instance, result);
    }
}