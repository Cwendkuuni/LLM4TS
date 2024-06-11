package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    
    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(metadata, newObjectIdValueProperty.getMetadata());
        assertEquals(objectIdReader, newObjectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer<?> deser = new JsonDeserializer<>() {

            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
            
        };
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deser);

        assertEquals(deser, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);

        assertEquals(newName, newObjectIdValueProperty.getFullName());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdValueProperty.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null);

        objectIdReader.idProperty = idProperty;

        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertNull(result);
    }
}