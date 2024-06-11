package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testWithNullValueProvider() {
        NullValueProvider nva = new NullValueProvider();

        FieldProperty withNullProviderProperty = fieldProperty.withNullProvider(nva);

        Assert.assertEquals(withNullProviderProperty._valueDeserializer, fieldProperty._valueDeserializer);
        Assert.assertEquals(nva, withNullProviderProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        fieldProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Assert.assertNull(fieldProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        Assert.assertEquals(_annotated, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        fieldProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.setAndReturn(instance, value);
    }

}