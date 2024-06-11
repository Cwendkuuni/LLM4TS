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
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        NullValueProvider nva = new NullValueProvider();
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        assertNotNull(newMethodProperty);
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        DeserializationConfig config = new DeserializationConfig();
        methodProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        Class<A> acls = A.class;
        A annotation = methodProperty.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testDeserializeAndSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
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
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
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
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadResolve() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
        Method m = new Method();
        MethodProperty newMethodProperty = methodProperty.readResolve();
        assertNotNull(newMethodProperty);
    }
}