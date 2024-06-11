package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nva = NullValueProvider.forValue(new Object());
        SettableBeanProperty newSettableBeanProperty = objectIdValueProperty.withNullProvider(nva);
        assertNotNull(newSettableBeanProperty);
        assertNotNull(newSettableBeanProperty.getIdType());
        assertNull(newSettableBeanProperty.getValueDeserializer());
        assertEquals(nva, newSettableBeanProperty.getNullValueProvider());
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newSettableBeanProperty = objectIdValueProperty.withName(newName);
        assertNotNull(newSettableBeanProperty);
        assertEquals(newName, newSettableBeanProperty.getFullName());
        assertNotNull(newSettableBeanProperty.getIdType());
        assertNull(newSettableBeanProperty.getValueDeserializer());
    }

    @Test
    public void testWithNewValueDeserializer() {
        StdDeserializer<?> deser = new StdDeserializer<>(Object.class);
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty newSettableBeanProperty = objectIdValueProperty.withValueDeserializer(deser);
        assertNotNull(newSettableBeanProperty);
        assertNotNull(newSettableBeanProperty.getIdType());
        assertEquals(deser, newSettableBeanProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory());
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        // Check if the object is null
        assertNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("{\"id\":1}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory());
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Object returnedInstance = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        // Check if the returned instance is the same reference as the original instance
        assertSame(instance, returnedInstance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"id\":1}");
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        ObjectIdReader objectIdReader = new ObjectIdReader(mapper, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        objectIdValueProperty.set(p, ctxt, instance);
        // Check if the object is null
        assertNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"id\":1}");
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        ObjectIdReader objectIdReader = new ObjectIdReader(mapper, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        objectIdValueProperty.setAndReturn(p, ctxt, instance);
        // Expecting an UnsupportedOperationException to be thrown
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(objectIdValueProperty.getAnnotation(Test.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCustomMethod() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(
                new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty.customMethod();
        // Expecting an UnsupportedOperationException to be thrown
    }
}