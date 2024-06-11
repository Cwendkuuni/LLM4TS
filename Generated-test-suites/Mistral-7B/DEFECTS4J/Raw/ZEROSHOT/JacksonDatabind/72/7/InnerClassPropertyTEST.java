package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

@RunWith(MockitoJUnitRunner.class)
public class InnerClassPropertyTest extends TestBase {

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> constructor = Mockito.mock(Constructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertNull(property._annotated);
        assertNull(property._creator);
    }

    @Test
    public void testInnerClassProperty() throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        // Test InnerClassProperty constructor
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> constructor = Mockito.mock(Constructor.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor, ann);
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(ann, property._annotated);
        assertEquals(constructor, property._creator);
    }

    @Test
    public void testInnerClassPropertyWithDeserializer() {
        // Test withJsonDeserializer constructor
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> constructor = Mockito.mock(Constructor.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor, ann, deserializer);
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(ann, property._annotated);
        assertEquals(constructor, property._creator);
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testInnerClassPropertyWithName() {
        // Test withName constructor
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        InnerClassProperty property = new InnerClassProperty(delegate, newName);
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Test deserializeAndSet method
        Object instance = new Object();
        JsonParser jp = createContext().createParser("{\"innerClass\":null}");
        Object value = new Object();
        innerClassProperty.deserializeAndSet(jp, context, instance);
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Test deserializeSetAndReturn method
        Object instance = new Object();
        JsonParser jp = createContext().createParser("{\"innerClass\":null}");
        Object returnValue = new Object();
        when(innerClassProperty.deserialize(jp, context)).thenReturn(returnValue);
        Object result = innerClassProperty.deserializeSetAndReturn(jp, context, instance);
        assertEquals(returnValue, result);
        assertEquals(returnValue, innerClassProperty.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        // Test set method
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Test setAndReturn method
        Object instance = new Object();
        Object value = new Object();
        Object result = innerClassProperty.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, innerClassProperty.get(instance));
    }

    @Test
    public void testReadResolve() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // Test readResolve method
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> constructor = Mockito.mock(Constructor.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor, ann);
        assertEquals(property, property.readResolve());
    }

    @Test
    public void testWriteReplace() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // Test writeReplace method
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> constructor = Mockito.mock(Constructor.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor, ann);
        assertEquals(property, property.writeReplace());
    }
}