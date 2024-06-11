package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    @Mock
    private ReadableObjectId roid;

    @Mock
    private SettableBeanProperty idProp;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(objectIdReader.propertyName()).thenReturn("propertyName");
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(objectIdReader.generator()).thenReturn("generator");
        spy(new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL));
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        when(idProp.setAndReturn(any(), any())).thenReturn(instance);
        when(objectIdReader.idProperty()).thenReturn(idProp);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // setup
        setup();
        
        // execute
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL);
        property.deserializeAndSet(parser, ctxt, instance);
        
        // assert
        verify(property).deserializeSetAndReturn(parser, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // setup
        setup();
        when(deserializer.deserialize(parser, ctxt)).thenReturn("id");
        
        // execute
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL);
        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
        
        // assert
        assertEquals(instance, result);
        verify(ctxt).findObjectId("id", "generator", "resolver");
        verify(roid).bindItem(instance);
        verify(idProp).setAndReturn(instance, "id");
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        // setup
        setup();
        when(deserializer.deserialize(parser, ctxt)).thenReturn(null);
        
        // execute
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL);
        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);
        
        // assert
        assertNull(result);
        verify(ctxt).findObjectId(null, "generator", "resolver");
        verifyZeroInteractions(roid);
        verifyNoMoreInteractions(idProp);
    }

    @Test
    public void testSet() throws IOException {
        // setup
        setup();
        
        // execute
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL);
        property.set(instance, "value");
        
        // assert
        verify(property).setAndReturn(instance, "value");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // setup
        setup();
        
        // execute
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OPTIONAL);
        Object result = property.setAndReturn(instance, "value");
        
        // assert
        assertEquals(instance, result);
        verify(idProp).setAndReturn(instance, "value");
    }
}