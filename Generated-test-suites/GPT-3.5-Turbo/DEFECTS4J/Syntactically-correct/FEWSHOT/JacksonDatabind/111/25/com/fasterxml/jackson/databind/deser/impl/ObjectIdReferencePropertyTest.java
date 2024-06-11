package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectIdReferencePropertyTest {
    
    @Test
    public void testConstructor1() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor2() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        
        ObjectIdReferenceProperty src = Mockito.mock(ObjectIdReferenceProperty.class);
        when(src._forward).thenReturn(forward);
        when(src._objectIdInfo).thenReturn(new ObjectIdInfo());
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        
        assertEquals(forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testConstructor3() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        PropertyName newName = new PropertyName();
        
        ObjectIdReferenceProperty src = Mockito.mock(ObjectIdReferenceProperty.class);
        when(src._forward).thenReturn(forward);
        when(src._objectIdInfo).thenReturn(new ObjectIdInfo());
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        
        assertEquals(forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = Mockito.mock(ObjectIdReferenceProperty.class);
        when(src._forward).thenReturn(Mockito.mock(SettableBeanProperty.class));
        when(src._objectIdInfo).thenReturn(new ObjectIdInfo());
        
        PropertyName newName = new PropertyName();
        SettableBeanProperty property = src.withName(newName);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty src = Mockito.mock(ObjectIdReferenceProperty.class);
        when(src._forward).thenReturn(Mockito.mock(SettableBeanProperty.class));
        when(src._objectIdInfo).thenReturn(new ObjectIdInfo());
        
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        SettableBeanProperty property = src.withValueDeserializer(deser);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty src = Mockito.mock(ObjectIdReferenceProperty.class);
        when(src._forward).thenReturn(Mockito.mock(SettableBeanProperty.class));
        when(src._objectIdInfo).thenReturn(new ObjectIdInfo());
        
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty property = src.withNullProvider(nva);
        
        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.fixAccess(config);
        
        verify(forward, times(1)).fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        Class<A> acls = Mockito.mock(Class.class);
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.getAnnotation(acls);
        
        verify(forward, times(1)).getAnnotation(acls);
    }
    
    @Test
    public void testGetMember() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.getMember();
        
        verify(forward, times(1)).getMember();
    }
    
    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.getCreatorIndex();
        
        verify(forward, times(1)).getCreatorIndex();
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(forward, times(1)).deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(forward, times(1)).deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        Object instance = new Object();
        Object value = new Object();
        
        property.set(instance, value);
        
        verify(forward, times(1)).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, new ObjectIdInfo());
        property._forward = forward;
        
        Object instance = new Object();
        Object value = new Object();
        
        property.setAndReturn(instance, value);
        
        verify(forward, times(1)).setAndReturn(instance, value);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(Mockito.mock(SettableBeanProperty.class), new ObjectIdInfo());
        
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = Mockito.mock(Class.class);
        Object ob = new Object();
        
        property.new PropertyReferring(property, reference, type, ob);
    }
    
    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(Mockito.mock(SettableBeanProperty.class), new ObjectIdInfo());
        
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property,
                Mockito.mock(UnresolvedForwardReference.class),
                Object.class, new Object());
        
        referring.handleResolvedForwardReference(new Object(), new Object());
    }
}