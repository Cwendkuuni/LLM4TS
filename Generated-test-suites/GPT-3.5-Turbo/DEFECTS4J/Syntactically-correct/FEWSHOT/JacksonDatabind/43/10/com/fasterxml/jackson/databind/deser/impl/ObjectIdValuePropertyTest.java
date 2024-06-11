package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, property.getPropertyName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        assertEquals(src, property.getSourceProperty());
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = src.withName(newName);
        assertEquals(src, property.getSourceProperty());
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertNull(result);
    }
}