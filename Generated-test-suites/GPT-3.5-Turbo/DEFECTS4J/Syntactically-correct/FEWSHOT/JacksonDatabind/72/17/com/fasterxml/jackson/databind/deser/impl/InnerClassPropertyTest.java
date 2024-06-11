package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {
    
    @Mock
    private SettableBeanProperty delegate;
    
    @Mock
    private Constructor<?> constructor;
    
    @Mock
    private AnnotatedConstructor annotated;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private PropertyName name;
    
    @Mock
    private JsonParser parser;
    
    @Mock
    private DeserializationContext context;
    
    private InnerClassProperty innerClassProperty;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(innerClassProperty._delegate, delegate);
        assertEquals(innerClassProperty._creator, constructor);
    }
    
    @Test
    public void testConstructorWithAnnotated() {
        when(annotated.getAnnotated()).thenReturn(constructor);
        InnerClassProperty innerClassProperty = new InnerClassProperty(this.innerClassProperty, annotated);
        assertEquals(innerClassProperty._delegate, delegate);
        assertEquals(innerClassProperty._annotated, annotated);
        assertEquals(innerClassProperty._creator, constructor);
    }
    
    @Test
    public void testConstructorWithDeserializer() {
        when(delegate.withValueDeserializer(deserializer)).thenReturn(delegate);
        InnerClassProperty innerClassProperty = new InnerClassProperty(this.innerClassProperty, deserializer);
        assertEquals(innerClassProperty._delegate, delegate);
        assertEquals(innerClassProperty._creator, constructor);
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        when(delegate.withName(name)).thenReturn(delegate);
        InnerClassProperty innerClassProperty = new InnerClassProperty(this.innerClassProperty, name);
        assertEquals(innerClassProperty._delegate, delegate);
        assertEquals(innerClassProperty._creator, constructor);
    }
    
    @Test
    public void testWithPropertyName() {
        when(delegate.withName(name)).thenReturn(delegate);
        assertEquals(innerClassProperty.withName(name)._delegate, delegate);
    }
    
    @Test
    public void testWithValueDeserializer() {
        when(delegate.withValueDeserializer(deserializer)).thenReturn(delegate);
        assertEquals(innerClassProperty.withValueDeserializer(deserializer)._delegate, delegate);
    }
    
    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(1);
        verify(delegate).assignIndex(1);
    }
    
    @Test
    public void testGetPropertyIndex() {
        when(delegate.getPropertyIndex()).thenReturn(1);
        assertEquals(innerClassProperty.getPropertyIndex(), 1);
    }
    
    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(delegate.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(innerClassProperty.getAnnotation(Annotation.class), annotation);
    }
    
    @Test
    public void testGetMember() {
        AnnotatedConstructor member = mock(AnnotatedConstructor.class);
        when(delegate.getMember()).thenReturn(member);
        assertEquals(innerClassProperty.getMember(), member);
    }
    
    @Test(expected=IOException.class)
    public void testDeserializeAndSet_NullToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializer.getNullValue(context)).thenReturn(null);
        innerClassProperty.deserializeAndSet(parser, context, new Object());
    }
    
    @Test(expected=IOException.class)
    public void testDeserializeAndSet_WithTypeDeser_NullToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(innerClassProperty._valueTypeDeserializer).thenReturn(deserializer);
        innerClassProperty.deserializeAndSet(parser, context, new Object());
    }
    
    @Test
    public void testDeserializeAndSet_CreatorException() throws Exception {
        when(innerClassProperty._creator.newInstance(any())).thenThrow(new Exception());
        ClassUtil.unwrapAndThrowAsIAE(new Exception(), "Failed to instantiate class "+innerClassProperty._creator.getDeclaringClass().getName()+", problem: "+new Exception().getMessage());
        innerClassProperty.deserializeAndSet(parser, context, new Object());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        innerClassProperty.deserializeAndSet(parser, context, new Object());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        innerClassProperty.deserializeSetAndReturn(parser, context, new Object());
    }
    
    @Test
    public void testSet() throws IOException {
        innerClassProperty.set(new Object(), new Object());
        verify(delegate).set(any(Object.class), any(Object.class));
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        innerClassProperty.setAndReturn(new Object(), new Object());
        verify(delegate).setAndReturn(any(Object.class), any(Object.class));
    }
    
    @Test
    public void testReadResolve() {
        innerClassProperty.readResolve();
    }
    
    @Test
    public void testWriteReplace() {
        innerClassProperty.writeReplace();
    }
    
}