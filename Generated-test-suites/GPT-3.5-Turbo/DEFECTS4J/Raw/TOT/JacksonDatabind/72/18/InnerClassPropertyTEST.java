
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class InnerClassPropertyTest {

    @Test
    public void testConstructorWithDelegateAndCtor() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>(String.class);
        
        // When
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        
        // Then
        assertEquals(delegate, innerClassProperty.getDelegate());
        assertEquals(ctor, innerClassProperty.getCreator());
        assertNull(innerClassProperty.getAnnotated());
    }
    
    @Test
    public void testConstructorWithSrcAndAnnotatedConstructor() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>(String.class);
        AnnotatedConstructor annotated = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        
        // When
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, annotated);
        
        // Then
        assertEquals(delegate, innerClassProperty.getDelegate());
        assertEquals(ctor, innerClassProperty.getCreator());
        assertEquals(annotated, innerClassProperty.getAnnotated());
    }
    
    @Test
    public void testConstructorWithSrcAndDeserializer() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty src = new InnerClassProperty(delegate, deser);
        
        // When
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        
        // Then
        assertEquals(delegate, innerClassProperty.getDelegate());
        assertEquals(deser, innerClassProperty.getValueDeserializer());
    }
    
    @Test
    public void testConstructorWithSrcAndNewName() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty src = new InnerClassProperty(delegate);
        
        // When
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        
        // Then
        assertEquals(delegate, innerClassProperty.getDelegate());
        assertEquals(newName, innerClassProperty.getPropertyName());
    }
    
    @Test
    public void testWithName() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        
        // When
        InnerClassProperty newInnerClassProperty = innerClassProperty.withName(newName);
        
        // Then
        assertEquals(delegate, newInnerClassProperty.getDelegate());
        assertEquals(newName, newInnerClassProperty.getPropertyName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        
        // When
        InnerClassProperty newInnerClassProperty = innerClassProperty.withValueDeserializer(deser);
        
        // Then
        assertEquals(delegate, newInnerClassProperty.getDelegate());
        assertEquals(deser, newInnerClassProperty.getValueDeserializer());
    }
    
    @Test
    public void testAssignIndex() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        int index = 1;
        
        // When
        innerClassProperty.assignIndex(index);
        
        // Then
        assertEquals(index, innerClassProperty.getIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        int index = 1;
        innerClassProperty.assignIndex(index);
        
        // When
        int propertyIndex = innerClassProperty.getPropertyIndex();
        
        // Then
        assertEquals(index, propertyIndex);
    }
    
    @Test
    public void testGetAnnotation() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        
        // When
        Annotation annotation = innerClassProperty.getAnnotation(MyAnnotation.class);
        
        // Then
        assertNotNull(annotation);
    }
    
    @Test
    public void testGetMember() {
        // Given
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate);
        
        // When
        AnnotatedMember member = innerClassProperty.getMember();
        
        // Then
        assertNotNull(member);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        // Given
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty.setValueDeserializer(new JsonDeserializer<>());
        
        // When
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        // Then
        assertNotNull(innerClassProperty.getValue());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty.setValueDeserializer(new JsonDeserializer<>());
        
        // When
        Object value = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        
        // Then
        assertNotNull(value);
    }
    
    @Test
    public void testSet() throws IOException {
        // Given
        Object instance = new Object();
        Object value = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        
        // When
        innerClassProperty.set(instance, value);
        
        // Then
        assertNotNull(innerClassProperty.getValue());
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        Object instance = new Object();
        Object value = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        
        // When
        Object returnValue = innerClassProperty.setAndReturn(instance, value);
        
        // Then
        assertEquals(value, returnValue);
    }
    
    @Test
    public void testReadResolve() {
        // Given
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, new Constructor<>());
        innerClassProperty.setAnnotated(annotatedConstructor);
        
        // When
        InnerClassProperty resolvedProperty = innerClassProperty.readResolve();
        
        // Then
        assertEquals(innerClassProperty.getDelegate(), resolvedProperty.getDelegate());
        assertEquals(innerClassProperty.getCreator(), resolvedProperty.getCreator());
        assertNull(resolvedProperty.getAnnotated());
    }
    
    @Test
    public void testWriteReplaceWithAnnotated() {
        // Given
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, new Constructor<>());
        innerClassProperty.setAnnotated(annotatedConstructor);
        
        // When
        Object replace = innerClassProperty.writeReplace();
        
        // Then
        assertEquals(innerClassProperty, replace);
    }
    
    @Test
    public void testWriteReplaceWithoutAnnotated() {
        // Given
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        
        // When
        Object replace = innerClassProperty.writeReplace();
        
        // Then
        InnerClassProperty newProperty = (InnerClassProperty) replace;
        assertNotNull(newProperty.getAnnotated());
    }
}

