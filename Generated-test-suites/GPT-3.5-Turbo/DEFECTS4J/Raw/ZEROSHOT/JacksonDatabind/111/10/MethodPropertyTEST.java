package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, methodProperty.getMember());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());

        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method.getAnnotated(), methodProperty.getMember().getAnnotated());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty src = new MethodProperty();
        NullValueProvider nva = new NullValueProvider();
        
        MethodProperty methodProperty = src.withNullProvider(nva);

        assertEquals(nva, methodProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty();
        DeserializationConfig config = new DeserializationConfig();

        methodProperty.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() {
        MethodProperty methodProperty = new MethodProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            methodProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        MethodProperty methodProperty = new MethodProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        MethodProperty methodProperty = new MethodProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            methodProperty.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        MethodProperty methodProperty = new MethodProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            Object result = methodProperty.setAndReturn(instance, value);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadResolve() {
        MethodProperty methodProperty = new MethodProperty();
        assertNotNull(methodProperty.readResolve());
    }
}