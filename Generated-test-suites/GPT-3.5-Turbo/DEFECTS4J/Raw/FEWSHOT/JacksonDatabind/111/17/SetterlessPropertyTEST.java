package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(property);
        assertNull(property.getName());
        assertNull(property.getFullName());
        assertNull(property.getMember());
        assertNull(property.getType());
        assertNull(property.getInclusion());

        assertEquals(0, property.getAliases().length);
    }

    @Test
    public void testConstructorWithNullValueProvider() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        NullValueProvider nva = null;

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method, nva);

        assertNotNull(property);
        assertNull(property.getName());
        assertNull(property.getFullName());
        assertNull(property.getMember());
        assertNull(property.getType());
        assertNull(property.getInclusion());

        assertEquals(0, property.getAliases().length);
    }

    @Test
    public void testFixAccessWithMapperFeatureEnabled() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        DeserializationConfig config = null;

        property.fixAccess(config);
        // Verify that fixAccess() does not throw any exceptions
    }

    @Test
    public void testFixAccessWithMapperFeatureDisabled() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        DeserializationConfig config = null;
        // Disable the OVERRIDE_PUBLIC_ACCESS_MODIFIERS feature
        config.disable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);

        property.fixAccess(config);
        // Verify that fixAccess() does not throw any exceptions
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        Class<A> acls = null;

        Annotation annotation = property.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testDeserializeAndSetWithValueToken() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        try {
            property.deserializeAndSet(p, ctxt, instance);
            // Verify that deserializeAndSet() does not throw any exceptions
        } catch (IOException e) {
            fail("Should not throw any exceptions");
        }
    }

    @Test
    public void testDeserializeAndSetWithNullToken() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        try {
            property.deserializeAndSet(p, ctxt, instance);
            // Verify that deserializeAndSet() does not throw any exceptions
        } catch (IOException e) {
            fail("Should not throw any exceptions");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            // Verify that deserializeSetAndReturn() does not throw any exceptions
        } catch (IOException e) {
            fail("Should not throw any exceptions");
        }
    }

    @Test
    public void testSet() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;

        try {
            property.set(instance, value);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        } catch (IOException e) {
            fail("Should throw UnsupportedOperationException");
        }
    }

    @Test
    public void testSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;

        try {
            property.setAndReturn(instance, value);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        } catch (IOException e) {
            fail("Should throw UnsupportedOperationException");
        }
    }
}