package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        assertEquals(reader.propertyName, property.getName());
        assertEquals(reader.getIdType(), property.getType());
        assertEquals(reader.getDeserializer(), property.getValueDeserializer());
        assertEquals(reader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null).withName(newName);
        assertEquals(newName, property.getName());
        assertEquals(reader.getIdType(), property.getType());
        assertEquals(reader.getDeserializer(), property.getValueDeserializer());
        assertEquals(reader, property._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        JsonDeserializer<?> deserializer = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null)
                .withValueDeserializer(deserializer);
        assertEquals(reader.propertyName, property.getName());
        assertEquals(reader.getIdType(), property.getType());
        assertEquals(deserializer, property.getValueDeserializer());
        assertEquals(reader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = null;
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        JsonDeserializer<?> deserializer = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null)
                .withValueDeserializer(deserializer);

        property.deserializeAndSet(parser, context, instance);
        property.deserializeAndSet(parser, context, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = null;
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        JsonDeserializer<?> deserializer = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null)
                .withValueDeserializer(deserializer);

        assertNull(property.deserializeSetAndReturn(parser, context, instance));
        assertNull(property.deserializeSetAndReturn(parser, context, null));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        SettableBeanProperty idProp = reader.idProperty;
        idProp = null;
        try {
            property.set(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty",
                    e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        ObjectIdReader reader = new ObjectIdReader(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        SettableBeanProperty idProp = reader.idProperty;
        idProp = null;
        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty",
                    e.getMessage());
        }
    }

}