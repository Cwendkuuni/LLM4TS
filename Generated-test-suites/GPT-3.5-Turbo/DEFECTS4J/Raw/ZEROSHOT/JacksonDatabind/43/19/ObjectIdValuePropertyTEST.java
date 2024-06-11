package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithSimpleName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty oldProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newProperty = oldProperty.withName(newName);

        assertNotSame(oldProperty, newProperty);
        assertEquals(newName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                return null;
            }
        };
        ObjectIdValueProperty oldProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newProperty = oldProperty.withValueDeserializer(deser);

        assertNotSame(oldProperty, newProperty);
        assertEquals(objectIdReader.propertyName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertSame(deser, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty.getObjectIdReader());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(anyClass));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: test implementation
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: test implementation
    }

    @Test
    public void testSet() {
        // TODO: test implementation
    }

    @Test
    public void testSetAndReturn() {
        // TODO: test implementation
    }
}