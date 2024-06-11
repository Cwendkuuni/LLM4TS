package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdValueProperty property;
    private ObjectIdReader reader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private JsonParser jp;
    private DeserializationContext ctxt;
    private Object instance;
    private SettableBeanProperty idProp;
    
    @Before
    public void setUp() {
        reader = new ObjectIdReader(null, null, null);
        metadata = new PropertyMetadata(null, null, null);
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        property = new ObjectIdValueProperty(reader, metadata);
        jp = null;
        ctxt = null;
        instance = null;
        idProp = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(reader.propertyName, property.getPropertyName());
        assertEquals(reader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getPropertyName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return "new deserializer";
            }
        };
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        assertEquals(property.getPropertyName(), newProperty.getPropertyName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertNotEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals("new deserializer", newProperty.getValueDeserializer().deserialize(jp, ctxt));
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
        assertNull(property.getAnnotation(Annotation.class));
    }
    
    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        property.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        property.deserializeSetAndReturn(jp, ctxt, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        try {
            property.set(instance, null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        try {
            property.setAndReturn(instance, null);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}