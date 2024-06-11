package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.annotation.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(creator, innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);

        InnerClassProperty innerClassProperty2 = new InnerClassProperty(innerClassProperty, annotated);
        assertSame(delegate, innerClassProperty2._delegate);
        assertSame(creator, innerClassProperty2._creator);
        assertSame(annotated, innerClassProperty2._annotated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAnnotated() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = createMock(JsonParser.class);
        DeserializationContext ctxt = createMock(DeserializationContext.class);
        Object bean = createMock(Object.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        Object value = new Object();

        when(delegate.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(creator.newInstance(bean)).thenReturn(value);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(delegate).set(bean, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = createMock(JsonParser.class);
        DeserializationContext ctxt = createMock(DeserializationContext.class);
        Object instance = createMock(Object.class);

        Object value = new Object();

        when(jp.getValueAsBoolean()).thenReturn(false);
        when(jp.getValueAsNull()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn("value");
        when(jp.getValueAsInt()).thenReturn(1);
        when(jp.getValueAsLong()).thenReturn(1L);
        when(jp.getValueAsFloat()).thenReturn(1.0f);
        when(jp.getValueAsDouble()).thenReturn(1.0);

        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        when(jp.currentName()).thenReturn("name");

        when(jp.isExpectedStartVectorToken()).thenReturn(false);
        when(jp.nextValue()).thenReturn("value");

        when(_jacksonObjectMapper.readValue(jp, Object.class)).thenReturn(instance);

        InnerClassProperty innerClassProperty = new InnerClassProperty(mock(SettableBeanProperty.class), mock(Constructor.class));

        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertSame(instance, result);

        verify(jp).nextToken();
        verify(jp).nextValue();
        verifyNoMoreInteractions(jp);
    }

    @Test
    public void testSet() throws IOException {
        Object bean = createMock(Object.class);
        Object value = new Object();
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.set(bean, value);

        verify(delegate).set(bean, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object bean = createMock(Object.class);
        Object value = new Object();
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);

        when(delegate.setAndReturn(bean, value)).thenReturn(value);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Object result = innerClassProperty.setAndReturn(bean, value);

        assertSame(value, result);

        verify(delegate).setAndReturn(bean, value);
    }

    @Test
    public void testReadResolve() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        InnerClassProperty innerClassProperty2 = innerClassProperty.readResolve();

        assertSame(delegate, innerClassProperty2._delegate);
        assertSame(annotated, innerClassProperty2._annotated);
    }

    @Test
    public void testWriteReplace() throws IOException {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedConstructor annotated = new AnnotatedConstructor(null, mock(Constructor.class), null, null);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);

        Object result = innerClassProperty.writeReplace();
        assertSame(innerClassProperty, result);

        Object result2 = innerClassProperty.writeReplace();
        assertSame(innerClassProperty, result2);

        InnerClassProperty innerClassProperty2 = new InnerClassProperty(innerClassProperty, annotated);
        Object result3 = innerClassProperty2.writeReplace();
        assertSame(innerClassProperty2, result3);
    }
}