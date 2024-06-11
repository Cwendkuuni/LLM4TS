package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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
import org.mockito.Mockito;

public class MethodPropertyTest {
    private BeanPropertyDefinition mockPropDef = mock(BeanPropertyDefinition.class);
    private JavaType mockType = mock(JavaType.class);
    private TypeDeserializer mockTypeDeser = mock(TypeDeserializer.class);
    private Annotations mockContextAnnotations = mock(Annotations.class);
    private AnnotatedMethod mockMethod = mock(AnnotatedMethod.class);
    private Method mockSetter = mock(Method.class);
    private JsonDeserializer<?> mockDeserializer = mock(JsonDeserializer.class);
    private NullValueProvider mockNullValueProvider = mock(NullValueProvider.class);
    private DeserializationConfig mockConfig = mock(DeserializationConfig.class);
    private JsonParser mockJsonParser = mock(JsonParser.class);
    private DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
    private Object mockInstance = mock(Object.class);

    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        assertEquals(mockPropDef, methodProperty.getMember());
        assertEquals(mockSetter, methodProperty.getMember().getAnnotated());
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(mockDeserializer);
        assertEquals(mockDeserializer, newMethodProperty.getValueDeserializer());
    }

    @Test
    public void testConstructorWithNullProvider() {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        MethodProperty newMethodProperty = methodProperty.withNullProvider(mockNullValueProvider);
        assertEquals(mockNullValueProvider, newMethodProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        when(mockConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        methodProperty.fixAccess(mockConfig);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        methodProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);
        Mockito.verify(mockJsonParser, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(mockJsonParser, Mockito.times(1)).getValueAsString();
        Mockito.verify(mockJsonParser, Mockito.times(1)).getNullValue();
        Mockito.verify(mockInstance, Mockito.times(1)).getClass();
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        methodProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, mockInstance);
        Mockito.verify(mockJsonParser, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(mockJsonParser, Mockito.times(1)).getValueAsString();
        Mockito.verify(mockJsonParser, Mockito.times(1)).getNullValue();
        Mockito.verify(mockInstance, Mockito.times(1)).getClass();
    }

    @Test
    public void testSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        methodProperty.set(mockInstance, mockInstance);
        Mockito.verify(mockMethod, Mockito.times(1)).getAnnotated();
        Mockito.verify(mockMethod, Mockito.times(1)).invoke(mockInstance, mockInstance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(
                mockPropDef,
                mockType,
                mockTypeDeser,
                mockContextAnnotations,
                mockMethod);
        methodProperty.setAndReturn(mockInstance, mockInstance);
        Mockito.verify(mockMethod, Mockito.times(1)).getAnnotated();
        Mockito.verify(mockMethod, Mockito.times(1)).invoke(mockInstance, mockInstance);
    }

    Object readResolve() {
        return new MethodProperty(this, mockMethod);
    }
}