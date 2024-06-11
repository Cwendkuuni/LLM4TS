package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Test;

public class MethodPropertyTest {
    @Test
    public void testConstructor1() {
        // Initialize test data
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        // Create MethodProperty instance
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Verify the result
        assertNotNull(methodProperty);
    }

    @Test
    public void testConstructor2() {
        // Initialize test data
        MethodProperty src = null;
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;

        // Create MethodProperty instance
        MethodProperty methodProperty = new MethodProperty(src, deser, nva);

        // Verify the result
        assertNotNull(methodProperty);
    }

    @Test
    public void testConstructor3() {
        // Initialize test data
        MethodProperty src = null;
        PropertyName newName = null;

        // Create MethodProperty instance
        MethodProperty methodProperty = new MethodProperty(src, newName);

        // Verify the result
        assertNotNull(methodProperty);
    }

    @Test
    public void testConstructor4() {
        // Initialize test data
        MethodProperty src = null;
        Method m = null;

        // Create MethodProperty instance
        MethodProperty methodProperty = new MethodProperty(src, m);

        // Verify the result
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithNullProvider() {
        // Initialize test data
        MethodProperty src = null;
        NullValueProvider nva = null;

        // Create MethodProperty instance
        MethodProperty result = (MethodProperty) src.withNullProvider(nva);

        // Verify the result
        assertNotNull(result);
        assertEquals(src.getClass(), result.getClass());
        assertEquals(src.getType(), result.getType());
        assertEquals(src.getValueDeserializer(), result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        // Invoke fixAccess() method
        methodProperty.fixAccess(null);

        // Verify the result
        // No exception is thrown
    }

    @Test
    public void testGetAnnotation() {
        // Initialize test data
        Class<A> acls = null;
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        // Invoke getAnnotation() method
        A result = methodProperty.getAnnotation(acls);

        // Verify the result
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        // Invoke getMember() method
        AnnotatedMember result = methodProperty.getMember();

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSet() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        // Invoke deserializeAndSet() method
        methodProperty.deserializeAndSet(p, ctxt, instance);

        // Verify the result
        // No exception is thrown
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        // Invoke deserializeSetAndReturn() method
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testSet() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;

        // Invoke set() method
        methodProperty.set(instance, value);

        // Verify the result
        // No exception is thrown
    }

    @Test
    public void testSetAndReturn() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;

        // Invoke setAndReturn() method
        Object result = methodProperty.setAndReturn(instance, value);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testReadResolve() {
        // Initialize test data
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);

        // Invoke readResolve() method
        Object result = methodProperty.readResolve();

        // Verify the result
        assertNotEquals(methodProperty, result);
        assertEquals(methodProperty.getClass(), result.getClass());
        assertNotNull(result);
    }
}