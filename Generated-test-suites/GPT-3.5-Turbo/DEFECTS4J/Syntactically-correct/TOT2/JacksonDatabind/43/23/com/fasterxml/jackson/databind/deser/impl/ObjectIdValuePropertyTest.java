package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithProperty() {
        ObjectIdReader reader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader reader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());
        JsonDeserializer<?> deserializer = new SomeDeserializer();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getDeserializer());
    }

    // Add more test cases here

}