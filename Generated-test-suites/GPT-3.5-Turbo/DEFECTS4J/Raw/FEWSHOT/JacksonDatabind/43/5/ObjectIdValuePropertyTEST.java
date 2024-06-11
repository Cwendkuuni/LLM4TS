package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.impl.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private JsonParser parser;
    private DeserializationContext context;
    private Object instance;
    private Object id;
    private ReadableObjectId roid;
    private SettableBeanProperty idProperty;
    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        instance = new Object();
        id = new Object();
        roid = mock(ReadableObjectId.class);
        idProperty = mock(SettableBeanProperty.class);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(parser, context)).thenReturn(id);
        when(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        property.deserializeAndSet(parser, context, instance);
        verify(roid).bindItem(instance);
        verify(idProperty, never()).setAndReturn(eq(instance), any());
    }

    @Test
    public void testDeserializeAndSetWithNullId() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(parser, context)).thenReturn(null);
        assertNull(property.deserializeAndSet(parser, context, instance));
        verifyZeroInteractions(roid);
        verify(idProperty, never()).setAndReturn(eq(instance), any());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        assertEquals(instance, property.setAndReturn(instance, id));
        verify(idProperty).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(null);
        property.setAndReturn(instance, id);
    }
}