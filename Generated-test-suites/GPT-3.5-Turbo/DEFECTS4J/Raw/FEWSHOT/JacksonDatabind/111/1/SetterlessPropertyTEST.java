package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class SetterlessPropertyTest {
    private SetterlessProperty property;

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }

    @Test
    public void testWithNullProvider() {
        property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider nva = null;

        SetterlessProperty newProperty = property.withNullProvider(nva);

        assertEquals(nva, newProperty.getValueNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;

        assertThrows(Exception.class, () -> {
            property = new SetterlessProperty(null, null, null, null, null);
            property.fixAccess(config);
        });
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        assertThrows(Exception.class, () -> {
            property = new SetterlessProperty(null, null, null, null, null);
            property.deserializeAndSet(p, ctxt, instance);
        });
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        assertThrows(Exception.class, () -> {
            property = new SetterlessProperty(null, null, null, null, null);
            property.deserializeSetAndReturn(p, ctxt, instance);
        });
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;

        assertThrows(UnsupportedOperationException.class, () -> {
            property = new SetterlessProperty(null, null, null, null, null);
            property.set(instance, value);
        });
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;

        assertThrows(UnsupportedOperationException.class, () -> {
            property = new SetterlessProperty(null, null, null, null, null);
            property.setAndReturn(instance, value);
        });
    }
}