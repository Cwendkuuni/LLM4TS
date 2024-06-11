package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

public class InnerClassPropertyTest extends BaseMapTest {

    private static final Object FAKE_VALUE = new Object() {};
    private static final Class<?> FAKE_VALUE_CLASS = FAKE_VALUE.getClass();

    @Test
    public void testInnerClassPropertyCreation() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        assertSame(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
    }

    @Test
    public void testInnerClassPropertyCreationWithAnnotatedConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotated);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(annotated, innerClassProperty._annotated);
        assertSame(constructor, innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertyCreationWithDeserializer() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deserializer);
        assertSame(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
        assertSame(deserializer, innerClassProperty._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyCreationWithNewName() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, newName);
        assertSame(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
        assertSame(newName, innerClassProperty._name);
    }

    @Test
    public void testAssignIndex() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.assignIndex(0);
        verify(delegate).assignIndex(0);
    }

    @Test
    public void testGetPropertyIndex() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        assertEquals(delegate.getPropertyIndex(), innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Annotation annotation = mock(Annotation.class);
        when(delegate.getAnnotation(Mockito.any(Class.class))).thenReturn(annotation);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        assertEquals(annotation, innerClassProperty.getAnnotation(Mockito.any(Annotation.class).getClass()));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(delegate.getMember()).thenReturn(member);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        assertEquals(member, innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> constructor = mock(Constructor.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        when(constructor.newInstance(bean)).thenReturn(FAKE_VALUE);
        when(delegate.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(ctx.findValueDeserializer(FAKE_VALUE_CLASS)).thenReturn(mock(JsonDeserializer.class));
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(jp).nextToken();
        verify(ctx).findBeanProperty(FAKE_VALUE_CLASS.getName());
        verify(ctx).findValueDeserializer(FAKE_VALUE_CLASS);
        verify(delegate).set(bean, FAKE_VALUE);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deserializer);
        when(delegate.getMember()).thenReturn(mock(AnnotatedMember.class));
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, bean);
        verify(jp).nextToken();
        verify(delegate).deserialize(jp, ctxt, FAKE_VALUE);
        assertEquals(FAKE_VALUE, result);
    }

    @Test
    public void testSet() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.set(bean, FAKE_VALUE);
        verify(delegate).set(bean, FAKE_VALUE);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Object result = innerClassProperty.setAndReturn(bean, FAKE_VALUE);
        verify(delegate).set(bean, FAKE_VALUE);
        assertEquals(FAKE_VALUE, result);
    }

    @Test
    public void testReadResolve() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotated);
        InnerClassProperty result = innerClassProperty.readResolve();
        assertSame(delegate, result._delegate);
        assertSame(annotated, result._annotated);
        assertNull(result._creator);
    }

    @Test
    public void testWriteReplace() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedConstructor annotated = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotated);
        Object result = innerClassProperty.writeReplace();
        assertSame(delegate, result);
    }
}