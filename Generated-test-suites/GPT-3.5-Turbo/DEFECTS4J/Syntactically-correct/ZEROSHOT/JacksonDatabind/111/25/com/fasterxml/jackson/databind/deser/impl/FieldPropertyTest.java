package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {
    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertNotNull(fieldProperty);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        fieldProperty.fixAccess(config);
        // assert something
    }

    @Test
    public void testGetAnnotation() {
        fieldProperty.getAnnotation(SomeAnnotation.class);
        // assert something
    }

    @Test
    public void testGetMember() {
        AnnotatedMember expected = null;
        AnnotatedMember result = fieldProperty.getMember();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            fieldProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object value = null;
        try {
            fieldProperty.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = null;
        Object value = null;
        try {
            fieldProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}