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
        assertEquals(typeDeser, methodProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod())
                .withNullProvider(nva);

        assertEquals(nva, methodProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        
        methodProperty.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        methodProperty.deserializeAndSet(p, ctxt, instance);

        // verify the result
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        // verify the result
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

        Object instance = new Object();
        Object value = new Object();

        methodProperty.set(instance, value);

        // verify the result
    }

    @Test
    public void testSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

        Object instance = new Object();
        Object value = new Object();

        Object result = methodProperty.setAndReturn(instance, value);

        // verify the result
        assertEquals(instance, result);
    }
}