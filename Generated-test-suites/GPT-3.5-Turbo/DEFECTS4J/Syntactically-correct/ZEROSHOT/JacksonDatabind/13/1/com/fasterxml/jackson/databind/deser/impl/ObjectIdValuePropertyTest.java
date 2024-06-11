package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;

    @Before
    public void setUp() {
        deserializer = mock(JsonDeserializer.class);
        propertyMetadata = new PropertyMetadata("metadata");
        objectIdReader = new ObjectIdReader(new PropertyName("name"), null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testWithDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertSame(newDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithPropertyName() {
        PropertyName propertyName = new PropertyName("name");
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(propertyName);
        assertSame(propertyName, newObjectIdValueProperty.getFullName());
        assertSame(propertyName.getSimpleName(), newObjectIdValueProperty.getName());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Test.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        objectIdValueProperty.set(new Object(), null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        objectIdReader = new ObjectIdReader(new PropertyName("name"), null, idProperty);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.setAndReturn(new Object(), null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        assertNull(objectIdValueProperty.deserializeAndSet(jp, ctxt, instance));
    }
}