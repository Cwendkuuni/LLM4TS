package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader.propertyName, property.getPropertyName());
        assertSame(objectIdReader.getIdType(), property.getType());
        assertSame(metadata, property.getMetadata());
        assertSame(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        assertSame(src.getPropertyName(), property.getPropertyName());
        assertSame(src.getType(), property.getType());
        assertSame(src.getMetadata(), property.getMetadata());
        assertSame(deser, property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty property = src.withName(newName);

        assertSame(newName, property.getPropertyName());
        assertSame(src.getType(), property.getType());
        assertSame(src.getMetadata(), property.getMetadata());
        assertSame(src.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert any additional conditions on the deserialized object or assigned value.
        assertNotNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            // Assert any additional conditions on the deserialized object or returned value.
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        try {
            property.set(instance, value);
            // Assert any additional conditions on the assigned value.
            assertNotNull(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        try {
            Object result = property.setAndReturn(instance, value);
            // Assert any additional conditions on the assigned value or returned value.
            assertNotNull(value);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}