package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.annotation.NullValue;

public class FieldPropertyTest {
    @Test
    public void testConstructorWithNullValueProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertNull(fieldProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithDeserializerANullValueProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        fieldProperty = fieldProperty.withName(new PropertyName());
        fieldProperty = fieldProperty.withValueDeserializer(deser);
        fieldProperty = fieldProperty.withNullProvider(nva);
        
        assertEquals(nva, fieldProperty.getNullValueProvider());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        JsonParser p = new JsonParser();
        Object instance = new Object();
        fieldProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        JsonParser p = new JsonParser();
        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        fieldProperty.set(ctxt, ctxt);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        fieldProperty.setAndReturn(ctxt, ctxt);
    }
}