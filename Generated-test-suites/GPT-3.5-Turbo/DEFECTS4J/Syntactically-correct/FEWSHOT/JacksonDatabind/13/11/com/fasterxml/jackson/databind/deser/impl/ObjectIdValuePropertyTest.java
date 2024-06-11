package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithDeser() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithDeserNull() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(null);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertNull(newProperty.getValueDeserializer());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testGetName() {
        assertEquals(objectIdReader.propertyName, property.getName());
    }

    @Test
    public void testGetType() {
        assertEquals(objectIdReader.getIdType(), property.getType());
    }

    @Test
    public void testGetMetadata() {
        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testGetValueDeserializer() {
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithDeserAndName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithDeserAndNameString() {
        String newName = "newName";
        PropertyName expectedName = new PropertyName(newName);
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(expectedName, newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        property.deserializeAndSet(jp, ctxt, instance);

        // Verify that deserializeSetAndReturn() is called
        verify(property).deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        Object id = mock(Object.class);

        when(property._objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(property._objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(ctxt.findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver)).thenReturn(roid);
        when(property._objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
        verify(property).deserializeSetAndReturn(jp, ctxt, instance);
        verify(roid).bindItem(instance);
        verify(property._objectIdReader.idProperty).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.setAndReturn(instance, value);
    }
}