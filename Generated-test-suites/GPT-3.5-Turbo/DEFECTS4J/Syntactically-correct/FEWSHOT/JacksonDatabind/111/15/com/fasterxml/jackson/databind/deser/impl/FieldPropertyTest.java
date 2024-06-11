package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {
    private FieldProperty fieldProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedField field;
    private Field f;
    private NullValueProvider nvp;
    private Object instance;
    private JsonParser p;
    private DeserializationContext ctxt;

    @Before
    public void setUp() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        field = mock(AnnotatedField.class);
        f = mock(Field.class);
        nvp = mock(NullValueProvider.class);
        instance = new Object();
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        
        when(field.getAnnotated()).thenReturn(f);
        
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testConstructor() {
        assertEquals(propDef, fieldProperty.getCreator());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getMetadata());
        assertEquals(field, fieldProperty.getMember());
        assertEquals(f, fieldProperty.getField());
        assertNull(fieldProperty.getNullProvider());
        assertEquals(false, fieldProperty.isRequired());
    }

    @Test
    public void testConstructorWithNullProvider() {
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, nvp);
        assertEquals(propDef, fieldProperty.getCreator());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getMetadata());
        assertEquals(field, fieldProperty.getMember());
        assertEquals(f, fieldProperty.getField());
        assertEquals(nvp, fieldProperty.getNullProvider());
        assertEquals(false, fieldProperty.isRequired());
    }

    @Test
    public void testConstructorWithSkipNulls() {
        NullsConstantProvider nc = mock(NullsConstantProvider.class);
        when(nvp.isNulls()).thenReturn(nc);

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, nvp);
        assertEquals(true, fieldProperty.isRequired());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = fieldProperty.withNullProvider(nvp);
        assertEquals(null, result.getValueDeserializer());
        assertEquals(nvp, result.getNullProvider());
        assertEquals(false, result.isRequired());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        fieldProperty.fixAccess(config);
        verify(f).setAccessible(true);
    }

    @Test
    public void testDeserializeAndSetWithTokenNull() throws IOException {
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.hasToken(token)).thenReturn(true);

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(token);
        verify(p, never()).hasToken(JsonToken.VALUE_FALSE);
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeAndSetWithString() throws IOException {
        String value = "test";
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.getValueAsString()).thenReturn(value);
        when(p.hasToken(token)).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(token);

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(token);
        verify(p).getValueAsString();
        verify(p, never()).hasToken(JsonToken.VALUE_FALSE);
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeAndSetWithBoolean() throws IOException {
        boolean value = true;
        JsonToken token = JsonToken.VALUE_TRUE;
        when(p.getBooleanValue()).thenReturn(value);
        when(p.hasToken(token)).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(token);

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(token);
        verify(p).getBooleanValue();
        verify(p, never()).hasToken(JsonToken.VALUE_FALSE);
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeAndSetWithNumber() throws IOException {
        int value = 123;
        JsonToken token = JsonToken.VALUE_NUMBER_INT;
        when(p.getIntValue()).thenReturn(value);
        when(p.hasToken(token)).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(token);

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(token);
        verify(p).getIntValue();
        verify(p, never()).hasToken(JsonToken.VALUE_FALSE);
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeAndSetWithCustomValueTypeDeserializer() throws IOException {
        Object value = new Object();
        JsonToken token = JsonToken.VALUE_EMBEDDED_OBJECT;
        when(typeDeser.deserializeTypedFromObject(p, ctxt)).thenReturn(value);
        when(p.hasToken(token)).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(token);

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        verify(typeDeser).deserializeTypedFromObject(p, ctxt);
        verify(p).hasToken(token);
        verify(p, never()).hasToken(JsonToken.VALUE_FALSE);
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();

        fieldProperty.set(instance, value);

        verify(f).set(instance, value);
        assertEquals(instance, fieldProperty.setAndReturn(instance, value));
    }

    @Test(expected=IOException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        Object value = new Object();
        JsonToken token = JsonToken.VALUE_EMBEDDED_OBJECT;
        when(p.hasToken(token)).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(token);
        doThrow(IOException.class).when(f).set(instance, value);

        fieldProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test(expected=IOException.class)
    public void testSetWithException() throws IOException {
        Object value = new Object();
        doThrow(IOException.class).when(f).set(instance, value);

        fieldProperty.set(instance, value);
    }
}