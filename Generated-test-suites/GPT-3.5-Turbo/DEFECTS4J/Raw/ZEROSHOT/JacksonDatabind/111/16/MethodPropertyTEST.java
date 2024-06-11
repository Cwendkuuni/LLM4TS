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
        MethodProperty src = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        NullValueProvider nva = new NullValueProvider();
        MethodProperty methodProperty = src.withNullProvider(nva);
        assertNotNull(methodProperty);
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty src = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        DeserializationConfig config = new DeserializationConfig();
        src.fixAccess(config);
        // Assert access is fixed
    }

    // Add more test methods for other methods in MethodProperty class

}