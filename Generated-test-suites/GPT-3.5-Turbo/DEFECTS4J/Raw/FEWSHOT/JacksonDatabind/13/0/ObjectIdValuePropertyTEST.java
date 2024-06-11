package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdValuePropertyTest {
    
    @Mock
    private ObjectIdReader objectIdReaderMock;
    
    @Mock
    private PropertyMetadata metadataMock;
    
    @Mock
    private JsonDeserializer<?> deserializerMock;

    private ObjectIdValueProperty objectIdValueProperty;
    
    @Before
    public void setUp() {
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(objectIdReaderMock.propertyName, objectIdValueProperty.f());
        assertEquals(objectIdReaderMock.getIdType(), objectIdValueProperty.i());
        assertEquals(metadataMock, objectIdValueProperty.l());
        assertEquals(objectIdReaderMock.getDeserializer(), objectIdValueProperty.j());
    }
    
    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        when(src._objectIdReader).thenReturn(objectIdReaderMock);
        
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(src, deserializerMock);
        
        assertEquals(src, newObjectIdValueProperty.g());
        assertEquals(deserializerMock, newObjectIdValueProperty.j());
        assertEquals(objectIdReaderMock, newObjectIdValueProperty._objectIdReader);
    }
    
    @Test
    @Deprecated
    public void testConstructorWithPropertyName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        when(src._objectIdReader).thenReturn(objectIdReaderMock);
        
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(src, new PropertyName("newName"));
        
        assertEquals(src, newObjectIdValueProperty.g());
        assertEquals(objectIdReaderMock, newObjectIdValueProperty._objectIdReader);
    }
    
    @Test
    @Deprecated
    public void testConstructorWithStringName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        when(src._objectIdReader).thenReturn(objectIdReaderMock);
        
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(src, "newName");
        
        assertEquals(src, newObjectIdValueProperty.g());
        assertEquals(objectIdReaderMock, newObjectIdValueProperty._objectIdReader);
    }
    
    @Test
    public void testName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        when(src._objectIdReader).thenReturn(objectIdReaderMock);
        when(src.a()).thenReturn(new PropertyName("name"));
        
        assertEquals(new PropertyName("name"), src.a());
    }
    
    @Test
    public void testValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        when(src._objectIdReader).thenReturn(objectIdReaderMock);
        when(src.h()).thenReturn(deserializerMock);
        
        assertEquals(deserializerMock, src.h());
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(null));
        
        Class<Deprecated> deprecatedClass = Deprecated.class;
        ObjectIdValueProperty spy = spy(objectIdValueProperty);
        doReturn(deprecatedClass).when(spy).getAnnotation(null);
        
        assertEquals(deprecatedClass, spy.getAnnotation(null));
        verify(spy).getAnnotation(null);
    }
    
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
        
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        ObjectIdValueProperty spy = spy(objectIdValueProperty);
        doReturn(annotatedMember).when(spy).getMember();
        
        assertEquals(annotatedMember, spy.getMember());
        verify(spy).getMember();
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
        
        verify(objectIdValueProperty).deserializeSetAndReturn(jp, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
        
        verify(objectIdValueProperty).deserializeSetAndReturn(jp, ctxt, instance);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        objectIdValueProperty.set(instance, value);
        
        verify(objectIdValueProperty).setAndReturn(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        objectIdValueProperty.setAndReturn(instance, value);
        
        verify(objectIdValueProperty).setAndReturn(instance, value);
    }
}