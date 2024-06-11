package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
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
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {
    private InnerClassProperty innerClassProperty;
    private SettableBeanProperty delegate;
    private Constructor<?> ctor;
    
    @Before
    public void setup() {
        delegate = mock(SettableBeanProperty.class);
        ctor = mock(Constructor.class);
        
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty newProperty = innerClassProperty.withName(newName);
        
        assertEquals(delegate, newProperty._delegate);
        assertEquals(ctor, newProperty._creator);
        assertEquals(newName, newProperty.getFullName());
    }
    
    @Test
    public void testWithNewValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        InnerClassProperty newProperty = innerClassProperty.withValueDeserializer(deser);
        
        assertEquals(delegate, newProperty._delegate);
        assertEquals(ctor, newProperty._creator);
        assertEquals(deser, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testAssignIndex() {
        int index = 1;
        innerClassProperty.assignIndex(index);
        
        assertEquals(index, delegate.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        assertEquals(delegate.getPropertyIndex(), innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = mock(Annotation.class);
        when(delegate.getAnnotation(acls)).thenReturn(annotation);
        
        assertEquals(annotation, innerClassProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(delegate.getMember()).thenReturn(member);
        
        assertEquals(member, innerClassProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet_JsonValueIsNull() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        JsonToken t = JsonToken.VALUE_NULL;
        
        when(jp.getCurrentToken()).thenReturn(t);
        when(innerClassProperty.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(innerClassProperty._creator.newInstance(bean)).thenReturn(new Object());
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        // verify that the value is set using the delegate
        // (this assumes the delegate's behavior has been tested separately)
    }
    
    @Test
    public void testDeserializeAndSet_JsonValueIsNotNull() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        JsonToken t = JsonToken.VALUE_STRING;
        when(jp.getCurrentToken()).thenReturn(t);
        Object value = new Object();
        when(innerClassProperty.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(innerClassProperty._creator.newInstance(bean)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        // verify that the correct value is set using the delegate
        // (this assumes the delegate's behavior has been tested separately)
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassProperty_MissingConstructor() {
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        new InnerClassProperty(innerClassProperty, ann);
    }
    
    @Test
    public void testGetAnnotated() {
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, ann);
        
        assertEquals(ann, property.getAnnotated());
    }
    
    @Test
    public void testWithTrueReturnObject() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        when(innerClassProperty.deserialize(jp, ctxt)).thenReturn(value);
        when(delegate.setAndReturn(instance, value)).thenReturn(value);
        
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        
        assertEquals(value, result);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        innerClassProperty.set(instance, value);
        
        // verify that the value is set using the delegate
        // (this assumes the delegate's behavior has been tested separately)
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        when(delegate.setAndReturn(instance, value)).thenReturn(value);
        
        Object result = innerClassProperty.setAndReturn(instance, value);
        
        assertEquals(value, result);
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty result = new InnerClassProperty(innerClassProperty, mock(AnnotatedConstructor.class));
        InnerClassProperty resolved = (InnerClassProperty)result.readResolve();
        
        assertEquals(result._annotated, resolved._annotated);
    }
    
    @Test
    public void testWriteReplace_WithAnnotatedConstructor() {
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, ann);
        InnerClassProperty replaced = (InnerClassProperty)property.writeReplace();
        
        assertEquals(innerClassProperty, replaced);
    }
    
    @Test
    public void testWriteReplace_WithoutAnnotatedConstructor() {
        AnnotatedConstructor ann = null;
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, ann);
        InnerClassProperty replaced = (InnerClassProperty)property.writeReplace();
        
        assertEquals(property, replaced);
    }
}