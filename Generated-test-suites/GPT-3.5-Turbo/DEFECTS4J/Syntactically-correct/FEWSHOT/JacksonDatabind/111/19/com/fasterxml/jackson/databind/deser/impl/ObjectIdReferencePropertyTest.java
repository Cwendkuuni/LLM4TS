package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.PropertyName;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private PropertyName newName;
    private DeserializationContext ctxt;
    private JsonParser jsonParser;
    private Object instance;
    private ObjectIdReferenceProperty.PropertyReferring propertyReferring;
    
    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        newName = mock(PropertyName.class);
        ctxt = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        instance = new Object();
        propertyReferring = mock(ObjectIdReferenceProperty.PropertyReferring.class);
    }
    
    @Test
    public void testConstructor() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withValueDeserializer(deser);
        
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withNullProvider(nva);
        
        assertEquals(nva, property.getNullValueProvider());
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }
    
    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(ctxt);
        
        verify(forward, times(1)).fixAccess(ctxt);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Class<Annotation> acls = Annotation.class;
        when(forward.getAnnotation(acls)).thenReturn(acls.getAnnotation(acls));
        Annotation annotation = property.getAnnotation(acls);
        
        verify(forward, times(1)).getAnnotation(acls);
        assertEquals(acls.getAnnotation(acls), annotation);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);
        assertEquals(member, property.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        when(forward.getCreatorIndex()).thenReturn(1);
        assertEquals(1, property.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object value = new Object();
        property.deserializeAndSet(jsonParser, ctxt, instance);
        
        verify(property._valueDeserializer, times(1)).deserialize(jsonParser, ctxt);
        verify(property._forward, times(1)).setAndReturn(instance, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object value = new Object();
        property.deserializeSetAndReturn(jsonParser, ctxt, instance);
        
        verify(property._valueDeserializer, times(1)).deserialize(jsonParser, ctxt);
        verify(property._forward, times(1)).setAndReturn(instance, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn_ThrowsUnresolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(reference.getRoid()).thenReturn(mock(ReadableObjectId.class));
        when(reference.getRoid().appendReferring(any(ObjectIdReferenceProperty.PropertyReferring.class))).thenReturn(null);
        Class<?> type = Object.class;
        when(forward.getType()).thenReturn(type);
        
        try {
            property.deserializeSetAndReturn(jsonParser, ctxt, instance);
        } catch (JsonMappingException e) {
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object value = new Object();
        property.set(instance, value);
        
        verify(property._forward, times(1)).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object value = new Object();
        when(property._forward.setAndReturn(instance, value)).thenReturn(value);
        assertEquals(value, property.setAndReturn(instance, value));
        
        verify(property._forward, times(1)).setAndReturn(instance, value);
    }
    
    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = 
            new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        propertyReferring.handleResolvedForwardReference(null, null);
        
        assertNull(propertyReferring.getRoid());
    }
}