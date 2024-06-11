package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer deserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = createObjectIdReader();
        metadata = createPropertyMetadata();
        deserializer = createDeserializer();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testGetName() {
        String expected = "property";
        String actual = objectIdValueProperty.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetType() {
        Class<?> expected = Object.class;
        Class<?> actual = objectIdValueProperty.getType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMetadata() {
        PropertyMetadata expected = metadata;
        PropertyMetadata actual = objectIdValueProperty.getMetadata();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDeserializer() {
        JsonDeserializer expected = deserializer;
        JsonDeserializer actual = objectIdValueProperty.getDeserializer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer newDeserializer = createDeserializer();
        ObjectIdValueProperty expected = new ObjectIdValueProperty(objectIdValueProperty, newDeserializer);
        ObjectIdValueProperty actual = objectIdValueProperty.withValueDeserializer(newDeserializer);
        Assert.assertEquals(expected, actual);
    }

    private ObjectIdReader createObjectIdReader() {
        // create and return ObjectIdReader instance
    }

    private PropertyMetadata createPropertyMetadata() {
        // create and return PropertyMetadata instance
    }

    private JsonDeserializer createDeserializer() {
        // create and return JsonDeserializer instance
    }
}