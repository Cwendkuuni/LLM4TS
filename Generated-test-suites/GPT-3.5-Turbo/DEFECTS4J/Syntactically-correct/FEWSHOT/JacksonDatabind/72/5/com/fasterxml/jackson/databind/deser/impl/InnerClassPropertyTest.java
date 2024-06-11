package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {
    
    @Test
    public void testConstructorWithDelegateAndCtor() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        Constructor<?> ctor = ClassUtil.getConstructor(InnerClassProperty.class, SettableBeanProperty.class, Constructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }
    
    @Test
    public void testConstructorWithSrcAndAnn() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, null);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(src, ann);
        
        assertSame(delegate, property._delegate);
        assertSame(ann, property._annotated);
        assertSame(ann.getAnnotated(), property._creator);
    }
    
    @Test
    public void testConstructorWithSrcAndDeser() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>(null);
        InnerClassProperty property = new InnerClassProperty(src, deser);
        
        assertSame(delegate, property._delegate);
        assertSame(deser, property._delegate.getValueDeserializer());
        assertNull(property._creator);
    }
    
    @Test
    public void testConstructorWithSrcAndNewName() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty src = new InnerClassProperty(delegate, null);
        PropertyName newName = new PropertyName("");
        InnerClassProperty property = new InnerClassProperty(src, newName);
        
        assertSame(delegate, property._delegate);
        assertEquals(newName, property._delegate.getFullName());
        assertNull(property._creator);
    }
    
    @Test
    public void testWithNullName() {
        SettableBeanProperty delegate = new SettableBeanProperty(null, null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        PropertyName newName = null;
        InnerClassProperty newProperty = property.withName(newName);
        
        assertSame(delegate, newProperty._delegate);
        assertNull(newProperty._delegate.getFullName());
        assertNull(newProperty._creator);
    }
    
    @Test
    public void testWithNullDeser() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        JsonDeserializer<?> deser = null;
        InnerClassProperty newProperty = property.withValueDeserializer(deser);
        
        assertSame(delegate, newProperty._delegate);
        assertNull(newProperty._delegate.getValueDeserializer());
        assertNull(newProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        property.assignIndex(0);
        
        assertEquals(0, delegate.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        delegate.assignIndex(0);
        
        assertEquals(0, property.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        Class<? extends Annotation> acls = null;
        assertNull(property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        property.deserializeAndSet(jp, ctxt, bean);
        // assert something
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = property.deserializeSetAndReturn(jp, ctxt, bean);
        // assert something
        assertNotNull(result);
    }
    
    @Test
    public void testSet() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        Object instance = null;
        Object value = null;
        property.set(instance, value);
        // assert something
    }
    
    @Test
    public void testSetAndReturn() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        Object instance = null;
        Object value = null;
        Object result = property.setAndReturn(instance, value);
        // assert something
        assertNotNull(result);
    }
    
    @Test
    public void testReadResolve() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        InnerClassProperty result = (InnerClassProperty) property.readResolve();
        // assert something
        assertNotNull(result);
    }
    
    @Test
    public void testWriteReplace() {
        SettableBeanProperty delegate = new SettableBeanProperty(new AnnotationMap(), null, null, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        
        Object result = property.writeReplace();
        // assert something
        assertNotNull(result);
    }
}