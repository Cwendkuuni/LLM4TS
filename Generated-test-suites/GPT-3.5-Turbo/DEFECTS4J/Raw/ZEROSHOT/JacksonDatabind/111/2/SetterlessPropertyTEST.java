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
        assertEquals(propDef, property.getPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getAnnotated());
    }

    @Test
    public void testConstructor2() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        SetterlessProperty property = new SetterlessProperty(src, deser, nva);

        assertNotNull(property);
        assertEquals(src.getAnnotated(), property.getAnnotated());
        assertEquals(src.getGetter(), property.getGetter());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testConstructor3() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        PropertyName newName = new PropertyName("newName");

        SetterlessProperty property = new SetterlessProperty(src, newName);

        assertNotNull(property);
        assertEquals(src.getAnnotated(), property.getAnnotated());
        assertEquals(src.getGetter(), property.getGetter());
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithName() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        PropertyName newName = new PropertyName("newName");

        SetterlessProperty property = (SetterlessProperty)src.withName(newName);

        assertNotNull(property);
        assertEquals(src.getAnnotated(), property.getAnnotated());
        assertEquals(src.getGetter(), property.getGetter());
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        SetterlessProperty property = (SetterlessProperty)src.withValueDeserializer(deser);

        assertNotNull(property);
        assertEquals(src.getAnnotated(), property.getAnnotated());
        assertEquals(src.getGetter(), property.getGetter());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src.getNullValueProvider(), property.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty src = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        NullValueProvider nva = new NullValueProvider();

        SetterlessProperty property = (SetterlessProperty)src.withNullProvider(nva);

        assertNotNull(property);
        assertEquals(src.getAnnotated(), property.getAnnotated());
        assertEquals(src.getGetter(), property.getGetter());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);

        // Test implementation here
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Class<A> acls = A.class;

        A annotation = property.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

        AnnotatedMember member = property.getMember();

        assertEquals(property.getAnnotated(), member);
    }

    @Test
    public void testDeserializeAndSet() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        // Test implementation here
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        property.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        Object instance = new Object();
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }
}