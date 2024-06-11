package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt  = new DeserializationContext();
        Object bean = new Object();
        
        try {
            property.deserializeAndSet(jp, ctxt, bean);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt  = new DeserializationContext();
        Object instance = new Object();
        
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testSet() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        Object instance = new Object();
        Object value = new Object();
        
        try {
            property.set(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testSetAndReturn() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        Object instance = new Object();
        Object value = new Object();
        
        try {
            property.setAndReturn(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testReadResolve() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        AnnotatedConstructor ann = new AnnotatedConstructor(null, ctor, null, null);
        InnerClassProperty property = new InnerClassProperty(delegate, ann);
        
        Object obj = property.readResolve();
        
        assertTrue(obj instanceof InnerClassProperty);
        InnerClassProperty result = (InnerClassProperty) obj;
        
        assertSame(property, result);
        assertSame(ann, result._annotated);
    }

    @Test
    public void testWriteReplace() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        Object obj = property.writeReplace();
        
        assertTrue(obj instanceof InnerClassProperty);
        InnerClassProperty result = (InnerClassProperty) obj;
        
        assertNull(result._annotated);
        assertSame(property._creator, result._creator);
    }
}