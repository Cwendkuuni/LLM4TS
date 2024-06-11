package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    
    @Mock
    private SettableBeanProperty forward;
    
    @Mock
    private ObjectIdInfo objectIdInfo;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private NullValueProvider nullValueProvider;
    
    @Mock
    private PropertyName propertyName;
    
    @Mock
    private DeserializationConfig config;
    
    @Mock
    private JsonParser parser;
    
    @Mock
    private DeserializationContext context;
    
    @Mock
    private Object instance;
    
    @Mock
    private UnresolvedForwardReference reference;
    
    @Mock
    private Class<?> type;
    
    @Mock
    private Object pojo;
    
    @Mock
    private Object id;
    
    @Mock
    private Object value;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property.getForward());
        assertEquals(objectIdInfo, property.getObjectIdInfo());
    }
    
    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = new ObjectIdReferenceProperty(property, deserializer, nullValueProvider);
        assertEquals(forward, property.getForward());
        assertEquals(objectIdInfo, property.getObjectIdInfo());
        assertEquals(deserializer, property.getValueDeserializer());
        assertEquals(nullValueProvider, property.getNullValueProvider());
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = new ObjectIdReferenceProperty(property, propertyName);
        assertEquals(forward, property.getForward());
        assertEquals(objectIdInfo, property.getObjectIdInfo());
        assertEquals(propertyName, property.getFullName());
    }
    
    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withName(propertyName);
        assertEquals(propertyName, property.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property = property.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, property.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(config);
        verify(forward, times(1)).fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Annotation annotation = mock(Annotation.class);
        when(forward.getAnnotation(any(Class.class))).thenReturn(annotation);
        
        assertEquals(annotation, property.getAnnotation(Annotation.class));
        verify(forward, times(1)).getAnnotation(Annotation.class);
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);
        
        assertEquals(member, property.getMember());
        verify(forward, times(1)).getMember();
    }
    
    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        when(forward.getCreatorIndex()).thenReturn(1);
        
        assertEquals(1, property.getCreatorIndex());
        verify(forward, times(1)).getCreatorIndex();
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(parser, context, instance);
        verify(forward, times(1)).deserializeAndSet(parser, context, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(parser, context, instance);
        verify(forward, times(1)).deserializeAndSet(parser, context, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.set(instance, value);
        verify(forward, times(1)).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.setAndReturn(instance, value);
        verify(forward, times(1)).setAndReturn(instance, value);
    }
    
    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = property.new PropertyReferring(property, reference, type, pojo);
        assertEquals(property, propertyReferring.getParent());
        assertEquals(pojo, propertyReferring.getPojo());
        
        propertyReferring.handleResolvedForwardReference(id, value);
        
        verify(property, times(1)).set(pojo, value);
    }
}