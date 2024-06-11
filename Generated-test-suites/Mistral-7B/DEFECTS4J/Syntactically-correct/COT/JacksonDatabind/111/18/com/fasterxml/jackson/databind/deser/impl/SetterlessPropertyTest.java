package com.example.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor_WithValidArguments() {
        // Given
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Then
        assertSame(propDef, setterlessProperty.getPropertyDefinition());
        assertSame(type, setterlessProperty.getType());
        assertSame(typeDeser, setterlessProperty.getTypeDeserializer());
        assertSame(contextAnnotations, setterlessProperty.getContextAnnotations());
        assertSame(method, setterlessProperty.getAnnotated());
        assertSame(method.getAnnotated(), setterlessProperty.getGetter());
    }

    @Test
    public void testWithName() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        PropertyName newName = mock(PropertyName.class);

        // When
        SetterlessProperty result = setterlessProperty.withName(newName);

        // Then
        assertSame(setterlessProperty, result);
        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        // When
        SetterlessProperty result = setterlessProperty.withValueDeserializer(deser);

        // Then
        assertSame(deser, result.getValueDeserializer());
        assertSame(nva, result.getNullProvider());
    }

    @Test
    public void testWithNullProvider() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        // When
        SetterlessProperty result = setterlessProperty.withNullProvider(nva);

        // Then
        assertSame(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        // When
        setterlessProperty.fixAccess(config);

        // Then
        // No verification needed, as fixAccess() does not have any side effects
    }

    @Test
    public void testGetAnnotation() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Class<Annotation> acls = mock(Class.class);
        Annotation annotation = mock(Annotation.class);

        when(setterlessProperty.getAnnotated().getAnnotation(acls)).thenReturn(annotation);

        // When
        Annotation result = setterlessProperty.getAnnotation(acls);

        // Then
        assertSame(annotation, result);
    }

    @Test
    public void testGetMember() {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);

        when(setterlessProperty.getMember()).thenReturn(annotatedMember);

        // When
        AnnotatedMember result = setterlessProperty.getMember();

        // Then
        assertSame(annotatedMember, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSet() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // When
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Then
        // UnsupportedOperationException is expected
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(setterlessProperty.deserializeAndSet(p, ctxt, instance)).thenReturn();

        // When
        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

        // Then
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        Object value = new Object();

        // When
        setterlessProperty.set(instance, value);

        // Then
        // UnsupportedOperationException is expected
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        Object value = new Object();

        // When
        Object result = setterlessProperty.setAndReturn(instance, value);

        // Then
        assertSame(instance, result);
    }

    @Test
    public void testGetValueTypeDeserializerReturnsNull() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // When
        setterlessProperty.deserializeAndSet(p, ctxt, null);

        // Then
        // No verification needed, as this method is not being called
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_NullInstance() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = null;
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // When & Then
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Expected IOException
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_NullPropertyValue() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        // When
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Then
        // No verification needed, as this method is not being called
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_InvalidJsonToken() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);

        // When & Then
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Expected IOException
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_ExceptionWhileAccessingProperty() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Method getter = mock(Method.class);

        when(setterlessProperty.getGetter()).thenReturn(getter);
        when(instance.getClass()).thenReturn(new Class<?>() {});
        when(getter.invoke(instance, (Object[]) null)).thenThrow(new IOException());

        // When
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Then
        // IOException is expected
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_ExceptionWhileDeserializingPropertyValue() throws IOException {
        // Given
        SetterlessProperty setterlessProperty = mock(SetterlessProperty.class);
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(setterlessProperty.getTypeDeserializer()).thenReturn(typeDeserializer);
        when(typeDeserializer.deserializeValue(p, ctxt, null)).thenThrow(new IOException());

        // When
        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Then
        // IOException is expected
    }
}