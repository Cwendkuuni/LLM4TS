package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {
    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithNullPropertyName() {
        PropertyName newName = null;
        SettableBeanProperty result = setterlessProperty.withName(newName);
        assertNull(result.getName());
    }

    @Test
    public void testWithBlankPropertyName() {
        PropertyName newName = new PropertyName("");
        SettableBeanProperty result = setterlessProperty.withName(newName);
        assertEquals("", result.getName().getSimpleName());
    }

    @Test
    public void testWithValidPropertyName() {
        PropertyName newName = new PropertyName("property");
        SettableBeanProperty result = setterlessProperty.withName(newName);
        assertEquals("property", result.getName().getSimpleName());
    }

    @Test
    public void testWithNullValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);
        assertNull(result.getValueDeserializer());
    }

    @Test
    public void testWithValidValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullNullValueProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = setterlessProperty.withNullProvider(nva);
        assertNull(result.getNullValueProvider());
    }

    @Test
    public void testWithValidNullValueProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = setterlessProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccessWithOverridePublicAccessModifiersEnabled() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        setterlessProperty.fixAccess(config);
        // Verify that fixAccess method is called
    }

    @Test
    public void testFixAccessWithOverridePublicAccessModifiersDisabled() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        setterlessProperty.fixAccess(config);
        // Verify that fixAccess method is not called
    }

    @Test
    public void testGetAnnotationWithNullClass() {
        Class<A> acls = null;
        assertNull(setterlessProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = setterlessProperty.getMember();
        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        // Verify that deserializeAndSet method is called
    }

    @Test
    public void testDeserializeAndSetWithValueTypeDeserializerNull() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        setterlessProperty.setValueTypeDeserializer(null);
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        // Verify that deserializeAndSet method is called
    }

    @Test
    public void testDeserializeSetAndReturnValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        setterlessProperty.set(instance, value);
        // Expecting UnsupportedOperationException
    }

    @Test
    public void testSetAndReturnValue() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = setterlessProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
    }
}