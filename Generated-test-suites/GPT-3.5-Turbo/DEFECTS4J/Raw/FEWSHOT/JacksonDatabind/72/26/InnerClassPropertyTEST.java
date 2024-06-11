package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {
    private InnerClassProperty innerClassProperty;

    @Mock
    private SettableBeanProperty delegate;

    @Mock
    private Constructor<?> constructor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }

    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("value");
        when(constructor.newInstance(bean)).thenReturn(bean);
        when(delegate.getNullValue(ctxt)).thenReturn(null);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(constructor, times(1)).newInstance(bean);
        verify(delegate, times(1)).deserialize(jp, ctxt, bean);
        verify(delegate, times(1)).set(bean, bean);
    }

    @Test
    public void testDeserializeAndSetNullValue() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(delegate.getNullValue(ctxt)).thenReturn(null);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(delegate, never()).deserialize(any(), any(), any());
    }

    @Test
    public void testDeserializeAndSetWithTypeDeser() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("value");

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(delegate, never()).getNullValue(ctxt);
        verify(delegate, never()).deserialize(any(), any(), any());
        verify(delegate, times(1)).set(bean, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("value");
        when(constructor.newInstance(instance)).thenReturn(instance);
        when(delegate.getNullValue(ctxt)).thenReturn(null);

        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);

        verify(constructor, times(1)).newInstance(instance);
        verify(delegate, times(1)).deserialize(jp, ctxt, instance);
        verify(delegate, times(1)).set(instance, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        innerClassProperty.set(instance, value);

        verify(delegate, times(1)).set(instance, value);
        verify(delegate, never()).setAndReturn(any(), any());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        when(delegate.setAndReturn(instance, value)).thenReturn(value);

        Object result = innerClassProperty.setAndReturn(instance, value);

        verify(delegate, never()).set(any(), any());
        verify(delegate, times(1)).setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testWithAnnotation() {
        Class<? extends Annotation> annotationClass = Annotation.class;

        when(delegate.getAnnotation(annotationClass)).thenReturn(null);

        Annotation result = innerClassProperty.getAnnotation(annotationClass);

        verify(delegate, times(1)).getAnnotation(annotationClass);
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);

        when(delegate.getMember()).thenReturn(member);

        AnnotatedMember result = innerClassProperty.getMember();

        verify(delegate, times(1)).getMember();
        assertEquals(member, result);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");

        when(delegate.withName(newName)).thenReturn(delegate);

        InnerClassProperty result = innerClassProperty.withName(newName);

        verify(delegate, times(1)).withName(newName);
        assertEquals(delegate, result._delegate);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        when(delegate.withValueDeserializer(deserializer)).thenReturn(delegate);

        InnerClassProperty result = innerClassProperty.withValueDeserializer(deserializer);

        verify(delegate, times(1)).withValueDeserializer(deserializer);
        assertEquals(delegate, result._delegate);
    }

    @Test
    public void testAssignIndex() {
        int index = 1;

        innerClassProperty.assignIndex(index);

        verify(delegate, times(1)).assignIndex(index);
    }

    @Test
    public void testGetPropertyIndex() {
        int index = 1;

        when(delegate.getPropertyIndex()).thenReturn(index);

        int result = innerClassProperty.getPropertyIndex();

        verify(delegate, times(1)).getPropertyIndex();
        assertEquals(index, result);
    }
}