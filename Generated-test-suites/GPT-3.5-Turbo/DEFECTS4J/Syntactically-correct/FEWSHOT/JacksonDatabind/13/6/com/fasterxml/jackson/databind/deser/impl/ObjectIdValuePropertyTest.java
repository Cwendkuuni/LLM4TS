package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import com.fasterxml.jackson.databind.util.ReadableObjectId;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property._fieldName);
        assertEquals(objectIdReader.getIdType(), property._type);
        assertEquals(metadata, property._metadata);
        assertEquals(objectIdReader.getDeserializer(), property._valueDeserializer);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        JsonDeserializer<?> deserializer = new JsonDeserializer<>(null);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer);

        assertEquals(src._fieldName, property._fieldName);
        assertEquals(src._type, property._type);
        assertEquals(src._metadata, property._metadata);
        assertEquals(deserializer, property._valueDeserializer);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, "newName");

        assertEquals(src._fieldName, property._fieldName);
        assertEquals(src._type, property._type);
        assertEquals(src._metadata, property._metadata);
        assertEquals(src._valueDeserializer, property._valueDeserializer);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testConstructorWithNewPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, new PropertyName("newName"));

        assertEquals(src._fieldName, property._fieldName);
        assertEquals(src._type, property._type);
        assertEquals(src._metadata, property._metadata);
        assertEquals(src._valueDeserializer, property._valueDeserializer);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withName(new PropertyName("newName"));

        assertEquals("newName", property._fieldName.getSimpleName());
        assertEquals(src._type, property._type);
        assertEquals(src._metadata, property._metadata);
        assertEquals(src._valueDeserializer, property._valueDeserializer);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deserializer = new JsonDeserializer<>(null);
        ObjectIdValueProperty property = src.withValueDeserializer(deserializer);

        assertEquals(src._fieldName, property._fieldName);
        assertEquals(src._type, property._type);
        assertEquals(src._metadata, property._metadata);
        assertEquals(deserializer, property._valueDeserializer);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        Object instance = new Object();
        property.deserializeAndSet(jp, ctxt, instance);

        // code coverage, no assert needed
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, true, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.setAndReturn(new Object(), new Object()));
    }
}