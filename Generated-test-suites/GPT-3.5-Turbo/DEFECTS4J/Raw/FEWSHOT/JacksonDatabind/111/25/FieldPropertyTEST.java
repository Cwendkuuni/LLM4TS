package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertNotNull(property);
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        NullValueProvider nva = new NullValueProvider();
        property.withNullProvider(nva);
    }

    @Test
    public void testWithNullProvider_duplicate() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        NullValueProvider nva = new NullValueProvider();
        property.withNullProvider(nva);
        property.withNullProvider(nva);
    }

    @Test
    public void testWithNullProvider_null() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property.withNullProvider(null);
    }

    @Test
    public void testWithNullProvider_null_nva() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        NullValueProvider nva = null;
        property.withNullProvider(nva);
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        PropertyName newName = new PropertyName();
        property.withName(newName);
    }

    @Test
    public void testReadResolve() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        FieldProperty result = property.readResolve();
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        Class<A> acls = null;
        property.getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        AnnotatedMember result = property.getMember();
        assertNotNull(result);
    }

}