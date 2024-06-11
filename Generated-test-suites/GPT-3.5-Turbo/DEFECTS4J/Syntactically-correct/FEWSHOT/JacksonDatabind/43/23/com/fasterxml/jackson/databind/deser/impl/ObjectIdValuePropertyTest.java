package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer deserializer;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonParser jsonParser;
    private DeserializationContext context;
    private Object instance;
    private Object value;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        metadata = mock(PropertyMetadata.class);
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        instance = new Object();
        value = new Object();
        // Initialize ObjectIdValueProperty object
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithname() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newIdValueProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newIdValueProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newIdValueProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newIdValueProperty.getMetadata());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newIdValueProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newIdValueProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(objectIdValueProperty.getName(), newIdValueProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newIdValueProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newIdValueProperty.getMetadata());
        assertEquals(newDeserializer, newIdValueProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newIdValueProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }

    @Test
    public void testSet() throws IOException {
        assertNull(objectIdValueProperty.setAndReturn(instance, value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        objectIdValueProperty.setAndReturn(instance, value);
    }
}