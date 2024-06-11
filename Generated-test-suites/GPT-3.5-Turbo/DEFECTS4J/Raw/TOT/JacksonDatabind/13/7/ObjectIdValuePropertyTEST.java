
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testGetName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, property.getName());
    }

    @Test
    public void testGetType() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.getIdType(), property.getType());
    }

    @Test
    public void testWithDeser() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty updatedProperty = property.withValueDeserializer(deser);

        assertEquals(deser, updatedProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: Write test for this method
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: Write test for this method
    }

    @Test
    public void testSet() {
        // TODO: Write test for this method
    }

    @Test
    public void testSetAndReturn() {
        // TODO: Write test for this method
    }
}
