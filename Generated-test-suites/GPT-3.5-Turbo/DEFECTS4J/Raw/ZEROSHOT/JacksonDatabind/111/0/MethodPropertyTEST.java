package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null; // pass appropriate value here
        JavaType type = null; // pass appropriate value here
        TypeDeserializer typeDeser = null; // pass appropriate value here
        Annotations contextAnnotations = null; // pass appropriate value here
        AnnotatedMethod method = null; // pass appropriate value here

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // add assertion statements here
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty src = null; // pass appropriate value here
        NullValueProvider nva = null; // pass appropriate value here

        MethodProperty methodProperty = new MethodProperty(src, nva);

        // add assertion statements here
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithNewName() {
        MethodProperty src = null; // pass appropriate value here
        PropertyName newName = null; // pass appropriate value here

        MethodProperty methodProperty = new MethodProperty(src, newName);

        // add assertion statements here
        assertNotNull(methodProperty);
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty();

        DeserializationConfig config = null; // pass appropriate value here

        methodProperty.fixAccess(config);

        // add assertion statements here
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty methodProperty = new MethodProperty();

        Class<A> acls = null; // pass appropriate value here

        A result = methodProperty.getAnnotation(acls);

        // add assertion statements here
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        MethodProperty methodProperty = new MethodProperty();

        AnnotatedMember result = methodProperty.getMember();

        // add assertion statements here
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        MethodProperty methodProperty = new MethodProperty();

        JsonParser p = null; // pass appropriate value here
        DeserializationContext ctxt = null; // pass appropriate value here
        Object instance = null; // pass appropriate value here

        methodProperty.deserializeAndSet(p, ctxt, instance);

        // add assertion statements here
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        MethodProperty methodProperty = new MethodProperty();

        JsonParser p = null; // pass appropriate value here
        DeserializationContext ctxt = null; // pass appropriate value here
        Object instance = null; // pass appropriate value here

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        // add assertion statements here
        assertNull(result);
    }

    @Test
    public void testSet() throws Exception {
        MethodProperty methodProperty = new MethodProperty();

        Object instance = null; // pass appropriate value here
        Object value = null; // pass appropriate value here

        methodProperty.set(instance, value);

        // add assertion statements here
    }

    @Test
    public void testSetAndReturn() throws Exception {
        MethodProperty methodProperty = new MethodProperty();

        Object instance = null; // pass appropriate value here
        Object value = null; // pass appropriate value here

        Object result = methodProperty.setAndReturn(instance, value);

        // add assertion statements here
        assertNull(result);
    }

    @Test
    public void testReadResolve() {
        MethodProperty methodProperty = new MethodProperty();

        Object result = methodProperty.readResolve();

        // add assertion statements here
        assertNotNull(result);
    }
}