package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();

        AnnotatedMethod method = new AnnotatedMethod();
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructorAndGetters() {
        assertNotNull(methodProperty);
        assertEquals(null, methodProperty.getAnnotation(Annotation.class));
        assertEquals(null, methodProperty.getMember());
        assertNotNull(methodProperty.withName(new PropertyName()));
        assertNotNull(methodProperty.withValueDeserializer(null));
        assertNotNull(methodProperty.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        methodProperty.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            methodProperty.deserializeAndSet(p, ctxt, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        try {
            methodProperty.set(instance, value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        try {
            methodProperty.setAndReturn(instance, value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}