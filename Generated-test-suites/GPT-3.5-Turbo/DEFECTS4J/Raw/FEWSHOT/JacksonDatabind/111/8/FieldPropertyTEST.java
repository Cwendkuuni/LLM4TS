package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        Basic contextAnnotations = mock(Basic.class);
        AnnotatedField field = mock(AnnotatedField.class);

        FieldProperty property = new FieldProperty(propDef, type, null, null, field);

        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property.getTypeDeserializer());
    }

    @Test
    public void testWithMethods() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        PropertyName newName = mock(PropertyName.class);
        AnnotatedField field = mock(AnnotatedField.class);

        FieldProperty property = new FieldProperty(propDef, type, null, null, field);
        FieldProperty withName = property.withName(newName);
        FieldProperty withValueDeserializer = property.withValueDeserializer(deser);
        FieldProperty withNullProvider = property.withNullProvider(nva);

        assertEquals(newName, withName.getFullName());
        assertEquals(deser, withValueDeserializer.getValueDeserializer());
        assertEquals(nva, withNullProvider.getNullProvider());
    }

    @Test
    public void testSetAndGet(){
        
        JsonParser p= mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        FieldProperty fieldProperty = mock(FieldProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(p.hasToken(any()))
            .thenReturn(false);

        when(fieldProperty.deserializeAndSet(p, ctxt, instance))
            .thenReturn(value);

        when(fieldProperty.deserializeSetAndReturn(p, ctxt, instance))
            .thenReturn(value);

        try {
            fieldProperty.set(instance, value);
            fieldProperty.setAndReturn(instance, value);

        } catch (Exception e) {
            //Handle Exception
        }

        verify(p, times(1)).hasToken(any());

        verify(fieldProperty, times(1)).deserializeAndSet(p, ctxt, instance);
        verify(fieldProperty, times(1)).deserializeSetAndReturn(p, ctxt, instance);

    }
}