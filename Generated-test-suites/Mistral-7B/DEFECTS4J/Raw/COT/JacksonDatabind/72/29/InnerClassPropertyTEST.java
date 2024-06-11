package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for InnerClassProperty class
 */
public class InnerClassPropertyTest {

    private static final Object TEST_INSTANCE = new Object();
    private Object testValue;
    private SettableBeanProperty delegate;
    private InnerClassProperty innerClassProperty;
    private AnnotatedConstructor constructor;
    private JsonDeserializer<Object> valueDeserializer;
    private JsonDeserializer<Object> valueTypeDeserializer;

    @Before
    public void setUp() throws Exception {
        delegate = Mockito.spy(new SettableBeanProperty(Mockito.mock(SettableBeanProperty.class), Mockito.mock(Constructor.class)));
        constructor = Mockito.mock(AnnotatedConstructor.class);
        valueDeserializer = Mockito.mock(JsonDeserializer.class);
        valueTypeDeserializer = Mockito.mock(JsonDeserializer.class);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
        innerClassProperty = new InnerClassProperty(innerClassProperty, valueDeserializer);
        innerClassProperty = new InnerClassProperty(innerClassProperty, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._annotated);
        assertNotNull(innerClassProperty._creator);
    }

    @Test
    public void testWithName() throws Exception {
        String newName = "newName";
        innerClassProperty = innerClassProperty.withName(new PropertyName(newName));
        assertEquals(newName, innerClassProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        innerClassProperty = innerClassProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, innerClassProperty._valueDeserializer);
    }

    @Test
    public void testAssignIndex() throws Exception {
        int index = 1;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() throws Exception {
        int index = 1;
        innerClassProperty.getPropertyIndex();
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() throws Exception {
        Annotation annotation = Mockito.mock(Annotation.class);
        assertEquals(annotation, innerClassProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() throws Exception {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        assertEquals(member, innerClassProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSet_ValueIsNull() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        innerClassProperty.deserializeAndSet(jp, ctxt, TEST_INSTANCE);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializerIsNull() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object value = new Object();
        innerClassProperty.deserializeAndSet(jp, ctxt, TEST_INSTANCE);
        assertEquals(value, innerClassProperty._delegate.getBean());
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerIsNull() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object value = new Object();
        innerClassProperty._valueTypeDeserializer = valueTypeDeserializer;
        innerClassProperty.deserializeAndSet(jp, ctxt, TEST_INSTANCE);
        assertEquals(value, innerClassProperty._delegate.getBean());
    }

    @Test
    public void testDeserializeAndSet_ConstructorThrowsException() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Mockito.doThrow(new IOException("Test exception"))
                .when(constructor).newInstance(TEST_INSTANCE);
        innerClassProperty.deserializeAndSet(jp, ctxt, TEST_INSTANCE);
        assertTrue(innerClassProperty._valueDeserializer.isCachingInstances());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object value = new Object();
        Mockito.doReturn(value).when(constructor).newInstance(TEST_INSTANCE);
        Mockito.doAnswer(invocationOnMock -> innerClassProperty._valueDeserializer.deserialize(jp, ctxt, value))
                .when(innerClassProperty).deserialize(jp, ctxt);
        innerClassProperty.deserializeAndSet(jp, ctxt, TEST_INSTANCE);
        assertEquals(value, innerClassProperty._delegate.getBean());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        Mockito.doReturn(value).when( innerClassProperty ).deserialize(jp, ctxt);
        assertEquals(value, innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        innerClassProperty.set(bean, value);
        assertEquals(value, innerClassProperty._delegate.getBean());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        assertEquals(value, innerClassProperty.setAndReturn(bean, value));
    }

    @Test
    public void testReadResolve() throws Exception {
        Object instance = new Object();
        Object result = innerClassProperty.readResolve();
        assertEquals(innerClassProperty, result);
    }

    @Test
    public void testWriteReplace() throws Exception {
        Object instance = new Object();
        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }
}