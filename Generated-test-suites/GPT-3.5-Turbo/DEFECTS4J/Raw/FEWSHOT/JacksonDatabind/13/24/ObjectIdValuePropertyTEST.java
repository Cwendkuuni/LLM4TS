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
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testProtectedConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertNotNull(property);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Deprecated
    @Test
    public void testProtectedDeprecatedConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, "newName");

        assertNotNull(property);
        assertEquals("newName", property.getName().getSimpleName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testDeprecatedWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, new PropertyName("newName"));

        assertNotNull(property);
        assertEquals("newName", property.getName().getSimpleName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withName(newName);

        assertNotNull(property);
        assertEquals("newName", property.getName().getSimpleName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        assertNotNull(property);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.deserializeAndSet(jp, ctxt, instance);

        // Add assertions here
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertSame(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.set(instance, value);

        // Add assertions here
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        Object value = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        Object result = property.setAndReturn(instance, value);

        assertSame(value, result);
    }
}