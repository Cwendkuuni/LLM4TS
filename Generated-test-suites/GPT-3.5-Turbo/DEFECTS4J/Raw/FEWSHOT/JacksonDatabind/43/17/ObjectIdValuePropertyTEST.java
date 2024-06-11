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
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.util.ReadableObjectId;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    
    private ObjectIdValueProperty property;
    
    private JsonParser jsonParser;
    private DeserializationContext ctxt;
    private Object instance;
    private Object id;
    private ReadableObjectId roid;
    private SettableBeanProperty idProp;
    
    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        
        jsonParser = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        id = new Object();
        roid = mock(ReadableObjectId.class);
        idProp = mock(SettableBeanProperty.class);
        
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, ctxt)).thenReturn(id);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(ctxt.findObjectId(id, null, null)).thenReturn(roid);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);
        
        Object result = property.deserializeSetAndReturn(jsonParser, ctxt, instance);
        
        verify(ro-id).bindItem(instance);
        verify(idProp).setAndReturn(instance, id);
        
        assertEquals(instance, result);
    }
    
    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, ctxt)).thenReturn(null);
        
        Object result = property.deserializeSetAndReturn(jsonParser, ctxt, instance);
        
        verifyZeroInteractions(roid);
        assertNull(result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(null);
        
        property.setAndReturn(instance, id);
    }
    
    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);
        
        Object result = property.setAndReturn(instance, id);
        
        assertEquals(instance, result);
    }
}