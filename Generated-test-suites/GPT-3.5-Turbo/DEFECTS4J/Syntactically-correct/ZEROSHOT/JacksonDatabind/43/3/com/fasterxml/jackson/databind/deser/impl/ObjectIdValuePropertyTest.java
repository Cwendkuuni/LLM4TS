package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<Object> deserializer;

    @Before
    public void setUp() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newProperty.getFullName());
        assertEquals(objectIdValueProperty.getLinkType(), newProperty.getLinkType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty.getMember(), newProperty.getMember());
        assertEquals(objectIdValueProperty.getAnnotation(Object.class), newProperty.getAnnotation(Object.class));
        assertEquals(objectIdValueProperty.isObjectId(), newProperty.isObjectId());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(objectIdValueProperty.getFullName(), newProperty.getFullName());
        assertEquals(objectIdValueProperty.getLinkType(), newProperty.getLinkType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty.getMember(), newProperty.getMember());
        assertEquals(objectIdValueProperty.getAnnotation(Object.class), newProperty.getAnnotation(Object.class));
        assertEquals(objectIdValueProperty.isObjectId(), newProperty.isObjectId());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        // Set up mocks
        Object id = new Object();
        when(deserializer.deserialize(p, ctxt)).thenReturn(id);
        when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(mock(ReadableObjectId.class));
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        objectIdReader.idProperty = idProp;
        Object returnValue = new Object();
        when(idProp.setAndReturn(instance, id)).thenReturn(returnValue);

        // Perform test
        Object result = objectIdValueProperty.deserializeAndSet(p, ctxt, instance);

        // Verify mocks
        verify(ctxt).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        verify(idProp).setAndReturn(instance, id);

        assertEquals(returnValue, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        // Set up mocks
        when(deserializer.deserialize(p, ctxt)).thenReturn(null);

        // Perform test
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        // Verify mocks
        verifyZeroInteractions(ctxt);

        assertEquals(null, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        Object instance = new Object();
        objectIdValueProperty.setAndReturn(instance, new Object());
    }

}