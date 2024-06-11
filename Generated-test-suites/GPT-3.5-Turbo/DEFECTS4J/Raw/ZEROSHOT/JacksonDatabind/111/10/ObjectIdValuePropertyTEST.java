package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withName(newName);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withValueDeserializer(deser);
        assertSame(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withNullProvider(nva);
        assertSame(nva, newProperty.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        property.set(new Object(), new Object());
    }

}