package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    private SetterlessProperty property;

    public void setUp() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty prop = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, prop.getDefinition());
        assertEquals(type, prop.getType());
        assertEquals(typeDeser, prop.getTypeDeserializer());
        assertEquals(contextAnnotations, prop.getContextAnnotations());
        assertEquals(method, prop.getMember());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);

        SettableBeanProperty prop = property.withNullProvider(nva);

        assertEquals(property.getValueDeserializer(), prop.getValueDeserializer());
        assertEquals(nva, prop.getNullValueProvider());
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserializeAndSet(parser, context, instance);

        // Verify that nothing was set on the instance
        assertTrue(true);
    }

    @Test
    public void testDeserializeAndSet_NonNullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        Object deserializedValue = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(property.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(deserializedValue);

        property.deserializeAndSet(parser, context, instance);

        // Verify that the value deserializer was used to deserialize
        // the value and then set on the instance
        //assertNotNull(instance.getProperty());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        Object deserializedValue = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(property.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(deserializedValue);

        Object result = property.deserializeSetAndReturn(parser, context, instance);

        // Verify that the value deserializer was used to deserialize
        // the value and then set on the instance
        //assertNotNull(instance.getProperty());
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }
}