package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNotNull(property);
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(field, property.getMember());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();

        NullValueProvider nva = new NullValueProvider();

        FieldProperty src = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        FieldProperty property = src.withNullProvider(nva);

        assertNotNull(property);
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(field, property.getMember());
        assertNull(property.getValueDeserializer());
        assertEquals(nva, property.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();

        DeserializationConfig config = new DeserializationConfig();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        property.fixAccess(config);

        // Verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        NullValueProvider nva = new NullValueProvider();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property = property.withValueDeserializer(deser);

        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        NullValueProvider nva = new NullValueProvider();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property = property.withValueDeserializer(deser);

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        NullValueProvider nva = new NullValueProvider();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        NullValueProvider nva = new NullValueProvider();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testReadResolve() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        NullValueProvider nva = new NullValueProvider();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        FieldProperty src = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty property = src.readResolve();

        assertNotNull(property);
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(field, property.getMember());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }
}