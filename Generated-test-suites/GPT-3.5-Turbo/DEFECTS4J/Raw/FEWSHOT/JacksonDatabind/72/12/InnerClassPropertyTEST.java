package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InnerClassPropertyTest {
    
    @Mock
    private SettableBeanProperty delegate;
    
    @Mock
    private Constructor<?> creator;
    
    @Mock
    private PropertyName newName;
    
    @Mock
    private JsonDeserializer<?> deser;
    
    @Mock
    private JsonParser jp;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private Object bean;
    
    @Mock
    private Object value;
    
    @Mock
    private AnnotatedMember member;
    
    @Test
    public void testConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithNewName() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(delegate.withName(newName)).thenReturn(delegate);
        
        InnerClassProperty result = innerClassProperty.withName(newName);
        
        assertEquals(delegate, result._delegate);
        assertEquals(creator, result._creator);
        verify(delegate).withName(newName);
    }
    
    @Test
    public void testWithValueDeserializer() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(delegate.withValueDeserializer(deser)).thenReturn(delegate);
        
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        
        assertEquals(delegate, result._delegate);
        assertEquals(creator, result._creator);
        verify(delegate).withValueDeserializer(deser);
    }
    
    @Test
    public void testAssignIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        doNothing().when(delegate).assignIndex(anyInt());
        
        innerClassProperty.assignIndex(0);
        
        verify(delegate).assignIndex(0);
    }
    
    @Test
    public void testGetPropertyIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(delegate.getPropertyIndex()).thenReturn(0);
        
        int result = innerClassProperty.getPropertyIndex();
        
        assertEquals(0, result);
        verify(delegate).getPropertyIndex();
    }
    
    @Test
    public void testGetAnnotation() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        Class<A> acls = A.class;
        when(delegate.getAnnotation(acls)).thenReturn(any(A.class));
        
        A result = innerClassProperty.getAnnotation(acls);
        
        verify(delegate).getAnnotation(acls);
    }
    
    @Test
    public void testGetMember() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(delegate.getMember()).thenReturn(member);
        
        AnnotatedMember result = innerClassProperty.getMember();
        
        assertEquals(member, result);
        verify(delegate).getMember();
    }
    
    @Test
    public void testDeserializeAndSet_NullToken() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(_valueDeserializer.getNullValue(ctxt)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        verify(_valueDeserializer).getNullValue(ctxt);
        verify(delegate).set(bean, value);
    }
    
    @Test
    public void testDeserializeAndSet_WithValueTypeDeserializer() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_valueTypeDeserializer.deserializeWithType(jp, ctxt, _valueTypeDeserializer)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        verify(_valueTypeDeserializer).deserializeWithType(jp, ctxt, _valueTypeDeserializer);
        verify(delegate).set(bean, value);
    }
    
    @Test
    public void testDeserializeAndSet_DefaultCase() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_creator.newInstance(bean)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        verify(delegate).set(bean, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_creator.newInstance(bean)).thenReturn(value);
        
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, bean);
        
        assertEquals(value, result);
        verify(delegate).set(bean, value);
    }
    
    @Test
    public void testSet() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        
        innerClassProperty.set(bean, value);
        
        verify(delegate).set(bean, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        when(delegate.setAndReturn(bean, value)).thenReturn(value);
        
        Object result = innerClassProperty.setAndReturn(bean, value);
        
        assertEquals(value, result);
        verify(delegate).setAndReturn(bean, value);
    }
    
    @Test
    public void testReadResolve() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        InnerClassProperty src = new InnerClassProperty(delegate, creator);
        AnnotatedConstructor ann = null;
        
        InnerClassProperty result = innerClassProperty.readResolve();
        
        assertEquals(innerClassProperty, result);
        verify(innerClassProperty).readResolve();
    }
    
    @Test
    public void testWriteReplace() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        InnerClassProperty src = new InnerClassProperty(delegate, creator);
        
        Object result = innerClassProperty.writeReplace();
        
        verify(innerClassProperty).writeReplace();
    }

    public class A {}
}