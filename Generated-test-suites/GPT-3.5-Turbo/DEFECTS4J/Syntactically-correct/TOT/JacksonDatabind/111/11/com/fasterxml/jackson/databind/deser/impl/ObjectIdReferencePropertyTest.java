
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    
    @Mock
    private SettableBeanProperty forward;
    
    @Mock
    private ObjectIdInfo objectIdInfo;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor1() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor2() {
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        
        ObjectIdReferenceProperty src = Mockito.spy(new ObjectIdReferenceProperty(forward, objectIdInfo));
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor3() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        
        ObjectIdReferenceProperty src = Mockito.spy(new ObjectIdReferenceProperty(forward, objectIdInfo));
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
        
        assertEquals(newName, property.getFullName());
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        
        ObjectIdReferenceProperty src = Mockito.spy(new ObjectIdReferenceProperty(forward, objectIdInfo));
        
        ObjectIdReferenceProperty property = src.withNullProvider(nva);
        
        assertEquals(nva, property._nullProvider);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.fixAccess(config);
        
        Mockito.verify(forward).fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        Class<A> acls = Mockito.mock(Class.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.getAnnotation(acls);
        
        Mockito.verify(forward).getAnnotation(acls);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.getMember();
        
        Mockito.verify(forward).getMember();
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.getCreatorIndex();
        
        Mockito.verify(forward).getCreatorIndex();
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        Mockito.verify(forward).deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        Mockito.verify(forward).deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.set(instance, value);
        
        Mockito.verify(forward).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        property.setAndReturn(instance, value);
        
        Mockito.verify(forward).setAndReturn(instance, value);
    }
    
    @Test
    public void testPropertyReferring() throws IOException {
        UnresolvedForwardReference ref = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = Mockito.mock(Class.class);
        Object ob = Mockito.mock(Object.class);
        
        ObjectIdReferenceProperty parent = Mockito.mock(ObjectIdReferenceProperty.class);
        
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = 
                new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        
        assertEquals(parent, propertyReferring._parent);
        assertEquals(ob, propertyReferring._pojo);
        
        Object id = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        
        propertyReferring.handleResolvedForwardReference(id, value);
        
        Mockito.verify(parent).set(ob, value);
        
        propertyReferring.handleResolvedForwardReference(id, value);
        
        Mockito.verify(parent, Mockito.times(2)).set(ob, value);
    }

}
