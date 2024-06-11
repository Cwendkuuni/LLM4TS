package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        assertNotNull(fieldProperty);
    }

    @Test
    public void testCopyConstructor() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        FieldProperty copy = new FieldProperty(fieldProperty);
        assertEquals(fieldProperty, copy);
    }

    @Test
    public void testNameConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        PropertyName newName = new PropertyName("newName");
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        FieldProperty copy = new FieldProperty(fieldProperty, newName);
        assertEquals(newName, copy.getName());
        assertEquals(fieldProperty, copy);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = new StdScalarTypeDeserializer(String.class);
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        JsonParser parser = createContext().parser();
        parser.nextToken(); // [START]
        String value = "testValue";
        parser.nextText(value);

        fieldProperty.deserializeAndSet(parser, createContext(), instance);
        assertEquals(value, fieldProperty.getMember().get(instance));

        parser.nextToken(); // [END]
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = new StdScalarTypeDeserializer(String.class);
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        JsonParser parser = createContext().parser();
        parser.nextToken(); // [START]
        String value = "testValue";
        parser.nextText(value);

        Object result = fieldProperty.deserializeSetAndReturn(parser, createContext(), instance);
        assertEquals(instance, result);
        assertEquals(value, fieldProperty.getMember().get(instance));

        parser.nextToken(); // [END]
    }

    @Test
    public void testSet() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        String value = "testValue";

        fieldProperty.set(instance, value);
        assertEquals(value, fieldProperty.getMember().get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        String value = "testValue";

        Object result = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
        assertEquals(value, fieldProperty.getMember().get(instance));
    }

    @Test
    public void testReadResolve() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "@JsonProperty", JsonProperty.class, String.class, null);
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField annotatedField = new AnnotatedField(
                "@TestAnnotatedField", String.class, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        FieldProperty copy = new FieldProperty(fieldProperty);
        Object result = copy.readResolve();
        assertEquals(copy, result);
    }
}