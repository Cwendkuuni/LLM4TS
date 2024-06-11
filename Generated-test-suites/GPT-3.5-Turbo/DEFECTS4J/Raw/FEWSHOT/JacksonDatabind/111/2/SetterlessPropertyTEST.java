package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.mockito.Mockito;

public class SetterlessPropertyTest {
    @Test
    public void testConstructorWithNullValueProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(propDef, property.getMember());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testConstructorWithDeserializerAndValueProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property = new SetterlessProperty(property, deser, nva);
        assertEquals(propDef, property.getMember());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        PropertyName newName = mock(PropertyName.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property = new SetterlessProperty(property, newName);
        assertEquals(propDef, property.getMember());
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.fixAccess(config);
        verify(method, times(1)).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.getAnnotation(acls);
        verify(method, times(1)).getAnnotation(acls);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken token = mock(JsonToken.class);
        Object toModify = mock(Object.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        when(p.getCurrentToken()).thenReturn(token);
        when(token == JsonToken.VALUE_NULL).thenReturn(false);
        when(p.readValueAs(SetterlessPropertyTest.class)).thenReturn(toModify);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(toModify);

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setValueDeserializer(valueDeserializer);
        property.deserializeAndSet(p, ctxt, instance);

        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken token = mock(JsonToken.class);
        Object toModify = mock(Object.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        when(p.getCurrentToken()).thenReturn(token);
        when(token == JsonToken.VALUE_NULL).thenReturn(false);
        when(p.readValueAs(SetterlessPropertyTest.class)).thenReturn(toModify);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(toModify);

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setValueDeserializer(valueDeserializer);
        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object instance = new Object();
        assertEquals(instance, property.setAndReturn(instance, new Object()));
    }
}