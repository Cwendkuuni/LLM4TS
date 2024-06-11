package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = new NullValueProvider();
        ObjectIdValueProperty result = objectIdValueProperty.withNullProvider(nullValueProvider);
        assertSame(nullValueProvider, result.getNullValueProvider());
    }

    @Test
    public void testWithAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        Object instance = new Object();
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        assertNull(objectIdValueProperty.setAndReturn(instance, value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        property.setAndReturn(instance, value);
    }
}