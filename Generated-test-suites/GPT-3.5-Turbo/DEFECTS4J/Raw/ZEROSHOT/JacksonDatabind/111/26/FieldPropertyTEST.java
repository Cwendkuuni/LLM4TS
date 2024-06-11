package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNotNull(property);
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(field, property.getMember());
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        FieldProperty property = new FieldProperty(null, null, null, null);
        FieldProperty result = property.withNullProvider(nva);
        
        assertNotSame(property, result);
        assertEquals(nva, result.getValueNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        FieldProperty property = new FieldProperty(null, null, null, null);
        property.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithValue() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithValue() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test(expected = IOException.class)
    public void testSetWithException() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        FieldProperty property = new FieldProperty(null, null, null, null);
        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

}