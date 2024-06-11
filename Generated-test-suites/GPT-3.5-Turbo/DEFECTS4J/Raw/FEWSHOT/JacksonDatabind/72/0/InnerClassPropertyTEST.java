package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;

public class InnerClassPropertyTest {
    
    private InnerClassProperty innerClassProperty;
    
    private SettableBeanProperty delegateMock;
    private Constructor<?> constructorMock;
    private AnnotatedConstructor annotatedConstructorMock;
    private JsonDeserializer<?> deserializerMock;
    private JsonToken jsonTokenMock;
    private DeserializationContext deserializationContextMock;
    
    @Before
    public void setUp() throws Exception {
        delegateMock = mock(SettableBeanProperty.class);
        constructorMock = mock(Constructor.class);
        annotatedConstructorMock = mock(AnnotatedConstructor.class);
        deserializerMock = mock(JsonDeserializer.class);
        jsonTokenMock = mock(JsonToken.class);
        deserializationContextMock = mock(DeserializationContext.class);
        
        innerClassProperty = new InnerClassProperty(delegateMock, constructorMock);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(delegateMock, innerClassProperty._delegate);
        assertEquals(constructorMock, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithAnnotated() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, annotatedConstructorMock);
        
        assertEquals(delegateMock, innerClassProperty._delegate);
        assertEquals(annotatedConstructorMock, innerClassProperty._annotated);
        assertEquals(constructorMock, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithDeserializer() {
        when(delegateMock.withValueDeserializer(deserializerMock)).thenReturn(delegateMock);
        
        innerClassProperty = new InnerClassProperty(innerClassProperty, deserializerMock);
        
        assertEquals(delegateMock, innerClassProperty._delegate);
        assertEquals(constructorMock, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        when(delegateMock.withName(any())).thenReturn(delegateMock);
        
        innerClassProperty = new InnerClassProperty(innerClassProperty, null);
        
        assertEquals(delegateMock, innerClassProperty._delegate);
        assertEquals(constructorMock, innerClassProperty._creator);
    }
    
    @Test
    public void testWithPropertyName() {
        when(delegateMock.withName(any())).thenReturn(delegateMock);
        
        innerClassProperty.withName(null);
        
        verify(delegateMock).withName(null);
    }
    
    @Test
    public void testWithValueDeserializer() {
        when(delegateMock.withValueDeserializer(deserializerMock)).thenReturn(delegateMock);
        
        innerClassProperty.withValueDeserializer(deserializerMock);
        
        verify(delegateMock).withValueDeserializer(deserializerMock);
    }
    
    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(0);
        
        verify(delegateMock).assignIndex(0);
    }
    
    @Test
    public void testGetPropertyIndex() {
        when(delegateMock.getPropertyIndex()).thenReturn(0);
        
        assertEquals(0, innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        Annotation annotationMock = mock(Annotation.class);
        when(delegateMock.getAnnotation(any())).thenReturn(annotationMock);
        
        Annotation result = innerClassProperty.getAnnotation(Annotation.class);
        
        assertEquals(annotationMock, result);
    }
    
    @Test
    public void testGetMember() {
        AnnotatedConstructor memberMock = mock(AnnotatedConstructor.class);
        when(delegateMock.getMember()).thenReturn(memberMock);
        
        assertEquals(memberMock, innerClassProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        when(delegateMock.getNullValue(deserializationContextMock)).thenReturn(null);
        when(jsonTokenMock == JsonToken.VALUE_NULL);
        
        innerClassProperty.deserializeAndSet(mock(JsonParser.class), deserializationContextMock, new Object());
        
        verify(delegateMock).set(any(), any());
    }
    
    @Test
    public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
        when(jsonTokenMock == JsonToken.VALUE_NULL).thenReturn(false);
        when(delegateMock.getNullValue(deserializationContextMock)).thenReturn(null);
        when(deserializerMock.deserializeWithType(any(), any(), any())).thenReturn(null);
        
        innerClassProperty.deserializeAndSet(mock(JsonParser.class), deserializationContextMock, new Object());
        
        verify(delegateMock).set(any(), any());
    }
    
    @Test
    public void testDeserializeAndSetWithCreatorException() throws IOException {
        when(jsonTokenMock == JsonToken.VALUE_NULL).thenReturn(false);
        when(delegateMock.getNullValue(deserializationContextMock)).thenReturn(null);
        when(constructorMock.newInstance(any())).thenThrow(new Exception());
        
        innerClassProperty.deserializeAndSet(mock(JsonParser.class), deserializationContextMock, new Object());
        
        verify(delegateMock).set(any(), any());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonTokenMock == JsonToken.VALUE_NULL).thenReturn(false);
        when(delegateMock.getNullValue(deserializationContextMock)).thenReturn(null);
        
        innerClassProperty.deserializeAndSet(mock(JsonParser.class), deserializationContextMock, new Object());
        
        verify(delegateMock).set(any(), any());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object valueMock = new Object();
        when(delegateMock.deserialize(any(), any())).thenReturn(valueMock);
        
        Object result = innerClassProperty.deserializeSetAndReturn(mock(JsonParser.class), deserializationContextMock, new Object());
        
        assertEquals(valueMock, result);
    }
    
    @Test
    public void testSet() throws IOException {
        innerClassProperty.set(new Object(), new Object());
        
        verify(delegateMock).set(any(), any());
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object valueMock = new Object();
        when(delegateMock.setAndReturn(any(), any())).thenReturn(valueMock);
        
        Object result = innerClassProperty.setAndReturn(new Object(), new Object());
        
        assertEquals(valueMock, result);
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty result = (InnerClassProperty) innerClassProperty.readResolve();
        
        assertEquals(innerClassProperty, result);
    }
    
    @Test
    public void testWriteReplaceWhenAnnotatedIsNull() {
        when(delegateMock.withName(any())).thenReturn(delegateMock);
        when(constructorMock.getDeclaringClass().getName()).thenReturn("com.test.Class");
        
        InnerClassProperty result = (InnerClassProperty) innerClassProperty.writeReplace();
        
        assertEquals(innerClassProperty, result);
    }
    
    @Test
    public void testWriteReplaceWhenAnnotatedIsNotNull() {
        InnerClassProperty result = (InnerClassProperty) innerClassProperty.writeReplace();
        
        assertEquals(innerClassProperty, result);
    }
    
}