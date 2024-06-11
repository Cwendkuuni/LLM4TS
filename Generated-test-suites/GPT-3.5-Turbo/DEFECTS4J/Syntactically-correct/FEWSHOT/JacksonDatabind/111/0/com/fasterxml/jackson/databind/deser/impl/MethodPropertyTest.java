package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    @Mock
    private BeanPropertyDefinition beanPropertyDefinition;

    @Mock
    private JavaType javaType;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedMethod annotatedMethod;

    @Mock
    private Method method;

    @Mock
    private SettableBeanProperty settableBeanProperty;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private PropertyName propertyName;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private JsonParser jsonParser;

    private MethodProperty methodProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);
    }

    @Test
    public void testConstructor() {
        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(true, methodProperty.isRequired());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty newMethodProperty = methodProperty.withNullProvider(nullValueProvider);

        assertEquals(methodProperty, newMethodProperty);
    }

    @Test
    public void testWithName() {
        SettableBeanProperty newMethodProperty = methodProperty.withName(propertyName);

        assertEquals(methodProperty, newMethodProperty);
    }

    @Test
    public void testWithNullDeserializer() {
        SettableBeanProperty newMethodProperty = methodProperty.withValueDeserializer(null);

        assertEquals(methodProperty, newMethodProperty);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        when(jsonParser.hasToken(any(JsonToken.class))).thenReturn(false);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(instance);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(annotatedMethod).invoke(instance, instance);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        when(jsonParser.hasToken(any(JsonToken.class))).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(annotatedMethod, never()).invoke(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        when(jsonParser.hasToken(any(JsonToken.class))).thenReturn(false);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(instance);

        assertEquals(instance, methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(annotatedMethod).invoke(instance, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        when(jsonParser.hasToken(any(JsonToken.class))).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        assertEquals(instance, methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(annotatedMethod, never()).invoke(any(), any());
    }

    @Test
    public void testSet() throws Exception {
        methodProperty.set(instance, instance);

        verify(annotatedMethod).invoke(instance, instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        assertEquals(instance, methodProperty.setAndReturn(instance, instance));

        verify(annotatedMethod).invoke(instance, instance);
    }

    @Test
    public void testConstructorWithDeserAndNVP() {
        MethodProperty methodProperty = new MethodProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

        assertEquals(settableBeanProperty, methodProperty.getMember());
        assertEquals(jsonDeserializer, methodProperty.getValueDeserializer());
        assertEquals(nullValueProvider, methodProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        MethodProperty methodProperty = new MethodProperty(settableBeanProperty, propertyName);

        assertEquals(settableBeanProperty, methodProperty.getMember());
        assertEquals(propertyName, methodProperty.getFullName());
    }

    @Test
    public void testConstructorWithRefMethod() {
        MethodProperty methodProperty = new MethodProperty(settableBeanProperty, method);

        assertEquals(settableBeanProperty, methodProperty.getMember());
    }
}