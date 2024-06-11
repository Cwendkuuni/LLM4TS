package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        NullValueProvider nva = new NullValueProvider();
        FieldProperty result = fieldProperty.withNullProvider(nva);

        assertNotNull(result);
        assertEquals(result._nullProvider, nva);
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        DeserializationConfig config = new DeserializationConfig();
        fieldProperty.fixAccess(config);

        // No exception should be thrown
    }

    @Test
    public void testDeserializeAndSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        
        try {
            fieldProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        
        try {
            fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = new Object();
        Object value = new Object();

        try {
            fieldProperty.set(instance, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null);
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object instance = new Object();
        Object value = new Object();

        try {
            fieldProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}