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
        ObjectIdValueProperty result = property.withName(newName);

        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty result = property.withValueDeserializer(deser);

        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A result = property.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = property.getMember();

        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Verify object binding
        ReadableObjectId roid = ctxt.findObjectId(id, _objectIdReader.generator, _objectIdReader.resolver);
        assertEquals(instance, roid.item);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Verify object binding
        ReadableObjectId roid = ctxt.findObjectId(id, _objectIdReader.generator, _objectIdReader.resolver);
        assertEquals(instance, roid.item);
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Verify object binding
        SettableBeanProperty idProp = _objectIdReader.idProperty;
        assertEquals(value, idProp.setAndReturn(instance, value));
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        try {
            Object result = property.setAndReturn(instance, value);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Verify object binding
        SettableBeanProperty idProp = _objectIdReader.idProperty;
        assertEquals(value, idProp.setAndReturn(instance, value));
    }
}