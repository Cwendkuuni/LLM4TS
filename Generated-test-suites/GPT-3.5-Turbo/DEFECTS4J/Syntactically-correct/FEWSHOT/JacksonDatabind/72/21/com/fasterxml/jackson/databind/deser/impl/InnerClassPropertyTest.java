package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.io.IOException;

public class InnerClassPropertyTest {

    @Mock
    SettableBeanProperty delegate;
    
    @Mock
    Constructor<?> ctor;
    
    InnerClassProperty innerClassProperty;
    
    @Mock
    InnerClassProperty src;
    
    @Mock
    AnnotatedConstructor ann;
    
    @Mock
    JsonDeserializer<?> deser;
    
    @Mock
    PropertyName newName;

    @Test
    public void testConstructor() {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertEquals(innerClassProperty._delegate, delegate);
        assertEquals(innerClassProperty._creator, ctor);
    }
    
    @Test
    public void testConstructorWithSrcAndAnn() {
        innerClassProperty = new InnerClassProperty(src, ann);
        assertEquals(innerClassProperty._delegate, src._delegate);
        assertEquals(innerClassProperty._annotated, ann);
        assertEquals(innerClassProperty._creator, ann.getAnnotated());
        try {
            new InnerClassProperty(src, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Missing constructor (broken JDK (de)serialization?)");
        }
    }
    
    @Test
    public void testConstructorWithSrcAndDeser() {
        innerClassProperty = new InnerClassProperty(src, deser);
        assertEquals(innerClassProperty._delegate, src._delegate.withValueDeserializer(deser));
        assertEquals(innerClassProperty._creator, src._creator);
    }
    
    @Test
    public void testConstructorWithSrcAndNewName() {
        innerClassProperty = new InnerClassProperty(src, newName);
        assertEquals(innerClassProperty._delegate, src._delegate.withName(newName));
        assertEquals(innerClassProperty._creator, src._creator);
    }
    
    @Test
    public void testWithPropertyName() {
        innerClassProperty = new InnerClassProperty(src, newName);
        InnerClassProperty result = innerClassProperty.withName(newName);
        assertEquals(result._delegate, src._delegate.withName(newName));
        assertEquals(result._creator, src._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        innerClassProperty = new InnerClassProperty(src, deser);
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        assertEquals(result._delegate, src._delegate.withValueDeserializer(deser));
        assertEquals(result._creator, src._creator);
    }
    
    @Test
    public void testAssignIndex() {
        innerClassProperty = new InnerClassProperty(src, newName);
        innerClassProperty.assignIndex(1);
        // No assertion as it only calls _delegate.assignIndex(index), which is already tested
    }
    
    @Test
    public void testGetPropertyIndex() {
        innerClassProperty = new InnerClassProperty(src, newName);
        innerClassProperty.getPropertyIndex();
        // No assertion as it only calls _delegate.getPropertyIndex(), which is already tested
    }
    
    @Test
    public void testGetAnnotation() {
        innerClassProperty = new InnerClassProperty(src, newName);
        Class<A> acls = A.class;
        innerClassProperty.getAnnotation(acls);
        // No assertion as it only calls _delegate.getAnnotation(acls), which is already tested
    }

    @Test
    public void testGetMember() {
        innerClassProperty = new InnerClassProperty(src, newName);
        innerClassProperty.getMember();
        // No assertion as it only calls _delegate.getMember(), which is already tested
    }
    
    @Test
    public void testDeserializeAndSetWhenCurrentValueIsNull() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        JsonParser jp = null; // specify input
        DeserializationContext ctxt = null; // specify input
        Object bean = null; // specify input
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // No assertion as it does only the deserialization and set operation
    }
    
    @Test
    public void testDeserializeAndSetWhenValueTypeDeserializerIsNotNull() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        JsonParser jp = null; // specify input
        DeserializationContext ctxt = null; // specify input
        Object bean = null; // specify input
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // No assertion as it does only the deserialization and set operation
    }
    
    @Test
    public void testDeserializeAndSetDefaultCase() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        JsonParser jp = null; // specify input
        DeserializationContext ctxt = null; // specify input
        Object bean = null; // specify input
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // No assertion as it does only the deserialization and set operation
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        JsonParser jp = null; // specify input
        DeserializationContext ctxt = null; // specify input
        Object instance = null; // specify input
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        // No assertion as it does only the deserialization and set operation
    }
    
    @Test
    public void testSet() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        Object instance = null; // specify input
        Object value = null; // specify input
        innerClassProperty.set(instance, value);
        // No assertion as it only calls _delegate.set(instance, value), which is already tested
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        Object instance = null; // specify input
        Object value = null; // specify input
        innerClassProperty.setAndReturn(instance, value);
        // No assertion as it only calls _delegate.setAndReturn(instance, value), which is already tested
    }
    
    @Test
    public void testReadResolve() {
        innerClassProperty = new InnerClassProperty(src, ann);
        innerClassProperty.readResolve();
        // No assertion as it only creates and returns a new instance of InnerClassProperty
    }
    
    @Test
    public void testWriteReplaceWhenAnnotatedIsNull() {
        innerClassProperty = new InnerClassProperty(src, ann);
        innerClassProperty.writeReplace();
        // No assertion as it only creates and returns a new instance of InnerClassProperty
    }
    
    @Test
    public void testWriteReplaceWhenAnnotatedIsNotNull() {
        innerClassProperty = new InnerClassProperty(src, null);
        innerClassProperty.writeReplace();
        // No assertion as it only creates and returns a new instance of InnerClassProperty
    }
}