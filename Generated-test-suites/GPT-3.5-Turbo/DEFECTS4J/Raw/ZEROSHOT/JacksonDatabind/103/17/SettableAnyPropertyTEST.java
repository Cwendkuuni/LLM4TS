package com.fasterxml.jackson.databind.deser.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @Test
    public void testGetProperty() {
        BeanProperty property = settableAnyProperty.getProperty();
        assertNull(property);
    }

    @Test
    public void testHasValueDeserializer() {
        boolean result = settableAnyProperty.hasValueDeserializer();
        assertFalse(result);
    }

    @Test
    public void testGetType() {
        JavaType type = settableAnyProperty.getType();
        assertNull(type);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = null;
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object result = settableAnyProperty.deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = null;
        Object propName = null;
        Object value = null;
        settableAnyProperty.set(instance, propName, value);
    }

    @Test
    public void testThrowAsIOE() throws Exception {
        Exception e = null;
        Object propName = null;
        Object value = null;
        settableAnyProperty._throwAsIOE(e, propName, value);
    }

    @Test
    public void testToString() {
        String result = settableAnyProperty.toString();
        assertNotNull(result);
    }
}