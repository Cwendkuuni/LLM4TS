
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {
    
    private InnerClassProperty property;
    private SettableBeanProperty delegate;
    private String name;
    private Constructor<?> ctor;
    private InnerClassProperty propertyWithDelegator;
    private InnerClassProperty propertyWithDeserializer;
    private InnerClassProperty propertyWithNewName;
    
    @Before
    public void setUp() {
        delegate = new SettableBeanProperty();
        name = "property";
        ctor = new Constructor<>();
        property = new InnerClassProperty(delegate, ctor);
        propertyWithDelegator = new InnerClassProperty(property, new AnnotatedConstructor());
        propertyWithDeserializer = new InnerClassProperty(property, new JsonDeserializer<>());
        propertyWithNewName = new InnerClassProperty(property, new PropertyName());
    }
    
    @Test
    public void testInnerClassProperty() {
        assertNotNull(property);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }
    
    @Test
    public void testInnerClassPropertyWithAnnotatedConstructor() {
        assertNotNull(propertyWithDelegator);
        assertNotNull(propertyWithDelegator._annotated);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertEquals(delegate, propertyWithDelegator._delegate);
        assertEquals(ctor, propertyWithDelegator._creator);
    }
    
    @Test
    public void testInnerClassPropertyWithDeserializer() {
        assertNotNull(propertyWithDeserializer);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertNotNull(propertyWithDeserializer._delegate);
        assertEquals(delegate, propertyWithDeserializer._delegate);
        assertEquals(ctor, propertyWithDeserializer._creator);
    }

    @Test
    public void testInnerClassPropertyWithNewName() {
        assertNotNull(propertyWithNewName);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertNotNull(propertyWithNewName._delegate);
        assertEquals(delegate, propertyWithNewName._delegate);
        assertEquals(ctor, propertyWithNewName._creator);
    }
    
    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName();
        InnerClassProperty innerClassProperty = property.withName(newName);
        assertNotNull(innerClassProperty);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertNotNull(innerClassProperty._delegate);
        assertEquals(delegate.getName(), newName);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = property.withValueDeserializer(deser);
        assertNotNull(innerClassProperty);
        assertNotNull(delegate);
        assertNotNull(ctor);
        assertNotNull(innerClassProperty._delegate);
        assertEquals(delegate.getDeser(), deser);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        int index = 1;
        property.assignIndex(index);
        assertEquals(index, property._delegate.assignIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        assertEquals(delegate.getNumber(), property.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertEquals(delegate.getAnnotation(acls), property.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        assertEquals(delegate.getMember(), property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object value = delegate.getValue();
        assertEquals(value, bean);
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        try {
            Object result = property.deserializeSetAndReturn(jp, ctxt, bean);
            Object value = delegate.getValue();
            assertEquals(value, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetValue() {
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(value, delegate.getValue());
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            Object result = property.setAndReturn(instance, value);
            assertEquals(value, delegate.getValue());
            assertEquals(value, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty result = property.readResolve();
        assertNotNull(result);
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty result = property.writeReplace();
        assertNotNull(result);
    }
}
