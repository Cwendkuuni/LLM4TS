package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import org.mockito.Mock;

public class FieldPropertyTest {
    @Mock
    JsonParser p;
    @Mock
    DeserializationContext ctxt;

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        FieldProperty prop = new FieldProperty(null, null, null, null, new AnnotatedField(null, field));

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(prop._skipNulls).thenReturn(false);
        assertEquals(instance, prop.deserializeSetAndReturn(p, ctxt, instance));
        verify(prop, times(1)).set(instance, value);

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(prop._valueTypeDeserializer).thenReturn(null);
        when(prop._valueDeserializer.deserialize(p, ctxt)).thenReturn(value);
        assertEquals(instance, prop.deserializeSetAndReturn(p, ctxt, instance));
        verify(prop, times(1)).set(instance, value);

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(prop._valueTypeDeserializer).thenReturn(mock(TypeDeserializer.class));
        when(prop._valueDeserializer.deserializeWithType(p, ctxt, prop._valueTypeDeserializer)).thenReturn(value);
        assertEquals(instance, prop.deserializeSetAndReturn(p, ctxt, instance));
        verify(prop, times(2)).set(instance, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        FieldProperty prop = new FieldProperty(null, null, null, null, new AnnotatedField(null, field));

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(prop._valueTypeDeserializer).thenReturn(null);
        when(prop._valueDeserializer.deserialize(p, ctxt)).thenReturn(value);
        doThrow(IOException.class).when(prop).set(instance, value);
        prop.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        FieldProperty prop = new FieldProperty(null, null, null, null, new AnnotatedField(null, field));

        assertEquals(instance, prop.setAndReturn(instance, value));
        verify(prop, times(1)).set(instance, value);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithException() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        FieldProperty prop = new FieldProperty(null, null, null, null, new AnnotatedField(null, field));

        doThrow(IOException.class).when(prop).set(instance, value);
        prop.setAndReturn(instance, value);
    }
}