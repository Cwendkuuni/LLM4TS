package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ReadableObjectId;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property._valueDeserializer);
    }
    
    @Test
    public void testDeprecatedCopyConstructor1() {
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testDeprecatedCopyConstructor2() {
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        String newName = "newName";
        PropertyName expectedName = new PropertyName(newName);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(expectedName, property._propName);
    }
    
    @Test
    public void testWithName() {
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        
        ObjectIdValueProperty property = src.withName(newName);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property._valueDeserializer);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        assertNull(property.getAnnotation(java.lang.Override.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.deserializeAndSet(jp, ctxt, instance);
        
        // Verify that findObjectId and bindItem were called
        Mockito.verify(ctxt).findObjectId(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(Mockito.mock(ReadableObjectId.class)).bindItem(instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.deserializeSetAndReturn(jp, ctxt, instance);
        
        // Verify that findObjectId and bindItem were called
        Mockito.verify(ctxt).findObjectId(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(Mockito.mock(ReadableObjectId.class)).bindItem(instance);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.set(instance, value);
        
        // Verify that setAndReturn was called
        Mockito.verify(property._objectIdReader.idProperty).setAndReturn(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(instance, value);
        
        // Verify that setAndReturn was called
        Mockito.verify(property._objectIdReader.idProperty).setAndReturn(instance, value);
    }
}