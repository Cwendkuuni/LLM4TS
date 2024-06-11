package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        NullValueProvider nva = null;
        ObjectIdReferenceProperty result = property.withNullProvider(nva);
        assertNull(result.getValueDeserializer());
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        DeserializationConfig config = null;
        property.fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Class<A> acls = null;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        assertThrows(IOException.class, () -> {
            property.deserializeAndSet(p, ctxt, instance);
        });
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        assertThrows(IOException.class, () -> {
            property.deserializeSetAndReturn(p, ctxt, instance);
        });
    }

    @Test
    public void testSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Object instance = null;
        Object value = null;
        assertThrows(IOException.class, () -> {
            property.set(instance, value);
        });
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Object instance = null;
        Object value = null;
        assertThrows(IOException.class, () -> {
            property.setAndReturn(instance, value);
        });
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = property.new PropertyReferring(property, null, null,null);
        Object id = null;
        Object value = null;
        assertThrows(IllegalArgumentException.class, () -> {
            propertyReferring.handleResolvedForwardReference(id, value);
        });
    }
}