package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo.IdClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;
    
    @Mock
    private PropertyName propertyName;
    
    @Mock
    private PropertyMetadata metadata;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private AnnotatedMember member;
    
    @Mock
    private JsonDeserializer<?> valueDeserializer;
    
    @Mock
    private SettableBeanProperty idProperty;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private ReadableObjectId roid;
    
    private ObjectIdValueProperty objectIdValueProperty;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(idProperty);
        
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty._valueDeserializer = valueDeserializer;
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty._propertyName);
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty._type);
        assertEquals(metadata, objectIdValueProperty._metadata);
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty._valueDeserializer);
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        
        assertEquals(objectIdValueProperty._objectIdReader, src._objectIdReader);
        assertEquals(deserializer, src._valueDeserializer);
        assertEquals(objectIdValueProperty, src._propName);
        assertEquals(objectIdValueProperty._metadata, src._metadata);
    }
    
    @Test
    public void testConstructor3() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdValueProperty, newName);
        
        assertEquals(objectIdValueProperty._objectIdReader, src._objectIdReader);
        assertEquals(objectIdValueProperty._valueDeserializer, src._valueDeserializer);
        assertEquals(newName, src._propName);
        assertEquals(objectIdValueProperty._metadata, src._metadata);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Override.class));
    }
    
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        
        when(valueDeserializer.deserialize(jsonParser, ctxt)).thenReturn("123");
        when(ctxt.findObjectId("123", objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        
        assertEquals(instance, objectIdValueProperty.deserializeSetAndReturn(jsonParser, ctxt, instance));
        
        verify(roid, times(1)).bindItem(instance);
        verify(idProperty, times(1)).setAndReturn(instance, "123");
    }
    
    @Test
    public void testDeserializeAndSetWithNullId() throws IOException {
        Object instance = new Object();
        
        when(valueDeserializer.deserialize(jsonParser, ctxt)).thenReturn(null);
        
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, ctxt, instance));
        
        verifyZeroInteractions(roid);
        verifyZeroInteractions(idProperty);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        Object instance = new Object();
        
        when(objectIdReader.idProperty).thenReturn(null);
        
        objectIdValueProperty.set(instance, "123");
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        
        when(objectIdReader.idProperty).thenReturn(idProperty);
        
        when(idProperty.setAndReturn(instance, "123")).thenReturn(instance);
        
        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, "123"));
    }
    
}