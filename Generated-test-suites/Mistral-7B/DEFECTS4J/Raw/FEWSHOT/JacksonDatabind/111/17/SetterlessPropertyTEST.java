package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {

    private Object instance;
    private Method getter;

    @Before
    public void setUp() throws Exception {
        instance = mock(Object.class);
        Class<?> clazz = SetterlessProperty.class;
        getter = clazz.getDeclaredMethod("_getter");
        getter.setAccessible(true);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        SettableBeanProperty property = spy(new SetterlessProperty(null, null, null, null, null));
        Annotation annotation = new Object() {}.getClass();
        assertEquals(annotation, property.getAnnotation(annotation.getClass()));
    }

    @Test
    public void testGetMember() throws IOException {
        SettableBeanProperty property = spy(new SetterlessProperty(null, null, null, null, null));
        AnnotatedMember member = mock(AnnotatedMember.class);
        assertEquals(member, property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty property = spy(new SetterlessProperty(null, null, null, null, null));
        Object value = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("test");

        property.deserializeAndSet(parser, context, instance);

        assertNotNull(getValue(instance, getter));
        assertEquals("test", getValue(instance, getter));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty property = spy(new SetterlessProperty(null, null, null, null, null));
        Object value = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("test");

        Object result = property.deserializeSetAndReturn(parser, context, instance);

        assertNotNull(result);
        assertSame(result, instance);
        assertEquals("test", getValue(instance, getter));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SettableBeanProperty property = new SetterlessProperty(null, null, null, null, null);
        property.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty property = new SetterlessProperty(null, null, null, null, null);
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);

        assertSame(result, instance);
    }

    private Object getValue(Object instance, Method method) throws IOException {
        return method.invoke(instance, (Object[]) null);
    }
}