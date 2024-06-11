package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testGetName() {
        String name = property.getName();

        assertNotNull(name);
        assertEquals("propertyName", name);
    }

    @Test
    public void testGetIdType() {
        Class<?> idType = property.getIdType();

        assertNotNull(idType);
        assertEquals(Object.class, idType);
    }

    @Test
    public void testGetMetadata() {
        PropertyMetadata metadata = property.getMetadata();

        assertNotNull(metadata);
    }

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<?> deserializer = property.getDeserializer();

        assertNotNull(deserializer);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty newProperty = property.withName(newName);

        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new TestJsonDeserializer();

        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertNotNull(newProperty);
        assertEquals(deserializer, newProperty.getDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        Class<Deprecated> acls = Deprecated.class;

        Deprecated annotation = property.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = property.getMember();

        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: implement this test
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: implement this test
    }

    @Test
    public void testSet() {
        // TODO: implement this test
    }

    @Test
    public void testSetAndReturn() {
        // TODO: implement this test
    }

    private static class TestJsonDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}