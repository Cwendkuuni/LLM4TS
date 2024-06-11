package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> jsonDeserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testConstructor1() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.name);
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testConstructor2() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty = new ObjectIdValueProperty(src, jsonDeserializer);
        assertEquals(src.name, objectIdValueProperty.name);
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(jsonDeserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(src._objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testConstructor3() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        PropertyName newName = mock(PropertyName.class);
        objectIdValueProperty = new ObjectIdValueProperty(src, newName);
        assertEquals(newName, objectIdValueProperty.name);
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(src.getValueDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(src._objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newProperty.name);
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
        assertEquals(objectIdValueProperty.name, newProperty.name);
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(jsonDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(null));
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(parser, context, instance);
        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, objectIdValueProperty._valueDeserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        assertEquals(objectIdReader.idProperty.setAndReturn(instance, objectIdValueProperty._valueDeserializer.deserialize(parser, context)), objectIdValueProperty.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdValueProperty._valueDeserializer.deserialize(parser, context)).thenReturn(null);
        assertNull(objectIdValueProperty.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        assertEquals(objectIdReader.idProperty.setAndReturn(instance, value), objectIdValueProperty.setAndReturn(instance, value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.idProperty).thenReturn(null);
        objectIdValueProperty.setAndReturn(instance, value);
    }
}