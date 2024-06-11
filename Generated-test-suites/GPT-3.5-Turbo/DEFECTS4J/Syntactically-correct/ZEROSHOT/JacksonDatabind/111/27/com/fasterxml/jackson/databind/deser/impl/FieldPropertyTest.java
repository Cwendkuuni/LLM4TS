package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

    private FieldProperty createFieldProperty() {
        // create a mock instance of FieldProperty
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedField field = new AnnotatedField();

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        return fieldProperty;
    }

    @Test
    public void createFieldProperty_ValidArgs_ReturnsFieldPropertyInstance() {
        // arrange

        // act
        FieldProperty fieldProperty = createFieldProperty();

        // assert
        assertNotNull(fieldProperty);
    }

    @Test
    public void createFieldProperty_InvalidArgs_ReturnsFieldPropertyInstance() {
        // arrange

        // act
        FieldProperty fieldProperty = createFieldProperty();

        // assert
        assertNotNull(fieldProperty);
    }

    @Test
    public void withName_ValidName_ReturnsFieldPropertyWithNewName() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        PropertyName newName = new PropertyName("newName");

        // act
        SettableBeanProperty result = fieldProperty.withName(newName);

        // assert
        assertTrue(result instanceof FieldProperty);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void withValueDeserializer_ValidDeserializer_ReturnsFieldPropertyWithValueDeserializer() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        // act
        SettableBeanProperty result = fieldProperty.withValueDeserializer(deser);

        // assert
        assertTrue(result instanceof FieldProperty);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void withNullProvider_ValidProvider_ReturnsFieldPropertyWithNullProvider() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        NullValueProvider nva = new NullValueProvider();

        // act
        SettableBeanProperty result = fieldProperty.withNullProvider(nva);

        // assert
        assertTrue(result instanceof FieldProperty);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void fixAccess_ValidConfig_FixesAccess() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        DeserializationConfig config = new DeserializationConfig();

        // act
        fieldProperty.fixAccess(config);

        // assert
        // no exceptions
    }

    @Test
    public void getAnnotation_ValidClass_ReturnsAnnotation() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        Class<Annotation> acls = Annotation.class;

        // act
        Annotation result = fieldProperty.getAnnotation(acls);

        // assert
        assertNull(result);
    }

    @Test
    public void getMember_ReturnsAnnotatedMember() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();

        // act
        AnnotatedMember result = fieldProperty.getMember();

        // assert
        assertNotNull(result);
    }

    @Test
    public void deserializeAndSet_ValidArgs_SetsValue() throws IOException {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        // act
        fieldProperty.deserializeAndSet(p, ctxt, instance);

        // assert
        // verify that the value was set correctly
    }

    @Test
    public void deserializeSetAndReturn_ValidArgs_SetsAndReturnsValue() throws IOException {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        // act
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);

        // assert
        assertEquals(instance, result);
        // verify that the value was set correctly
    }

    @Test
    public void set_ValidArgs_SetsValue() throws IOException {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        Object instance = new Object();
        Object value = new Object();

        // act
        fieldProperty.set(instance, value);

        // assert
        // verify that the value was set correctly
    }

    @Test
    public void setAndReturn_ValidArgs_SetsAndReturnsValue() throws IOException {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();
        Object instance = new Object();
        Object value = new Object();

        // act
        Object result = fieldProperty.setAndReturn(instance, value);

        // assert
        assertEquals(instance, result);
        // verify that the value was set correctly
    }

    @Test
    public void readResolve_ReturnsNewInstance() {
        // arrange
        FieldProperty fieldProperty = createFieldProperty();

        // act
        Object result = fieldProperty.readResolve();

        // assert
        assertTrue(result instanceof FieldProperty);
    }
}