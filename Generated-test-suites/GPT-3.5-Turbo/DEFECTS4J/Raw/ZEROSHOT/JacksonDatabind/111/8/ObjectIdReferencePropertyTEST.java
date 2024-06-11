package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithName() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);
        
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty result = property.withValueDeserializer(deser);
        
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = property.withNullProvider(nva);
        
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.fixAccess(new DeserializationConfig());
        assertTrue(true);
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        Class<A> acls = null;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertNotNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
            assertTrue(true);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            assertTrue(true);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSet() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
            assertTrue(true);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
            assertTrue(true);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = new Class<?>();
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                parent, ref, type, ob);

        assertEquals(parent, referring._parent);
        assertEquals(ob, referring._pojo);
    }

    @Test
    public void testHandleResolvedForwardReference() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = new Class<?>();
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                parent, ref, type, ob);

        Object id = new Object();
        Object value = new Object();
        try {
            referring.handleResolvedForwardReference(id, value);
            assertTrue(true);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}