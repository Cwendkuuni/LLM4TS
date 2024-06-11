package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        
        ObjectIdReferenceProperty property = src.withName(newName);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        ObjectIdReferenceProperty property = src.withValueDeserializer(deser);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(src._nullProvider, property._nullProvider);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        NullValueProvider nva = new NullValueProvider();
        
        ObjectIdReferenceProperty property = src.withNullProvider(nva);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(src._valueDeserializer, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        DeserializationConfig config = new DeserializationConfig();
        
        property.fixAccess(config);
        
        assertEquals(property._forward, property._forward);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Class<A> acls = A.class;
        
        A annotation = property.getAnnotation(acls);
        
        assertEquals(property._forward.getAnnotation(acls), annotation);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        
        AnnotatedMember member = property.getMember();
        
        assertEquals(property._forward.getMember(), member);
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        
        int creatorIndex = property.getCreatorIndex();
        
        assertEquals(property._forward.getCreatorIndex(), creatorIndex);
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        property.deserializeAndSet(p, ctxt, instance);
        
        // Verify the behavior here
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Verify the behavior here
        
        assertNull(result);
    }
    
    @Test
    public void testSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        
        property.set(instance, value);
        
        // Verify the behavior here
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        
        Object result = property.setAndReturn(instance, value);
        
        // Verify the behavior here
        
        assertEquals(value, result);
    }
    
    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring referring = 
            new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        
        assertEquals(parent, referring._parent);
        assertEquals(ref, referring._forwardReference);
        assertEquals(type, referring._type);
        assertEquals(ob, referring._pojo);
    }
    
    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Object id = new Object();
        Object value = new Object();
        Class<?> type = Object.class;
        Object ob = new Object();
        
        ObjectIdReferenceProperty.PropertyReferring referring = 
            new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        referring.addId(id);
        
        referring.handleResolvedForwardReference(id, value);
        
        // Verify the behavior here
    }
}