package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private JsonDeserializer<ObjectId> objectIdDeserializer;
    private JsonDeserializer<Object> idPropertyDeserializer;

    @org.junit.Before
    public void setUp() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        objectIdDeserializer = mock(JsonDeserializer.class);
        idPropertyDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testConstructors() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty propertyWithDeserializer = new ObjectIdValueProperty(objectIdReader, null, null, objectIdDeserializer);
        ObjectIdValueProperty propertyWithName = new ObjectIdValueProperty(objectIdReader, null, "testName");
        ObjectIdValueProperty propertyWithDeserializerAndName = new ObjectIdValueProperty(objectIdReader, null, "testName", objectIdDeserializer);
        ObjectIdValueProperty propertyWithIdProperty = new ObjectIdValueProperty(objectIdReader, null, null, null, idPropertyDeserializer);
        ObjectIdValueProperty propertyWithIdPropertyAndName = new ObjectIdValueProperty(objectIdReader, null, "testName", null, idPropertyDeserializer);

        assertEquals(objectIdReader, property.getObjectIdReader());
        assertEquals(null, property.getValueDeserializer());
        assertEquals(null, property.getIdProperty());
        assertEquals(null, property.getPropertyName());
        assertEquals(objectIdReader, propertyWithDeserializer.getObjectIdReader());
        assertEquals(objectIdDeserializer, propertyWithDeserializer.getValueDeserializer());
        assertEquals(null, propertyWithDeserializer.getIdProperty());
        assertEquals(null, propertyWithDeserializer.getPropertyName());
        assertEquals(objectIdReader, propertyWithName.getObjectIdReader());
        assertEquals(null, propertyWithName.getValueDeserializer());
        assertEquals("testName", propertyWithName.getPropertyName());
        assertEquals(null, propertyWithIdProperty.getValueDeserializer());
        assertEquals(null, propertyWithIdProperty.getIdProperty());
        assertEquals(null, propertyWithIdProperty.getPropertyName());
        assertEquals(null, propertyWithIdPropertyAndName.getValueDeserializer());
        assertEquals(idPropertyDeserializer, propertyWithIdPropertyAndName.getIdProperty());
        assertEquals("testName", propertyWithIdPropertyAndName.getPropertyName());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(new ObjectIdValueProperty(objectIdReader, null).getAnnotation(ObjectId.class));
    }

    @Test
    public void testGetMember() {
        assertNull(new ObjectIdValueProperty(objectIdReader, null).getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Object();
        when(objectIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(id);
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        Object idPropertyValue = property.getIdProperty().get(instance);
        assertEquals(id, idPropertyValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Object();
        when(objectIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(id);
        Object instance = new Object();

        Object returnValue = new Object();
        when(idPropertyDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(returnValue);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null, null, objectIdDeserializer, idPropertyDeserializer);
        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertEquals(instance, result);
        assertEquals(returnValue, property.getIdProperty().get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object id = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.set(instance, id);

        Object idPropertyValue = property.getIdProperty().get(instance);
        assertEquals(id, idPropertyValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object id = new Object();
        Object returnValue = new Object();

        when(idPropertyDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(returnValue);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null, null, null, idPropertyDeserializer);
        Object result = property.setAndReturn(instance, id);

        assertEquals(instance, result);
        assertEquals(returnValue, property.getIdProperty().get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() throws IOException {
        Object instance = new Object();
        Object id = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.setAndReturn(instance, id);
    }
}