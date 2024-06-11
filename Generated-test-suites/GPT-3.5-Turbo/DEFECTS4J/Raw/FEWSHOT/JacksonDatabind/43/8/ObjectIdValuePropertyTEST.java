package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty property;
    @Mock
    private ObjectIdReader objectIdReader;
    @Mock
    private PropertyMetadata metadata;
    @Mock
    private JsonDeserializer<Object> deserializer;
    @Mock
    private Object id;
    @Mock
    private JsonParser p;
    @Mock
    private DeserializationContext ctxt;
    @Mock
    private Object instance;
    @Mock
    private ReadableObjectId roid;
    @Mock
    private SettableBeanProperty idProp;

    @Before
    public void setUp() {
        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        property = new ObjectIdValueProperty(objectIdReader, metadata);
        property._objectIdReader = objectIdReader;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(deserializer.deserialize(p, ctxt)).thenReturn(id);
        when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        when(roid.bindItem(instance)).thenReturn(mock(ObjectIdReference.class));
        property.deserializeAndSet(p, ctxt, instance);
        verify(deserializer).deserialize(p, ctxt);
        verify(ctxt).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        verify(roid).bindItem(instance);
        verifyZeroInteractions(idProp);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(deserializer.deserialize(p, ctxt)).thenReturn(id);
        when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        when(roid.bindItem(instance)).thenReturn(mock(ObjectIdReference.class));
        property.deserializeSetAndReturn(p, ctxt, instance);
        verify(deserializer).deserialize(p, ctxt);
        verify(ctxt).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        verify(roid).bindItem(instance);
        verifyZeroInteractions(idProp);
    }

    @Test
    public void testDeserializeSetAndReturn_NullId_ReturnsNull() throws IOException {
        when(deserializer.deserialize(p, ctxt)).thenReturn(null);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
        verify(deserializer).deserialize(p, ctxt);
        verifyZeroInteractions(ctxt, roid, idProp);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NullIdProperty_ThrowsUnsupportedOperationException() throws IOException {
        property.setAndReturn(instance, id);
    }
}