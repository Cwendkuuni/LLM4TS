package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.util.ClassUtil;


import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Mock
    private SettableBeanProperty delegate;

    @Mock
    private Constructor ctor;

    @Mock
    private InnerClassProperty src;

    @Mock
    private JsonDeserializer deser;

    @Mock
    private PropertyName newName;

    @Mock
    private JsonParser jp;

    @Mock
    private DeserializationContext ctxt;

    @Before
    public void setUp() {
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        src = new InnerClassProperty(delegate, new AnnotatedConstructor(null, ctor, null, null));
    }

    @Test
    public void testWithAnnotation() {
        Class<A> annotationClass = A.class;
        A annotation = mock(A.class);
        when(delegate.getAnnotation(annotationClass)).thenReturn(annotation);

        A result = innerClassProperty.getAnnotation(annotationClass);

        assertEquals(annotation, result);
        verify(delegate, times(1)).getAnnotation(annotationClass);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(delegate.getMember()).thenReturn(member);

        AnnotatedMember result = innerClassProperty.getMember();

        assertEquals(member, result);
        verify(delegate, times(1)).getMember();
    }

    @Test
    public void testDeserializeAndSet_JsonValueNull() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Object nullValue = mock(Object.class);
        when(_valueDeserializer.getNullValue(ctxt)).thenReturn(nullValue);

        innerClassProperty.deserializeAndSet(jp, ctxt, null);

        verify(jp, times(1)).getCurrentToken();
        verify(delegate, times(1)).deserializeAndSet(jp, ctxt, nullValue);
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerNotNull() throws IOException {
        when(_valueTypeDeserializer).thenReturn(deser);

        innerClassProperty.deserializeAndSet(jp, ctxt, null);

        verify(_valueTypeDeserializer, times(1)).deserializeWithType(jp, ctxt, _valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSet_DefaultCase() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(_creator.newInstance(instance)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, instance);

        verify(_creator, times(1)).newInstance(instance);
        verify(_valueDeserializer, times(1)).deserialize(jp, ctxt, value);
        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(innerClassProperty.deserialize(jp, ctxt)).thenReturn(value);

        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
        verify(innerClassProperty, times(1)).deserialize(jp, ctxt);
        verify(delegate, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        innerClassProperty.set(instance, value);

        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(delegate.setAndReturn(instance, value)).thenReturn(instance);

        Object result = innerClassProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        verify(delegate, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testReadResolve() {
        Object result = innerClassProperty.readResolve();

        assertNotNull(result);
        assertEquals(InnerClassProperty.class, result.getClass());
    }

    @Test
    public void testWriteReplace_AnnotatedNotNull() {
        when(src._annotated).thenReturn(mock(AnnotatedConstructor.class));

        Object result = innerClassProperty.writeReplace();

        assertNotNull(result);
        assertEquals(InnerClassProperty.class, result.getClass());
    }

    @Test
    public void testWriteReplace_AnnotatedNull() {
        when(src._annotated).thenReturn(null);

        Object result = innerClassProperty.writeReplace();

        assertNotNull(result);
        assertEquals(InnerClassProperty.class, result.getClass());
    }
}