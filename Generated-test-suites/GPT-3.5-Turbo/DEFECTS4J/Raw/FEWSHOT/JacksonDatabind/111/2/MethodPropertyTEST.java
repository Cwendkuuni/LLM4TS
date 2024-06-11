package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @Mock
    private BeanPropertyDefinition propertyDefinition;

    @Mock
    private JavaType javaType;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedMethod annotatedMethod;

    @Mock
    private DeserializationConfig deserializationConfig;

    @Mock
    private JsonDeserializer<Object> valueDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private PropertyName newName;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);
        
        assertEquals(propertyDefinition, methodProperty.getPropertyDefinition());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(annotatedMethod.getMethod(), methodProperty.getMember());
        assertNull(methodProperty.getValueDeserializer());
        assertNull(methodProperty.getNullProvider());
    }

    @Test
    public void testConstructorWithDeserializers() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newMethodProperty.getValueDeserializer());
        assertNull(newMethodProperty.getNullProvider());
    }

    @Test
    public void testConstructorWithNullProvider() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        MethodProperty newMethodProperty = methodProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newMethodProperty.getNullProvider());
        assertNull(newMethodProperty.getValueDeserializer());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        when(deserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        methodProperty.fixAccess(deserializationConfig);
        verify(annotatedMethod).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        Class<JsonToken> annotationClass = JsonToken.class;
        Annotation annotation = mock(Annotation.class);
        when(annotatedMethod.getAnnotation(annotationClass)).thenReturn(annotation);

        Annotation result = methodProperty.getAnnotation(annotationClass);
        assertEquals(annotation, result);
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);
        methodProperty.setValueDeserializer(valueDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(annotatedMethod).invoke(instance, null);
    }

    @Test
    public void testDeserializeSetAndReturnWithValueDeserializer() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);
        methodProperty.setValueDeserializer(valueDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, result);
        verify(annotatedMethod).invoke(instance, null);
    }

    @Test
    public void testSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        Object value = new Object();
        methodProperty.set(instance, value);

        verify(annotatedMethod).invoke(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        Object value = new Object();
        Object result = methodProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        verify(annotatedMethod).invoke(instance, value);
    }

    @Test
    public void testReadResolve() throws NoSuchMethodException {
        Method setterMethod = MethodProperty.class.getDeclaredMethod("set", Object.class, Object.class);
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, javaType, typeDeserializer,
                contextAnnotations, annotatedMethod);

        MethodProperty newMethodProperty = (MethodProperty) methodProperty.readResolve();
        assertEquals(annotatedMethod.getMethod(), newMethodProperty.getMember());
        assertEquals(setterMethod, newMethodProperty.getAnnotated().getAnnotated());
        assertEquals(methodProperty._skipNulls, newMethodProperty._skipNulls);
    }
}