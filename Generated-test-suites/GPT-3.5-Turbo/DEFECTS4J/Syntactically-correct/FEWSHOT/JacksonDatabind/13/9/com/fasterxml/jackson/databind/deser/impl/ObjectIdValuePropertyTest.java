package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertNotNull(property.getDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertNotNull(property);
        assertEquals(src.propertyName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeprecatedCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertNotNull(property);
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertNotNull(property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Class<A> acls = null;
        A annotation = property.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        AnnotatedMember member = property.getMember();

        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        property.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = null;
        Object value = null;

        property.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = null;
        Object value = null;

        property.setAndReturn(instance, value);
    }
}