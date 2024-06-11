package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        field.setAnnotated(new FieldProperty());

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();

        SettableBeanProperty prop = fieldProperty.withNullProvider(nva);

        assertNotNull(prop);
        assertEquals(FieldProperty.class, prop.getClass());
        assertNull(prop.getName());
        assertNull(prop.getValueDeserializer());
        assertTrue(prop.hasNullValueProvider());
    }

    @Test
    public void testWithNullValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        SettableBeanProperty prop = fieldProperty.withValueDeserializer(deser);

        assertNotNull(prop);
        assertEquals(FieldProperty.class, prop.getClass());
        assertNull(prop.getName());
        assertNotNull(prop.getValueDeserializer());
        assertNotNull(prop.getValueDeserializer());
        assertEquals(nva, prop.getValueNullProvider(null));
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");

        SettableBeanProperty prop = fieldProperty.withName(newName);

        assertNotNull(prop);
        assertEquals(FieldProperty.class, prop.getClass());
        assertEquals(newName, prop.getName());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();

        fieldProperty.fixAccess(config);

        // Verify that no exception was thrown
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        // Verify that no exception was thrown
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.set(instance, value);

        // Verify that no exception was thrown
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation result = fieldProperty.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = fieldProperty.getMember();

        assertNotNull(result);
    }

    @Test
    public void testReadResolve() {
        Object result = fieldProperty.readResolve();

        assertNotNull(result);
        assertEquals(FieldProperty.class, result.getClass());
    }
}