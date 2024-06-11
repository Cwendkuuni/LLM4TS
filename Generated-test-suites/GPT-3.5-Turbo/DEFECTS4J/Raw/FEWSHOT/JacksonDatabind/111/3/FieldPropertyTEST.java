package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotatedField;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {
    @Test
    public void testConstructor1() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testConstructor2() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        
        FieldProperty src = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty fieldProperty = new FieldProperty(src, deser, nva);
        
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
        assertEquals(deser, fieldProperty.getValueDeserializer());
        assertEquals(nva, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testConstructor3() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        PropertyName newName = mock(PropertyName.class);
        
        FieldProperty src = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty fieldProperty = new FieldProperty(src, newName);
        
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(newName, fieldProperty.getFullName());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testConstructor4() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        
        FieldProperty src = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty fieldProperty = new FieldProperty(src, deser, nva);
        
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
        assertEquals(deser, fieldProperty.getValueDeserializer());
        assertEquals(nva, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        
        fieldProperty.fixAccess(config);
        
        verify(field).setAccessible(true);
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testDeserializeAndSet() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        try {
            doNothing().when(field).set(any(), any());
            
            FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
            
            fieldProperty.deserializeAndSet(p, ctxt, instance);
            
            verify(field).set(eq(instance), eq(value));
            verifyNoMoreInteractions(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        try {
            doNothing().when(field).set(any(), any());
            
            FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
            
            fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
            
            verify(field).set(eq(instance), eq(value));
            verifyNoMoreInteractions(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        try {
            doNothing().when(field).set(any(), any());
            
            FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
            
            fieldProperty.set(instance, value);
            
            verify(field).set(eq(instance), eq(value));
            verifyNoMoreInteractions(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        try {
            doNothing().when(field).set(any(), any());
            
            FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
            
            fieldProperty.setAndReturn(instance, value);
            
            verify(field).set(eq(instance), eq(value));
            verifyNoMoreInteractions(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadResolve() {
        Field field = mock(Field.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        FieldProperty fieldProperty1 = new FieldProperty(null, null, null, null, annotatedField);
        FieldProperty fieldProperty2 = null;
        
        fieldProperty2 = (FieldProperty)fieldProperty1.readResolve();
        
        assertEquals(fieldProperty1.getPropertyDefinition(), fieldProperty2.getPropertyDefinition());
        assertEquals(fieldProperty1.getType(), fieldProperty2.getType());
        assertEquals(fieldProperty1.getValueTypeDeserializer(), fieldProperty2.getValueTypeDeserializer());
        assertEquals(fieldProperty1.getContextAnnotations(), fieldProperty2.getContextAnnotations());
        assertEquals(fieldProperty1.getMember(), fieldProperty2.getMember());
    }
}