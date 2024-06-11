package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

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

/**
 * This concrete sub-class implements property that is set
 * directly assigning to a Field.
 */
public final class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(new Field("testField"), new Annotation[]{}, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertEquals(propDef, fieldProperty.getBeanPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertEquals(field, fieldProperty._annotated);
        assertEquals(field.getAnnotated(), fieldProperty._field);
        assertFalse(fieldProperty._skipNulls);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        FieldProperty property = new FieldProperty(new Field("testField"), null, null, null, null);
        SettableBeanProperty result = property.withName(newName);
        assertEquals(newName, result.getName());
        assertEquals(property._annotated, result._annotated);
        assertEquals(property._field, result._field);
        assertEquals(property._skipNulls, result._skipNulls);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();
        Field field = new Field("testField");
        Object instance = new Object();
        AnnotatedField annotatedField = new AnnotatedField(field, new Annotation[0], null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test with null
        if (fieldProperty._skipNulls) {
            assertNull(fieldProperty.deserializeAndSet(parser, ctxt, instance));
            return;
        }

        // Test with valueDeserializer
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, deserializer, null);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.findContextualValueDeserializer(fieldProperty.getType(), null)).thenReturn(deserializer);
        Object result = fieldProperty.deserializeAndSet(parser, ctxt, instance);
        assertSame(value, result);

        // Test with valueTypeDeserializer
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(deserializer.deserializeWithType(parser, ctxt, typeDeserializer)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, null, typeDeserializer);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.findContextualValueDeserializer(fieldProperty.getType(), null)).thenReturn(deserializer);
        result = fieldProperty.deserializeAndSet(parser, ctxt, instance);
        assertSame(value, result);

        // Test with null value
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        result = fieldProperty.deserializeAndSet(parser, ctxt, instance);
        assertNull(result);

        // Test with JsonToken.START_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        result = fieldProperty.deserializeAndSet(parser, ctxt, instance);
        assertNull(result);

        // Test with JsonToken.END_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.END_OBJECT);
        result = fieldProperty.deserializeAndSet(parser, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();
        Field field = new Field("testField");
        Object instance = new Object();
        AnnotatedField annotatedField = new AnnotatedField(field, new Annotation[0], null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test with null
        if (fieldProperty._skipNulls) {
            assertEquals(instance, fieldProperty.deserializeSetAndReturn(parser, ctxt, instance));
            return;
        }

        // Test with valueDeserializer
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, deserializer, null);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.findContextualValueDeserializer(fieldProperty.getType(), null)).thenReturn(deserializer);
        Object result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);
        assertEquals(value, fieldProperty._field.get(instance));

        // Test with valueTypeDeserializer
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(deserializer.deserializeWithType(parser, ctxt, typeDeserializer)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, null, typeDeserializer);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.findContextualValueDeserializer(fieldProperty.getType(), null)).thenReturn(deserializer);
        result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);
        assertEquals(value, fieldProperty._field.get(instance));

        // Test with null value
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);

        // Test with JsonToken.START_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);

        // Test with JsonToken.END_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.END_OBJECT);
        result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        Field field = new Field("testField");
        Object instance = new Object();
        AnnotatedField annotatedField = new AnnotatedField(field, new Annotation[0], null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test with null value
        if (fieldProperty._skipNulls) {
            return;
        }

        // Test with nullProvider
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(ctxt)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, nullValueProvider);
        try {
            fieldProperty.set(instance, null);
            fail();
        } catch (Exception e) {
            // Expected
        }

        // Test with value
        try {
            fieldProperty.set(instance, value);
            assertEquals(value, fieldProperty._field.get(instance));
        } catch (Exception e) {
            fail();
        }

        // Test with JsonToken.START_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        try {
            fieldProperty.set(instance, value);
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test with JsonToken.END_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.END_OBJECT);
        try {
            fieldProperty.set(instance, value);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        Field field = new Field("testField");
        Object instance = new Object();
        AnnotatedField annotatedField = new AnnotatedField(field, new Annotation[0], null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test with null value
        if (fieldProperty._skipNulls) {
            assertEquals(instance, fieldProperty.setAndReturn(instance, null));
            return;
        }

        // Test with nullProvider
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(ctxt)).thenReturn(value);
        fieldProperty = new FieldProperty(fieldProperty, nullValueProvider);
        try {
            fieldProperty.setAndReturn(instance, null);
            fail();
        } catch (Exception e) {
            // Expected
        }

        // Test with value
        try {
            Object result = fieldProperty.setAndReturn(instance, value);
            assertEquals(instance, result);
            assertEquals(value, fieldProperty._field.get(instance));
        } catch (Exception e) {
            fail();
        }

        // Test with JsonToken.START_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        try {
            fieldProperty.setAndReturn(instance, value);
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test with JsonToken.END_OBJECT
        when(parser.getValueAsToken()).thenReturn(JsonToken.END_OBJECT);
        try {
            fieldProperty.setAndReturn(instance, value);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}