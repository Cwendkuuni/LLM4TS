package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
    }

    @Test
    public void testWithPropertyName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertNotNull(newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getAnnotation(Override.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> property.deserializeAndSet(jsonParser, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jsonParser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> property.deserializeSetAndReturn(jsonParser, ctxt, instance));
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> property.set(instance, value));
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> property.setAndReturn(instance, value));
    }

}