package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class SettableAnyPropertyTest {

    @Mock
    private BeanProperty property;

    @Mock
    private AnnotatedMember setter;

    @Mock
    private JavaType type;

    @Mock
    private KeyDeserializer keyDeserializer;

    @Mock
    private JsonDeserializer<Object> valueDeserializer;

    @Mock
    private TypeDeserializer valueTypeDeserializer;

    @Mock
    private DeserializationConfig config;

    @InjectMocks
    private SettableAnyProperty settableAnyProperty;

    @Test
    public void testFixAccess() {
        settableAnyProperty.fixAccess(config);
        verify(setter).fixAccess(any(boolean.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadResolve_ThrowsIllegalArgumentException() {
        StubUnresolvedForwardReference unresolvedForwardReference = new StubUnresolvedForwardReference();
        when(setter.getAnnotated()).thenReturn(null);
        settableAnyProperty.readResolve();
    }

    @Test
    public void testReadResolve_ReturnsThis() {
        when(setter.getAnnotated()).thenReturn(new StubAnnotatedMember());
        assertEquals(settableAnyProperty, settableAnyProperty.readResolve());
    }

    @Test
    public void testGetProperty() {
        assertEquals(property, settableAnyProperty.getProperty());
    }

    @Test
    public void testHasValueDeserializer() {
        settableAnyProperty._valueDeserializer = null;
        assertEquals(false, settableAnyProperty.hasValueDeserializer());

        settableAnyProperty._valueDeserializer = valueDeserializer;
        assertEquals(true, settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet_ValueTokenIsNull() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(valueDeserializer.getNullValue(ctxt)).thenReturn(null);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        verify(valueDeserializer).getNullValue(ctxt);
        verifyNoMoreInteractions(valueDeserializer);
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerIsNotNull() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.deserializeWithType(p, ctxt, valueTypeDeserializer)).thenReturn("value");

        settableAnyProperty._valueTypeDeserializer = valueTypeDeserializer;
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        verify(valueDeserializer).deserializeWithType(p, ctxt, valueTypeDeserializer);
        verifyNoMoreInteractions(valueDeserializer);
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerIsNull() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn("value");

        settableAnyProperty._valueTypeDeserializer = null;
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        verify(valueDeserializer).deserialize(p, ctxt);
        verifyNoMoreInteractions(valueDeserializer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSet_SetterIsAnnotatedField_ValueIsNull() throws IOException {
        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> val = mock(Map.class);
        Object instance = mock(Object.class);
        Object propName = mock(Object.class);
        Object value = null;

        when(setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(val);

        settableAnyProperty._setterIsField = true;
        settableAnyProperty.set(instance, propName, value);
        verify(val, never()).put(eq(propName), any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSet_SetterIsAnnotatedField_ValueIsNotNull() throws IOException {
        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> val = mock(Map.class);
        Object instance = mock(Object.class);
        Object propName = mock(Object.class);
        Object value = mock(Object.class);

        when(setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(val);

        settableAnyProperty._setterIsField = true;
        settableAnyProperty.set(instance, propName, value);
        verify(val).put(propName, value);
    }

    @Test
    public void testSet_SetterIsAnnotatedMethod() throws IOException {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = mock(Object.class);
        Object propName = mock(Object.class);
        Object value = mock(Object.class);

        when(setter).thenReturn(method);

        settableAnyProperty._setterIsField = false;
        settableAnyProperty.set(instance, propName, value);
        verify(method).callOnWith(instance, propName, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_Set_ThrowsIllegalArgumentException() throws Exception {
        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> val = mock(Map.class);
        Object instance = mock(Object.class);
        Object propName = mock(Object.class);
        Object value = mock(Object.class);

        when(setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(val);
        doThrow(new IllegalArgumentException()).when(val).put(propName, value);

        settableAnyProperty._setterIsField = true;
        settableAnyProperty._valueDeserializer = valueDeserializer;
        settableAnyProperty._type = type;

        try {
            settableAnyProperty.set(instance, propName, value);
        } catch (JsonMappingException e) {
            assertEquals("Problem deserializing \"any\" property '" + propName + "' of class " + type + " (expected type: " + type + "; actual type: " + value.getClass() + ")", e.getMessage());
            return;
        }

        throw new AssertionError("Expected JsonMappingException");
    }

    private static class StubAnnotatedMember implements AnnotatedMember {

        @Override
        public Class<?> getDeclaringClass() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annoClass) {
            return null;
        }

        @Override
        public boolean hasAnnotation(Class<?> annoClass) {
            return false;
        }

        @Override
        public boolean hasOneOf(Class<? extends java.lang.annotation.Annotation>[] annoClasses) {
            return false;
        }

        @Override
        public Object getValue(Object pojo) throws IllegalArgumentException {
            return null;
        }

        @Override
        public void setValue(Object pojo, Object value) throws IllegalArgumentException, UnsupportedOperationException {

        }

        @Override
        public AnnotatedMember getPrimaryMember() {
            return null;
        }
    }

    private class StubUnresolvedForwardReference extends UnresolvedForwardReference {

        @Override
        public void handleResolvedForwardReference(Object id, Object value) throws IOException {

        }
    }
}