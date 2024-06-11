package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;

    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader();
        propertyMetadata = new PropertyMetadata();
        deserializer = new JsonDeserializer<>();
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        assertNotNull(property);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer);
        assertNotNull(property);
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        String newName = "new name";
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertNotNull(property);
    }

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        PropertyName newName = new PropertyName("new name");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertNotNull(newProperty);
    }

    @Test
    public void testWithNewDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertNotNull(newProperty);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Class<A> acls = A.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            Object result = property.setAndReturn(instance, value);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
}