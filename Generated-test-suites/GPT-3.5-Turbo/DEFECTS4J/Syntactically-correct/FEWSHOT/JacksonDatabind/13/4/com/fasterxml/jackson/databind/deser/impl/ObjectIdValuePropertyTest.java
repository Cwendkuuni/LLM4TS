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

public class ObjectIdValuePropertyTest {

    private JsonDeserializer<?> mockDeserializer;
    private ObjectIdReader mockReader;
    private PropertyMetadata mockMetadata;
    
    @Before
    public void setUp() {
        mockDeserializer = mock(JsonDeserializer.class);
        mockReader = mock(ObjectIdReader.class);
        mockMetadata = mock(PropertyMetadata.class);
    }

    @Test
    public void testConstruction() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockReader, mockMetadata);
        
        assertEquals(mockReader.propertyName, property.getName());
        assertEquals(mockReader.getIdType(), property.getType());
        assertEquals(mockMetadata, property.getMetadata());
        assertEquals(mockReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(mockReader, property.getObjectIdReader());
    }

    @Test
    public void testConstructionWithDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(mockReader, mockMetadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, mockDeserializer);
        
        assertEquals(mockReader.propertyName, property.getName());
        assertEquals(mockReader.getIdType(), property.getType());
        assertEquals(mockMetadata, property.getMetadata());
        assertEquals(mockDeserializer, property.getValueDeserializer());
        assertEquals(mockReader, property.getObjectIdReader());
    }

    @Test
    public void testConstructionWithNewName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(mockReader, mockMetadata);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty property = src.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(mockReader.getIdType(), property.getType());
        assertEquals(mockMetadata, property.getMetadata());
        assertEquals(mockReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(mockReader, property.getObjectIdReader());
    }

    @Test
    public void testConstructionWithNewDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(mockReader, mockMetadata);
        ObjectIdValueProperty property = src.withValueDeserializer(mockDeserializer);
        
        assertEquals(mockReader.propertyName, property.getName());
        assertEquals(mockReader.getIdType(), property.getType());
        assertEquals(mockMetadata, property.getMetadata());
        assertEquals(mockDeserializer, property.getValueDeserializer());
        assertEquals(mockReader, property.getObjectIdReader());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object mockInstance = mock(Object.class);
        Object mockId = mock(Object.class);
        ReadableObjectId mockReadableObjectId = mock(ReadableObjectId.class);
        SettableBeanProperty mockIdProperty = mock(SettableBeanProperty.class);
        
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(mockId);
        when(mockContext.findObjectId(mockId, mockReader.generator, mockReader.resolver)).thenReturn(mockReadableObjectId);
        when(mockReadableObjectId.bindItem(mockInstance)).thenReturn(null);
        when(mockReader.idProperty).thenReturn(mockIdProperty);
        when(mockIdProperty.setAndReturn(mockInstance, mockId)).thenReturn(mockInstance);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockReader, mockMetadata);
        property.deserializeAndSet(mockParser, mockContext, mockInstance);
        
        verify(mockDeserializer).deserialize(mockParser, mockContext);
        verify(mockContext).findObjectId(mockId, mockReader.generator, mockReader.resolver);
        verify(mockReadableObjectId).bindItem(mockInstance);
        verify(mockIdProperty).setAndReturn(mockInstance, mockId);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object mockInstance = mock(Object.class);
        Object mockValue = mock(Object.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockReader, mockMetadata);
        property.set(mockInstance, mockValue);
    }
    
    @Test
    public void testSetWithIdProperty() throws IOException {
        Object mockInstance = mock(Object.class);
        Object mockValue = mock(Object.class);
        SettableBeanProperty mockIdProperty = mock(SettableBeanProperty.class);
        
        when(mockReader.idProperty).thenReturn(mockIdProperty);
        when(mockIdProperty.setAndReturn(mockInstance, mockValue)).thenReturn(mockInstance);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockReader, mockMetadata);
        property.set(mockInstance, mockValue);
        
        verify(mockIdProperty).setAndReturn(mockInstance, mockValue);
    }
}