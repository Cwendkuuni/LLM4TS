package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public class FieldPropertyTest {
    @Test
    public void testConstructorAndFields() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(field, property.getMember());
    }

    @Test
    public void testDeserializationAndSetValue() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(property._nullProvider.getNullValue(ctxt)).thenReturn(value);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testDeserializationSetAndReturn() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(property._nullProvider.getNullValue(ctxt)).thenReturn(value);
        
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        property.set(instance, value);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        property.setAndReturn(instance, value);
        
        verify(property._field).set(instance, value);
    }
    
    @Test
    public void testFixAccess() throws Exception {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        property.fixAccess(config);
        
        verify(config, times(1)).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verify(property._field, times(1)).setAccessible(true);
    }
    
    @Test
    public void testGetAnnotation() {
        FieldProperty property = new FieldProperty();
        AnnotatedField field = mock(AnnotatedField.class);
        
        property._annotated = field;
        
        Class<A> acls = mock(Class.class);
        property.getAnnotation(acls);
        
        verify(field, times(1)).getAnnotation(acls);
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        FieldProperty property = new FieldProperty();
        
        SettableBeanProperty result = property.withNullProvider(nva);
        
        assertEquals(nva, result._nullProvider);
    }
    
    @Test
    public void testWithReturnValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        FieldProperty property = new FieldProperty();
        
        SettableBeanProperty result = property.withValueDeserializer(deser);
        
        assertEquals(deser, result._valueDeserializer);
    }

    @Test
    public void testWithPropertyName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty property = new FieldProperty();
        
        SettableBeanProperty result = property.withName(newName);
        
        assertEquals(newName, result._fullName);
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(property._nullProvider.getNullValue(ctxt)).thenReturn(value);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(property._nullProvider.getNullValue(ctxt)).thenReturn(value);
        
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        property.set(instance, value);
        
        verify(property._field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        property.setAndReturn(instance, value);
        
        verify(property._field).set(instance, value);
    }
    
    @Test
    public void testReadResolve() throws Exception {
        FieldProperty property = new FieldProperty();
        Field field = property.getClass().getDeclaredField("_field");
        field.setAccessible(true);
        field.set(property, mock(Field.class));
        
        FieldProperty result = property.readResolve();
        
        assertEquals(property._field, result._field);
    }
}