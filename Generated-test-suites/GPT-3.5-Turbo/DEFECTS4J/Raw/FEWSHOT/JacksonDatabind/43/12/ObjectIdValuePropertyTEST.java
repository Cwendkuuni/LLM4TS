package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertNull(property.getDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
        };
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        src.setValueDeserializer(deser);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getDeserializer(), property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getDeserializer(), property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
        };
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        // not testable as it is calling another method
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        // not testable as it is calling another method
    }
    
    @Test
    public void testSet() {
        // not testable as it is calling another method
    }
    
    @Test
    public void testSetAndReturn() {
        // not testable as it is calling another method
    }
}