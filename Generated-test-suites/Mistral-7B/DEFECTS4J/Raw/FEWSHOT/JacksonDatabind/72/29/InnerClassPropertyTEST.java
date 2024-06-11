package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class InnerClassPropertyTest extends BaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testInnerClassProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test constructor with delegate and creator
        Class<?> innerClassType = Mockito.mock(Class.class);
        Constructor<?> creator = mock(Constructor.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        assertNotNull(innerClassProperty);
        verify(delegate, times(1)).getName();
        verify(creator, times(1)).getDeclaringClass();

        // Test constructor with source and ann
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty2 = new InnerClassProperty(mock(InnerClassProperty.class), ann);
        assertNotNull(innerClassProperty2);
        verify(ann, times(1)).getAnnotated();

        // Test deserialization
        Object bean = mock(Object.class);
        Object value = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(delegate.getMember()).thenReturn(member);
        when(delegate.withValueDeserializer(any())).thenReturn(new InnerClassProperty(delegate, mock(JsonDeserializer.class)));

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("foo");

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(bean, times(1)).set(eq(member), eq(value));

        // Test deserialization with null value
        when(jp.getText()).thenReturn(null);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(bean, times(1)).set(eq(member), null);

        // Test deserialization with valueTypeDeserializer
        JsonDeserializer<?> valueTypeDeserializer = mock(JsonDeserializer.class);
        when(delegate.withValueDeserializer(valueTypeDeserializer)).thenReturn(new InnerClassProperty(delegate, valueTypeDeserializer));
        when(jp.getText()).thenReturn("bar");

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(valueTypeDeserializer, times(1)).deserialize(jp, ctxt, eq(value));

        // Test set and return
        when(delegate.setAndReturn(bean, eq(value))).thenReturn(new Object());
        Object result = innerClassProperty.setAndReturn(bean, value);
        assertNotNull(result);

        // Test writeReplace
        Object proxy = new InnerClassProperty(innerClassProperty, null);
        proxy = proxy.writeReplace();
        assertNotNull(proxy);
        verify(proxy, times(1)).getCreator();

        // Test readResolve
        Object proxy2 = new InnerClassProperty(innerClassProperty, null);
        proxy2 = proxy2.readResolve();
        assertNotNull(proxy2);
        verify(proxy2, times(1)).getDelegate();

        // Test deserialization with exception
        doThrow(InvocationTargetException.class).when(creator).newInstance(bean);
        when(jp.getText()).thenReturn("foo");
        try {
            innerClassProperty.deserializeAndSet(jp, ctxt, bean);
            fail();
        } catch (Exception e) {
            verify(jp, times(1)).getCurrentToken();
            verify(ctxt, times(1)).handleMissingValue();
            verifyNoMoreInteractions(bean, member, delegate);
        }
    }
}