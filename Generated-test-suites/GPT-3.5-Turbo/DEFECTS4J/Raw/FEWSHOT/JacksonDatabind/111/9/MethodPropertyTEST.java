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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MethodPropertyTest {

    @Mock
    private BeanPropertyDefinition mockPropDef;

    @Mock
    private JavaType mockType;

    @Mock
    private TypeDeserializer mockTypeDeser;

    @Mock
    private Annotations mockContextAnnotations;

    @Mock
    private AnnotatedMethod mockAnnotatedMethod;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Mock
    private NullValueProvider mockNullValueProvider;

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private JsonDeserializer<?> mockDeserializer;

    @Mock
    private PropertyName mockPropertyName;

    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        methodProperty = new MethodProperty(mockPropDef, mockType, mockTypeDeser, mockContextAnnotations, mockAnnotatedMethod);
    }

    @Test
    public void testConstructor() {
        assertEquals(mockPropDef, methodProperty.getMetadata());
        assertEquals(mockType, methodProperty.getType());
        assertEquals(mockTypeDeser, methodProperty.getTypeDeserializer());
        assertEquals(mockContextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(mockAnnotatedMethod, methodProperty.getMember());

        verifyZeroInteractions(mockPropDef);
        verifyZeroInteractions(mockType);
        verifyZeroInteractions(mockTypeDeser);
        verifyZeroInteractions(mockContextAnnotations);
        verifyZeroInteractions(mockAnnotatedMethod);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        MethodProperty methodPropertyWithDeserializerAndNullValueProvider = new MethodProperty(methodProperty, mockDeserializer, mockNullValueProvider);

        assertEquals(mockDeserializer, methodPropertyWithDeserializerAndNullValueProvider.getValueDeserializer());
        assertEquals(mockNullValueProvider, methodPropertyWithDeserializerAndNullValueProvider.getNullValueProvider());

        methodPropertyWithDeserializerAndNullValueProvider = new MethodProperty(methodProperty, mockDeserializer, null);

        assertEquals(mockDeserializer, methodPropertyWithDeserializerAndNullValueProvider.getValueDeserializer());
        assertNull(methodPropertyWithDeserializerAndNullValueProvider.getNullValueProvider());

        verifyZeroInteractions(mockDeserializer);
        verifyZeroInteractions(mockNullValueProvider);
    }

    @Test
    public void testConstructorWithPropertyName() {
        MethodProperty methodPropertyWithPropertyName = new MethodProperty(methodProperty, mockPropertyName);

        assertEquals(mockPropertyName, methodPropertyWithPropertyName.getFullName());

        verifyZeroInteractions(mockPropertyName);
    }

    @Test
    public void testConstructorWithMethod() {
        MethodProperty methodPropertyWithMethod = new MethodProperty(methodProperty, mockAnnotatedMethod);

        assertEquals(mockAnnotatedMethod, methodPropertyWithMethod.getMember());

        verifyZeroInteractions(mockAnnotatedMethod);
    }

    @Test
    public void testWithPropertyName() {
        MethodProperty methodPropertyWithPropertyName = (MethodProperty) methodProperty.withName(mockPropertyName);

        assertEquals(mockPropertyName, methodPropertyWithPropertyName.getFullName());

        verifyZeroInteractions(mockPropertyName);
    }

    @Test
    public void testWithDeserializer() {
        MethodProperty methodPropertyWithDeserializer = (MethodProperty) methodProperty.withValueDeserializer(mockDeserializer);

        assertEquals(mockDeserializer, methodPropertyWithDeserializer.getValueDeserializer());

        verifyZeroInteractions(mockDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty methodPropertyWithNullProvider = (MethodProperty) methodProperty.withNullProvider(mockNullValueProvider);

        assertEquals(mockNullValueProvider, methodPropertyWithNullProvider.getNullValueProvider());

        verifyZeroInteractions(mockNullValueProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig mockDeserializationConfig = mock(DeserializationConfig.class);

        when(mockDeserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

        methodProperty.fixAccess(mockDeserializationConfig);

        verify(mockAnnotatedMethod).fixAccess(true);
        verifyZeroInteractions(mockDeserializationConfig);
        verifyZeroInteractions(mockAnnotatedMethod);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> mockAcls = mock(Class.class);
        Annotation mockAnnotation = mock(Annotation.class);

        when(mockAnnotatedMethod.getAnnotation(mockAcls)).thenReturn(mockAnnotation);

        Annotation result = methodProperty.getAnnotation(mockAcls);

        assertEquals(mockAnnotation, result);

        verify(mockAnnotatedMethod).getAnnotation(mockAcls);
        verifyZeroInteractions(mockAcls);
        verifyZeroInteractions(mockAnnotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = methodProperty.getMember();

        assertEquals(mockAnnotatedMethod, result);

        verifyZeroInteractions(mockAnnotatedMethod);
    }

    @Test
    public void testDeserializeAndSetWithSkipNulls() throws IOException {
        String[] value = {"test"};
        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(mockNullValueProvider.getNullValue(mockDeserializationContext)).thenReturn(null);

        methodProperty.setSkipNulls(true);
        methodProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, value);

        assertEquals("test", value[0]);

        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(mockNullValueProvider).getNullValue(mockDeserializationContext);
        verifyZeroInteractions(mockJsonParser);
        verifyZeroInteractions(mockDeserializationContext);
        verifyZeroInteractions(mockNullValueProvider);
    }

}