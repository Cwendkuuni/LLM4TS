package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import com.fasterxml.jackson.databind.util.NullValueProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ObjectIdValuePropertyTest {
    
    @InjectMocks
    private ObjectIdValueProperty property;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata metadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ReadableObjectId readableObjectId;

    @Mock
    private SettableBeanProperty idProperty;

    @Mock
    private Object instance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullPropertyName() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withName(null);
        assertNull(newProperty.getFullName());
    }

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withName(new PropertyName("newName"));
        assertEquals("newName", newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        assertNull(property.deserializeAndSet(jsonParser, deserializationContext, instance));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Object();
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(readableObjectId.bindItem(any())).thenReturn(readableObjectId);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(readableObjectId).bindItem(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(null);
        property.set(instance, new Object());
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, value)).thenReturn(instance);
        assertEquals(instance, property.setAndReturn(instance, value));
    }
}