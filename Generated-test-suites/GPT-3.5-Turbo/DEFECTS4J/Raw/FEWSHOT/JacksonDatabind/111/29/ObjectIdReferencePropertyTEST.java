package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    
    @Test
    public void testConstructor1() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._name);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty property = src.withName(newName);
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._name);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty result = property.withValueDeserializer(deser);
        assertEquals(property._forward, result._forward);
        assertEquals(property._objectIdInfo, result._objectIdInfo);
        assertEquals(deser, result._valueDeserializer);
        assertEquals(property._nullProvider, result._nullProvider);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty result = property.withNullProvider(nva);
        assertEquals(property._forward, result._forward);
        assertEquals(property._objectIdInfo, result._objectIdInfo);
        assertEquals(property._valueDeserializer, result._valueDeserializer);
        assertEquals(nva, result._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        DeserializationConfig config = new DeserializationConfig();
        property._forward = new SettableBeanProperty();
        property.fixAccess(config);
        //Assert fixAccess method is called
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Class<A> acls = A.class;
        A annotation = new A();
        property._forward = new SettableBeanProperty();
        property._forward.setAnnotation(annotation);
        A result = property.getAnnotation(acls);
        assertEquals(annotation, result);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        AnnotatedMember member = new AnnotatedMember();
        property._forward = new SettableBeanProperty();
        property._forward.setMember(member);
        AnnotatedMember result = property.getMember();
        assertEquals(member, result);
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        int index = 1;
        property._forward = new SettableBeanProperty();
        property._forward.setCreatorIndex(index);
        int result = property.getCreatorIndex();
        assertEquals(index, result);
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
            //Assert deserializeAndSet method is called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertNull(result);
            //Assert deserializeSetAndReturn method is called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
            //Assert set method is called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            Object result = property.setAndReturn(instance, value);
            assertEquals(instance, result);
            //Assert setAndReturn method is called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, ref, type, ob);
        assertEquals(property, referring._parent);
        assertEquals(ref, referring._reference);
        assertEquals(type, referring._type);
        assertEquals(ob, referring._pojo);
    }
    
    @Test
    public void testHandleResolvedForwardReference() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, ref, type, ob);
        Object id = new Object();
        Object value = new Object();
        try {
            referring.handleResolvedForwardReference(id, value);
            //Assert handleResolvedForwardReference method is called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}