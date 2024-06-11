package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertSame(propDef, methodProperty.getDefinition());
        assertSame(type, methodProperty.getType());
        assertSame(typeDeser, methodProperty.getTypeDeserializer());
        assertSame(contextAnnotations, methodProperty.getContextAnnotations());
        assertSame(method, methodProperty.getMember());
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        NullValueProvider nva = new NullValueProvider(null, null);

        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withNullProvider(nva);

        assertSame(nva, newMethodProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = new JsonParser();

        try {
            methodProperty.deserializeAndSet(p, null, null);
            fail("An IOException should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = new JsonParser();

        try {
            methodProperty.deserializeSetAndReturn(p, null, null);
            fail("An IOException should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        try {
            methodProperty.set(null, null);
            fail("An IOException should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null, false, false);
        JavaType type = new JavaType(null, null, null, null, null, false, false);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, null, null, false, false);
        Annotations contextAnnotations = new Annotations(null, null);
        AnnotatedMethod method = new AnnotatedMethod(null, null);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        try {
            methodProperty.setAndReturn(null, null);
            fail("An IOException should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
}