package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

    @Mock
    private SettableBeanProperty delegate;

    @Mock
    private Constructor<?> constructor;

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }

    @Test
    public void testConstructorWithDelegateAndConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithSourceAndAnnotatedConstructor() {
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty source = mock(InnerClassProperty.class);
        when(source._delegate).thenReturn(delegate);
        when(source._creator).thenReturn(constructor);
        innerClassProperty = new InnerClassProperty(source, ann);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithSourceAndDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        InnerClassProperty source = mock(InnerClassProperty.class);
        when(source._delegate).thenReturn(delegate);
        when(source._creator).thenReturn(constructor);
        innerClassProperty = new InnerClassProperty(source, deser);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithSourceAndNewName() {
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty source = mock(InnerClassProperty.class);
        when(source._delegate).thenReturn(delegate);
        when(source._creator).thenReturn(constructor);
        innerClassProperty = new InnerClassProperty(source, newName);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty newInnerClassProperty = innerClassProperty.withName(newName);

        assertNotNull(newInnerClassProperty);
        assertEquals(delegate, newInnerClassProperty._delegate);
        assertEquals(constructor, newInnerClassProperty._creator);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        InnerClassProperty newInnerClassProperty = innerClassProperty.withValueDeserializer(deser);

        assertNotNull(newInnerClassProperty);
        assertEquals(delegate, newInnerClassProperty._delegate);
        assertEquals(constructor, newInnerClassProperty._creator);
    }

    @Test
    public void testAssignIndex() {
        int index = 1;
        innerClassProperty.assignIndex(index);

        verify(delegate).assignIndex(index);
    }

    @Test
    public void testGetPropertyIndex() {
        int expectedIndex = 1;
        when(delegate.getPropertyIndex()).thenReturn(expectedIndex);

        int actualIndex = innerClassProperty.getPropertyIndex();

        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);
        innerClassProperty.getAnnotation(acls);

        verify(delegate).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        innerClassProperty.getMember();

        verify(delegate).getMember();
    }

    @Test
    public void testDeserializeAndSetWithNullValueToken() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        Object nullValue = mock(Object.class);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        ArgumentCaptor<JsonDeserializer<Object>> captor = ArgumentCaptor.forClass(JsonDeserializer.class);
        when(delegate.getNullValue(ctxt)).thenReturn(nullValue);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(jp).getCurrentToken();
        verify(delegate).getNullValue(ctxt);
        verify(delegate, never()).withValueDeserializer(captor.capture());
        verify(delegate, never()).deserialize(jp, ctxt, nullValue);
        verify(delegate).set(bean, nullValue);
    }

    @Test
    public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        Object value = mock(Object.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> valueTypeDeserializer = mock(JsonDeserializer.class);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_OTHER);
        when(innerClassProperty._valueDeserializer).thenReturn(valueDeserializer);
        when(innerClassProperty._valueTypeDeserializer).thenReturn(valueTypeDeserializer);
        when(valueTypeDeserializer.deserializeWithType(jp, ctxt, valueTypeDeserializer)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(jp).getCurrentToken();
        verify(innerClassProperty).set(bean, value);
        verify(delegate, never()).getNullValue(ctxt);
        verify(delegate, never()).withValueDeserializer(any(JsonDeserializer.class));
        verify(delegate, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testDeserializeAndSetWithDefaultValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        Object value = mock(Object.class);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_OTHER);
        when(innerClassProperty._creator.newInstance(bean)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        verify(jp).getCurrentToken();
        verify(innerClassProperty).set(bean, value);
        verify(delegate, never()).getNullValue(ctxt);
        verify(delegate, never()).withValueDeserializer(any(JsonDeserializer.class));
        verify(delegate, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(innerClassProperty.deserialize(jp, ctxt)).thenReturn(value);

        Object returnValue = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, returnValue);
        verify(innerClassProperty).deserialize(jp, ctxt);
        verify(innerClassProperty).set(instance, value);
        verify(delegate, never()).setAndReturn(instance, value);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        innerClassProperty.set(instance, value);

        verify(delegate).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(delegate.setAndReturn(instance, value)).thenReturn(value);

        Object returnValue = innerClassProperty.setAndReturn(instance, value);

        assertEquals(value, returnValue);
        verify(delegate).setAndReturn(instance, value);
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty newInnerClassProperty = innerClassProperty.readResolve();

        assertNotNull(newInnerClassProperty);
        assertEquals(innerClassProperty, newInnerClassProperty);
        assertNull(newInnerClassProperty._annotated);
    }

    @Test
    public void testWriteReplaceWithNonNullAnnotated() {
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        innerClassProperty._annotated = annotatedConstructor;

        Object replacement = innerClassProperty.writeReplace();

        assertNotNull(replacement);
        assertTrue(replacement instanceof InnerClassProperty);
        assertEquals(innerClassProperty, replacement);
    }

    @Test
    public void testWriteReplaceWithNullAnnotated() {
        innerClassProperty._annotated = null;
        AnnotatedConstructor expectedAnnotatedConstructor = new AnnotatedConstructor(null, innerClassProperty._creator, null, null);

        Object replacement = innerClassProperty.writeReplace();

        assertNotNull(replacement);
        assertTrue(replacement instanceof InnerClassProperty);
        assertEquals(expectedAnnotatedConstructor, ((InnerClassProperty) replacement)._annotated);
    }
}