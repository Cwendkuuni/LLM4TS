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
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private ObjectIdValueProperty property;

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
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testWithname() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property.getObjectIdReader(), newProperty.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(property.getObjectIdReader(), newProperty.getObjectIdReader());
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
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);
        verify(property).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        when(property.getValueDeserializer().deserialize(p, ctxt)).thenReturn(id);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        verify(ctxt).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        verify(objectIdReader.generator).bindItem(instance);
        verify(property).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.idProperty).thenReturn(null);

        property.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        ObjectIdValueProperty idPropertyProperty = spy(new ObjectIdValueProperty(objectIdReader, metadata));
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.idProperty).thenReturn(idProperty);
        doReturn(value).when(idProperty).setAndReturn(instance, value);

        Object result = idPropertyProperty.setAndReturn(instance, value);
        assertEquals(value, result);
        verify(objectIdReader.idProperty).setAndReturn(instance, value);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
        verify(property).setAndReturn(instance, value);
    }
}