package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithProperties() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty objectIdValueProperty = src.withName(newName);
        assertEquals(newName, objectIdValueProperty.getName());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty objectIdValueProperty = src.withValueDeserializer(deser);
        assertEquals(deser, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        SettableBeanProperty idProp = null;
        objectIdValueProperty.set(instance, value);
        try {
            objectIdValueProperty.setAndReturn(instance, value);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}