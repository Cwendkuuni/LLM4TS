package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader, objIdValueProperty._objectIdReader);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objIdValueProperty.setValueDeserializer(valueDeserializer);
        
        objIdValueProperty.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objIdValueProperty.setValueDeserializer(valueDeserializer);
        
        objIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);        
        objIdValueProperty.setAndReturn(instance, value);
    }
    
    @Test
    public void testWithMethods() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        PropertyName newName = mock(PropertyName.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objIdValueProperty, objIdValueProperty.withName(newName));
        assertEquals(objIdValueProperty, objIdValueProperty.withValueDeserializer(deser));
    }
    
    @Test
    public void testGetMethods() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(objIdValueProperty.getAnnotation(AnnotatedMember.class));
        assertNull(objIdValueProperty.getMember());
    }
}