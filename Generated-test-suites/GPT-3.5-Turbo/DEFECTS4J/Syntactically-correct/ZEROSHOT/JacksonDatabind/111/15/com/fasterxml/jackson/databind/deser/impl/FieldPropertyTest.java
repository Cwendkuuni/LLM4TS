package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty fp = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertNotNull(fp);
    }

    @Test
    public void testWithNullProvider() {
        FieldProperty fp = new FieldProperty();
        NullValueProvider nva = new NullValueProvider();
        FieldProperty newFp = fp.withNullProvider(nva);
        assertNotNull(newFp);
    }

    @Test
    public void testFixAccess() {
        FieldProperty fp = new FieldProperty();
        DeserializationConfig config = new DeserializationConfig();
        fp.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        FieldProperty fp = new FieldProperty();
        Class<A> acls = A.class;
        A annotation = fp.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        FieldProperty fp = new FieldProperty();
        AnnotatedMember member = fp.getMember();
        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() {
        FieldProperty fp = new FieldProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        fp.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        FieldProperty fp = new FieldProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = fp.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() {
        FieldProperty fp = new FieldProperty();
        Object instance = new Object();
        Object value = new Object();
        fp.set(instance, value);
    }

    @Test
    public void testSetAndReturn() {
        FieldProperty fp = new FieldProperty();
        Object instance = new Object();
        Object value = new Object();
        Object result = fp.setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testReadResolve() {
        FieldProperty fp = new FieldProperty();
        Object result = fp.readResolve();
        assertNotNull(result);
    }
}