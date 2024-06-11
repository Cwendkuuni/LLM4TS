package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor_ObjectIdReader_PropertyMetadata() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.getPropertyName(), property.getPropertyName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }
    
    @Test
    public void testConstructor_ObjectIdValueProperty_JsonDeserializer_NullValueProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        
        assertEquals(src.getName(), property.getName());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testConstructor_ObjectIdValueProperty_PropertyName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testWithName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        
        ObjectIdValueProperty property = src.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        assertEquals(src, property);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        NullValueProvider nva = new NullValueProvider();
        
        ObjectIdValueProperty property = src.withNullProvider(nva);
        
        assertEquals(src, property);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        
        assertNull(property.getAnnotation(Object.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet_JsonParser_DeserializationContext_Object() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        property.deserializeAndSet(p, ctxt, instance);
        
        // Add assertions here
    }
    
    @Test
    public void testDeserializeSetAndReturn_JsonParser_DeserializationContext_Object() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Add assertions here
    }
    
    @Test
    public void testSet_Object_Object() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        
        property.set(instance, null);
        
        // Add assertions here
    }
    
    @Test
    public void testSetAndReturn_Object_Object() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        
        Object result = property.setAndReturn(instance, null);
        
        // Add assertions here
    }
}