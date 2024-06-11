package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JavaType type;

    @Mock
    private TypeDeserializer typeDeser;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedField field;

    @Mock
    private Field fieldObj;

    @Mock
    private JsonParser p;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    private ObjectMapper mapper;

    private FieldProperty property;

    @Before
    public void setUp() throws Exception {
        mapper = mock(ObjectMapper.class);
        when(field.getAnnotated()).thenReturn(fieldObj);
        when(propDef.getField()).thenReturn(field);
        property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property.fixAccess(mapper.getDeserializationConfig());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object value = "SomeValue";
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn(value.toString());
        property.deserializeAndSet(p, ctxt, instance);
        verify(fieldObj).set(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        property.deserializeAndSet(p, ctxt, instance);
        verify(fieldObj, never()).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object value = "SomeValue";
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn(value.toString());
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        verify(fieldObj).set(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        verify(fieldObj, never()).set(any(), any());
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        Object value = "SomeValue";
        property.set(instance, value);
        verify(fieldObj).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = "SomeValue";
        Object result = property.setAndReturn(instance, value);
        verify(fieldObj).set(instance, value);
        assertEquals(instance, result);
    }
    
}