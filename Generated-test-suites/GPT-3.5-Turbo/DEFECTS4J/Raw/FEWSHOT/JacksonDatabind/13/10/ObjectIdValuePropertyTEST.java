package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getDeserializer().getValueClass());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializationAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.deserializeAndSet(jp, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.setAndReturn(instance, value);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("new");
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        ObjectIdValueProperty property = src.withName(newName);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer deser = new JsonDeserializer();
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        assertEquals(deser, property.getDeserializer());
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

}