package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader(MAPPER, null, null, null);
        metadata = new PropertyMetadata();
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }            
        };

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty.getHandler(), newProperty.getHandler());
        assertEquals(objectIdValueProperty.getMember(), newProperty.getMember());
        assertEquals(objectIdValueProperty.getAnnotation(Override.class), newProperty.getAnnotation(Override.class));
    }

    @Test
    public void testWithNewDeserializer() {
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return "custom deserializer";
            }            
        };
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertNotNull(newProperty);
        assertEquals(objectIdValueProperty.getFullName(), newProperty.getFullName());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty.getHandler(), newProperty.getHandler());
        assertEquals(objectIdValueProperty.getMember(), newProperty.getMember());
        assertEquals(objectIdValueProperty.getAnnotation(Override.class), newProperty.getAnnotation(Override.class));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        // Test when _valueDeserializer is null
        assertNull(objectIdValueProperty.getValueDeserializer());
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
        
        // Test when _valueDeserializer is not null
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return "value";
            }            
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertNotNull(objectIdValueProperty.getValueDeserializer());
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
        
        // Additional test cases can be added for different scenarios
        
        // Test when idProperty is not null
        String value = "id";
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                assertNotNull(instance);
                assertNotNull(value);
            }
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                assertNotNull(instance);
                assertNotNull(value);
                return value;
            }
        };
        objectIdReader = new ObjectIdReader(MAPPER, null, null, idProperty);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        assertNull(objectIdValueProperty._objectIdReader.idProperty);
        objectIdValueProperty.set(instance, "value");
        
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null);
        objectIdReader = new ObjectIdReader(MAPPER, null, null, idProperty);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(objectIdValueProperty._objectIdReader.idProperty);
        objectIdValueProperty.set(instance, "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        assertNull(objectIdValueProperty._objectIdReader.idProperty);
        objectIdValueProperty.setAndReturn(instance, "value");
        
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null);
        objectIdReader = new ObjectIdReader(MAPPER, null, null, idProperty);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(objectIdValueProperty._objectIdReader.idProperty);
        objectIdValueProperty.setAndReturn(instance, "value");
    }
}