package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    private ObjectIdReader objectIdReader;

    private PropertyMetadata metadata;

    private JsonDeserializer<?> deserializer;

    private JsonParser jp;

    private DeserializationContext ctxt;

    private Object instance;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        jp = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        assertEquals(instance, objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        objectIdValueProperty.setAndReturn(instance, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        objectIdValueProperty.set(instance, instance);
    }
}