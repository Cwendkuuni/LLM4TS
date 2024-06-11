package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;


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

public class FieldPropertyTest {
    @Test
    public void testConstructor() {
        String propertyName = "fieldProperty";
        JavaType type = new JavaType(PropertyType.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;

        FieldProperty fieldProperty = new FieldProperty(propertyName, type, typeDeser, contextAnnotations, field);

        assertEquals(propertyName, fieldProperty.getPropertyName());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getField());
    }

    @Test
    public void testFixAccess() {
        FieldProperty fieldProperty = new FieldProperty();

        MockDeserializationConfig config = new MockDeserializationConfig();
        fieldProperty.fixAccess(config);

        assertTrue(config.checkAndFixAccessCalled);
    }

    @Test
    public void testGetAnnotation() {
        FieldProperty fieldProperty = new FieldProperty();
        Class<JsonProperty> annotationClass = JsonProperty.class;

        Annotation annotation = fieldProperty.getAnnotation(annotationClass);

        assertNotNull(annotation);
        assertEquals(annotationClass, annotation.annotationType());
    }

    @Test
    public void testGetMember() {
        FieldProperty fieldProperty = new FieldProperty();

        AnnotatedMember member = fieldProperty.getMember();

        assertNotNull(member);
        assertEquals(AnnotatedField.class, member.getClass());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        FieldProperty fieldProperty = new FieldProperty();

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        // Test code here
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        FieldProperty fieldProperty = new FieldProperty();

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
        // Test code here
    }

    @Test
    public void testSet() throws IOException {
        FieldProperty fieldProperty = new FieldProperty();

        Object instance = new Object();
        Object value = new Object();

        fieldProperty.set(instance, value);

        // Test code here
    }

    @Test
    public void testSetAndReturn() throws IOException {
        FieldProperty fieldProperty = new FieldProperty();

        Object instance = new Object();
        Object value = new Object();

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        // Test code here
    }
}