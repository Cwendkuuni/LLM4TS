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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SetterlessPropertyTest {

    @Mock
    private BeanPropertyDefinition mockPropDef;
    @Mock
    private JavaType mockType;
    @Mock
    private TypeDeserializer mockTypeDeser;
    @Mock
    private Annotations mockContextAnnotations;
    @Mock
    private AnnotatedMethod mockMethod;
    @Mock
    private JsonDeserializer<?> mockDeserializer;
    @Mock
    private NullValueProvider mockNullValueProvider;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private Object mockInstance;
    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private SettableBeanProperty mockSettableBeanProperty;

    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setterlessProperty = new SetterlessProperty(mockPropDef, mockType, mockTypeDeser, mockContextAnnotations, mockMethod);
    }

    @Test
    public void testConstructor() {
        assertEquals(mockMethod, setterlessProperty._annotated);
        assertEquals(mockMethod.getAnnotated(), setterlessProperty._getter);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SetterlessProperty newSetterlessProperty = (SetterlessProperty)setterlessProperty.withName(newName);
        assertEquals(newName, newSetterlessProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty newSetterlessProperty = (SetterlessProperty)setterlessProperty.withValueDeserializer(mockDeserializer);
        assertEquals(mockDeserializer, newSetterlessProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty newSetterlessProperty = (SetterlessProperty)setterlessProperty.withNullProvider(mockNullValueProvider);
        assertEquals(mockNullValueProvider, newSetterlessProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig mockConfig = mock(DeserializationConfig.class);
        when(mockConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        setterlessProperty.fixAccess(mockConfig);
        verify(mockMethod).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<?> acls = Annotation.class;
        setterlessProperty.getAnnotation(acls);
        verify(mockMethod).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        assertEquals(mockMethod, setterlessProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonToken mockToken = mock(JsonToken.class);
        when(mockJsonParser.getCurrentToken()).thenReturn(mockToken);
        when(mockToken.equals(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockMethod.invoke(mockInstance, (Object[])null)).thenReturn(null);
        setterlessProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);
        verify(mockDeserializationContext).reportBadDefinition(setterlessProperty.getType(), "Problem deserializing 'setterless' property '" + setterlessProperty.getName() + "': get method returned null");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object returnValue = setterlessProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, mockInstance);
        assertNull(returnValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(mockInstance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object returnValue = setterlessProperty.setAndReturn(mockInstance, null);
        assertEquals(mockInstance, returnValue);
    }
}