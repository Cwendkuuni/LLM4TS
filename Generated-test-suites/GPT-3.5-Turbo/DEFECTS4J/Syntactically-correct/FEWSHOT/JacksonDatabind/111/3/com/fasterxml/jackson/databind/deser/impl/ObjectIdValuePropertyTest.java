package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getReader());
    }
    
    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer<>(), new NullValueProvider());
        
        assertNull(src.getPropertyName());
        assertEquals(JsonDeserializer.class, src.getDeserializer().getClass());
        assertEquals(NullValueProvider.class, src.getNullValueProvider().getClass());
    }
    
    @Test
    public void testConstructorWithNewName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer<>(), new NullValueProvider()), new PropertyName("newName"));
        
        assertEquals("newName", src.getName());
        assertEquals(JsonDeserializer.class, src.getDeserializer().getClass());
        assertEquals(NullValueProvider.class, src.getNullValueProvider().getClass());
    }
    
    @Test
    public void testWithName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName("name"));
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withName(new PropertyName("newName"));
        
        assertEquals("newName", property.getName());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src.getNullValueProvider(), property.getNullValueProvider());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName("name"));
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(new JsonDeserializer<>());
        
        assertEquals(src.getPropertyName(), property.getPropertyName());
        assertNotEquals(src.getDeserializer(), property.getDeserializer());
        assertEquals(src.getNullValueProvider(), property.getNullValueProvider());
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName("name"));
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withNullProvider(new NullValueProvider());
        
        assertEquals(src.getPropertyName(), property.getPropertyName());
        assertEquals(src.getDeserializer(), property.getDeserializer());
        assertNotEquals(src.getNullValueProvider(), property.getNullValueProvider());
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        assertNull(property.getAnnotation(Object.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        // TODO: Add test cases for deserializeAndSet()
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        // TODO: Add test cases for deserializeSetAndReturn()
    }
    
    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        // TODO: Add test cases for set()
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        // TODO: Add test cases for setAndReturn()
    }
}