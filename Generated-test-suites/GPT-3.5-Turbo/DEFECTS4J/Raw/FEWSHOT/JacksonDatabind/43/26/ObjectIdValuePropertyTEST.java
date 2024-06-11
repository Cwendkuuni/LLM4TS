package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty objectIdValueProperty;
    
    @Mock
    private ObjectIdReader objectIdReader;
    
    @Mock
    private PropertyMetadata metadata;
    
    @Mock
    private JsonDeserializer<Object> deserializer;
    
    @Mock
    private PropertyName newName;
    
    @Mock
    private JsonParser p;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private Object instance;
    
    @Mock
    private JsonDeserializer<?> valueDeserializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(objectIdReader.propertyName).thenReturn("propertyName");
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(metadata.getDeserializer()).thenReturn(deserializer);
        
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testWithNewName() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
        
        assertEquals(newName, newObjectIdValueProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        
        assertEquals(valueDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }
    
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        
        verify(objectIdReader.idProperty, never()).setAndReturn(instance, id);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(objectIdReader.idProperty, never()).setAndReturn(instance, id);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws Exception {
        objectIdValueProperty.setAndReturn(instance, value);
    }
}