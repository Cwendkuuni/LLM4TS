package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    @Test
    public void testWithSetName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals(new PropertyName("newName"), newProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(null);
        assertNull(newProperty.getValueDeserializer());
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.deserializeAndSet(null, null, new Object());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.deserializeSetAndReturn(null, null, new Object()));
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.set(null, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(null, null);
    }
}