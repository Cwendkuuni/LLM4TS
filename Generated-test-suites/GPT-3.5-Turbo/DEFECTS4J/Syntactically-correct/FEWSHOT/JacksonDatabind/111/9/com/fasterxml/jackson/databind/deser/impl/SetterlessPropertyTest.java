package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

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

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, property.getPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
        assertEquals(annotatedMethod, property.getGetter());
    }

    @Test
    public void testConstructorWithDeserAndNvp() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        NullValueProvider nvp = mock(NullValueProvider.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty newProperty = (SetterlessProperty) property.withValueDeserializer(deser).withNullProvider(nvp);

        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(nvp, newProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        PropertyName newName = mock(PropertyName.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty newProperty = (SetterlessProperty) property.withName(newName);

        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.fixAccess(config);

        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = mock(Annotation.class);
        when(method.getAnnotation(acls)).thenReturn(annotation);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Annotation result = property.getAnnotation(acls);

        assertEquals(annotation, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(jsonToken);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setValueDeserializer(valueDeserializer);
        Object toModify = mock(Object.class);
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenReturn(toModify);

        property.deserializeAndSet(p, ctxt, instance);

        verify(valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_NULL;
        when(p.getCurrentToken()).thenReturn(jsonToken);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithNullToModify() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(jsonToken);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setValueDeserializer(valueDeserializer);

        property.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).reportBadDefinition(property.getType(), String.format(
                "Problem deserializing 'setterless' property '%s': get method returned null",
                property.getName()));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(jsonToken);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setValueDeserializer(valueDeserializer);
        Object toModify = mock(Object.class);
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenReturn(toModify);

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        property.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotatedMethod = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        property.setAndReturn(instance, value);
    }

}