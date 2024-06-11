package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullToken() {
        //Arrange
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();

        //Act
        Object result = property.deserializeSetAndReturn(parser, context, null);

        //Assert
        assertNull(result);
    }

    @Test
    public void testWithNonNullToken() {
        //Arrange
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();

        //Act
        Object result = property.deserializeSetAndReturn(parser, context, "test");

        //Assert
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() {
        //Arrange
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        Object instance = new Object();

        //Act
        property.setAndReturn(instance, "test");
    }
}