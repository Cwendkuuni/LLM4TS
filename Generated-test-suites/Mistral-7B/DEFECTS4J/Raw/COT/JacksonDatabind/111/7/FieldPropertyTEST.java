package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        assertNotNull(fieldProperty);
    }

    @Test
    public void testConstructorWithArguments() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertNotNull(fieldProperty);
    }

    @Test
    public void testWithName() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        PropertyName newName = PropertyName.construct("test");
        FieldProperty result = fieldProperty.withName(newName);

        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;

        FieldProperty result = fieldProperty.withValueDeserializer(deser).withNullProvider(nva);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        NullValueProvider nva = null;

        FieldProperty result = fieldProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        fieldProperty.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        Annotation annotation = new Object() {};
        AnnotatedField annotatedField = new AnnotatedField(null, null, null, null, null, null, null, null, null, annotation);

        assertEquals(annotation, fieldProperty.getAnnotation(annotation.getClass()));
        assertEquals(null, fieldProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        AnnotatedField annotatedField = new AnnotatedField(null, null, null, null, null, null, null, null, null, null);

        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object() {};
        JsonParser parser = null;
        DeserializationContext context = null;
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        fieldProperty.deserializeAndSet(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object() {};
        JsonParser parser = null;
        DeserializationContext context = null;
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        Object result = fieldProperty.deserializeSetAndReturn(parser, context, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object() {};
        Object value = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        fieldProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object() {};
        Object value = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        Object result = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithIOException() throws IOException {
        Object instance = new Object() {};
        JsonParser parser = null;
        DeserializationContext context = null;
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);

        fieldProperty.deserializeAndSet(parser, context, instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullField() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;

        try {
            new FieldProperty(propDef, type, typeDeser, contextAnnotations, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

}