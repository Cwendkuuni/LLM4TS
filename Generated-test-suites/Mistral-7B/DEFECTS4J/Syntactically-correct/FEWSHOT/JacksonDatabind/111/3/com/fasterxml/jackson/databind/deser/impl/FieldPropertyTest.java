package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        Field field = mock(Field.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        JavaType javaType = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(null, null, null, null, null, null, null, null),
                javaType, typeDeserializer, contextAnnotations, annotatedField);
        assertSame(annotatedField, fieldProperty._annotated);
        assertSame(field, fieldProperty._field);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(null, newName);
        assertSame(newName, fieldProperty._name);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(null, valueDeserializer, null);
        assertSame(valueDeserializer, fieldProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, nullValueProvider);
        assertSame(nullValueProvider, fieldProperty._nullProvider);
    }

    @Test
    public void testFixAccess() throws IOException {
        Field field = mock(Field.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        FieldProperty fieldProperty = new FieldProperty(null);
        fieldProperty.fixAccess(config);
        ClassUtil.checkAndFixAccess(field, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testGetAnnotation() {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        Annotation annotation = mock(Annotation.class);
        when(annotatedField.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, fieldProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        FieldProperty fieldProperty = new FieldProperty(null);
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance).set(value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        FieldProperty fieldProperty = new FieldProperty(null);
        fieldProperty.deserializeSetAndReturn(jsonParser, context, instance);
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        FieldProperty fieldProperty = new FieldProperty(null);
        fieldProperty.set(instance, value);
        verify(instance).set(value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        FieldProperty fieldProperty = new FieldProperty(null);
        fieldProperty.setAndReturn(instance, value);
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void testReadResolve() {
        FieldProperty fieldProperty = new FieldProperty(null);
        FieldProperty resolved = fieldProperty.readResolve();
        assertSame(fieldProperty, resolved);
    }

    @Test
    public void testSkipNulls() {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null);
        fieldProperty._skipNulls = true;
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance, never()).set(null);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._valueTypeDeserializer = typeDeserializer;
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(typeDeserializer.deserialize(jsonParser, context, String.class)).thenReturn("test");
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance).set(value);
        verify(value).equals("test");
    }

    @Test
    public void testDeserializeWithTypeNull() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._valueTypeDeserializer = null;
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance, never()).set(value);
        verify(value, never()).equals(null);
    }

    @Test
    public void testDeserializeWithTypeValueNull() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._valueTypeDeserializer = typeDeserializer;
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance, never()).set(value);
        verify(value, never()).equals(null);
    }

    @Test
    public void testDeserializeWithTypeInvalidType() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._valueTypeDeserializer = typeDeserializer;
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance, never()).set(value);
        verify(value, never()).equals(null);
    }

    @Test
    public void testDeserializeWithTypeValidType() throws IOException {
        Object instance = mock(Object.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = mock(Object.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(typeDeserializer.deserialize(jsonParser, context, String[].class)).thenReturn(new String[]{"test"});
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._valueTypeDeserializer = typeDeserializer;
        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        verify(instance).set(value);
        verify(value).isInstanceOf(String[].class);
        assertArrayEquals(new String[]{"test"}, (String[])value);
    }
}