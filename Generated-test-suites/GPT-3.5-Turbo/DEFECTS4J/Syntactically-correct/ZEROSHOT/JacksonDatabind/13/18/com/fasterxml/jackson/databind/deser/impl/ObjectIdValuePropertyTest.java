package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @After
    public void tearDown() {
        objectIdValueProperty = null;
    }

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        ObjectIdValueProperty property = objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, property._valueDeserializer);
    }

    @Test
    public void testGetName() {
        assertEquals("propertyName", objectIdValueProperty.getName().getSimpleName());
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = objectIdValueProperty.withName(newName);
        assertEquals(newName, property.getName());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
        // assert statements
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
        // assert statements
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdValueProperty.set(instance, value);
        // assert statements
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, value);
        // assert statements
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = objectIdValueProperty.getAnnotation(acls);
        assertNull(annotation);
    }
    
    @Test
    public void testGetMember() {
        AnnotatedMember member = objectIdValueProperty.getMember();
        assertNull(member);
    }
}