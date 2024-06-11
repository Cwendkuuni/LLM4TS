package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor1() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(property);
        assertEquals(propDef, property.getDef());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }

    @Test
    public void testConstructor2() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        SetterlessProperty property = new SetterlessProperty(src, deser, nva);
        assertNotNull(property);
        assertEquals(src.getDef(), property.getDef());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), property.getContextAnnotations());
        assertEquals(src.getMember(), property.getMember());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testConstructor3() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        PropertyName newName = new PropertyName();
        SetterlessProperty property = new SetterlessProperty(src, newName);
        assertNotNull(property);
        assertEquals(newName, property.getFullName());
        assertEquals(src.getDef(), property.getDef());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), property.getContextAnnotations());
        assertEquals(src.getMember(), property.getMember());
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        PropertyName newName = new PropertyName();
        SetterlessProperty property = (SetterlessProperty) src.withName(newName);
        assertNotNull(property);
        assertEquals(newName, property.getFullName());
        assertEquals(src.getDef(), property.getDef());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), property.getContextAnnotations());
        assertEquals(src.getMember(), property.getMember());
    }

    @Test
    public void testWithValueDeserializer() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SetterlessProperty property = (SetterlessProperty) src.withValueDeserializer(deser);
        assertNotNull(property);
        assertEquals(src.getDef(), property.getDef());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), property.getContextAnnotations());
        assertEquals(src.getMember(), property.getMember());
        assertEquals(deser, property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        NullValueProvider nva = new NullValueProvider();
        SetterlessProperty property = (SetterlessProperty) src.withNullProvider(nva);
        assertNotNull(property);
        assertEquals(src.getDef(), property.getDef());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getTypeDeserializer(), property.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), property.getContextAnnotations());
        assertEquals(src.getMember(), property.getMember());
        assertNull(property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        DeserializationConfig config = new DeserializationConfig();
        config.enable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Class<A> acls = A.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(instance, result);
    }
}