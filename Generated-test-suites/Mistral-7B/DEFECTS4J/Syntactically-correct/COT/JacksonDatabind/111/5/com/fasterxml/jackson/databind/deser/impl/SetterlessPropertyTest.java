package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testSetterlessPropertyConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNull(setterlessProperty);
    }

    @Test
    public void testSetterlessPropertyWithName() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        PropertyName newName = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(setterlessProperty, newName);

        assertNotNull(setterlessProperty);
        assertEquals(newName, setterlessProperty.getName());
    }

    @Test
    public void testSetterlessPropertyWithValueDeserializer() {
        SetterlessProperty src = null;
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(src, deser, nva);

        assertNotNull(setterlessProperty);
        assertEquals(src, setterlessProperty.getBackingBean());
        assertEquals(deser, setterlessProperty.getValueDeserializer());
        assertEquals(nva, setterlessProperty.getNullProvider());
    }

    @Test
    public void testSetterlessPropertyFixAccess() {
        SetterlessProperty setterlessProperty = null;
        DeserializationConfig config = null;

        setterlessProperty.fixAccess(config);

        // This method doesn't return anything, so we cannot directly assert on it
    }

    @Test
    public void testSetterlessPropertyGetAnnotation() {
        SetterlessProperty setterlessProperty = null;
        Class<Annotation> acls = null;

        Annotation annotation = setterlessProperty.getAnnotation(acls);

        assertNull(annotation);
    }

    @Test
    public void testSetterlessPropertyGetMember() {
        SetterlessProperty setterlessProperty = null;
        AnnotatedMember member = null;

        assertEquals(setterlessProperty.getMember(), member);
    }

    @Test
    public void testSetterlessPropertyDeserializeAndSet() throws IOException {
        Object instance = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        SetterlessProperty setterlessProperty = null;

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // This method doesn't return anything, so we cannot directly assert on it
    }

    @Test
    public void testSetterlessPropertyDeserializeSetAndReturn() throws IOException {
        Object instance = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        SetterlessProperty setterlessProperty = null;

        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertySet() throws IOException {
        Object instance = null;
        SetterlessProperty setterlessProperty = null;

        setterlessProperty.set(instance, null);
    }

    @Test
    public void testSetterlessPropertySetAndReturn() throws IOException {
        Object instance = null;
        SetterlessProperty setterlessProperty = null;

        Object result = setterlessProperty.setAndReturn(instance, null);

        assertEquals(instance, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterlessPropertyWithNullGetMethod() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        Method getter = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, getter);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterlessPropertyWithNullInstance() throws IOException {
        SetterlessProperty setterlessProperty = null;
        Object instance = null;

        setterlessProperty.deserializeAndSet(null, null, instance);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterlessPropertyWithNullPropertyName() {
        SetterlessProperty setterlessProperty = null;
        PropertyName newName = null;

        SetterlessProperty result = setterlessProperty.withName(newName);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterlessPropertyWithNullValueDeserializer() {
        SetterlessProperty src = null;
        JsonDeserializer<?> deser = null;

        SetterlessProperty result = new SetterlessProperty(src, deser, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetterlessPropertyWithNullNullProvider() {
        SetterlessProperty src = null;
        NullValueProvider nva = null;

        SetterlessProperty result = new SetterlessProperty(src, null, nva);
    }

    @Test
    public void testSetterlessPropertyWithNullValueTypeDeserializer() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        SetterlessProperty setterlessProperty = null;

        ctxt.reportBadDefinition(getType(), "Problem deserializing 'setterless' property: no way to handle typed deser with setterless yet");
    }

    @Test(expected = IOException.class)
    public void testSetterlessPropertyWithNullInstanceAndNullValue() throws IOException {
        SetterlessProperty setterlessProperty = null;
        Object instance = null;

        setterlessProperty.deserializeAndSet(null, null, instance);
    }

    @Test(expected = IOException.class)
    public void testSetterlessPropertyWithNullInstanceAndNullPropertyName() throws IOException {
        SetterlessProperty setterlessProperty = null;
        Object instance = null;
        PropertyName newName = null;

        setterlessProperty.withName(newName);
    }

    @Test(expected = IOException.class)
    public void testSetterlessPropertyWithNullPropertyValue() throws IOException {
        Object instance = null;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        SetterlessProperty setterlessProperty = null;

        setterlessProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test(expected = IOException.class)
    public void testSetterlessPropertyWithNullType() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertyWithValueDeserializerNullInstance() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        SetterlessProperty setterlessProperty = null;

        setterlessProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertyWithValueDeserializerNullPropertyName() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        SetterlessProperty setterlessProperty = null;

        setterlessProperty.deserializeAndSet(p, ctxt, instance);
    }
}