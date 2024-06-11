package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {
    private MethodProperty methodProperty;
    
    @Before
    public void setUp() {
        BeanPropertyDefinition beanPropertyDef = new BeanPropertyDefinition(null, null, null);
        JavaType type = new JavaType(null, null, 0, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null);
        Annotations contextAnnotations = new Annotations(null);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(null, null, null, null);
        methodProperty = new MethodProperty(beanPropertyDef, type, typeDeser, contextAnnotations, annotatedMethod);
    }
    
    @Test
    public void testMethodPropertyConstructor() {
        assertNotNull(methodProperty);
    }
    
    @Test
    public void testMethodPropertyWithNullProvider() {
        NullValueProvider nva = new NullValueProvider(null, null);
        SettableBeanProperty property = methodProperty.withNullProvider(nva);
        assertNotNull(property);
    }
    
    @Test
    public void testMethodPropertyWithAnnotation() {
        Annotation annotation = methodProperty.getAnnotation(Annotation.class);
        assertNull(annotation);
    }
    
    @Test
    public void testMethodPropertyGetMember() {
        AnnotatedMember member = methodProperty.getMember();
        assertNotNull(member);
    }
    
    @Test
    public void testMethodPropertyFixAccess() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        methodProperty.fixAccess(config);
    }
    
    @Test
    public void testMethodPropertyWithNewName() {
        PropertyName newName = new PropertyName(null, null, null);
        SettableBeanProperty property = methodProperty.withName(newName);
        assertNotNull(property);
    }
    
    @Test
    public void testMethodPropertyWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>(null);
        SettableBeanProperty property = methodProperty.withValueDeserializer(deser);
        assertNotNull(property);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNotNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        methodProperty.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = methodProperty.setAndReturn(instance, value);
        assertNotNull(result);
    }
}