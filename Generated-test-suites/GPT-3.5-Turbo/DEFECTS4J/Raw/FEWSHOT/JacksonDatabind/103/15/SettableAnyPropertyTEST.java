package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    private BeanProperty beanProperty;

    private AnnotatedMember setter;

    private JavaType type;

    private KeyDeserializer keyDeserializer;

    private JsonDeserializer<Object> valueDeserializer;

    private TypeDeserializer typeDeserializer;

    @Before
    public void setUp() {
        beanProperty = mock(BeanProperty.class);
        setter = mock(AnnotatedMember.class);
        type = mock(JavaType.class);
        keyDeserializer = mock(KeyDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        settableAnyProperty = new SettableAnyProperty(beanProperty, setter, type, keyDeserializer, valueDeserializer,
                typeDeserializer);
    }

    @Test
    public void testGetProperty() {
        assertEquals(beanProperty, settableAnyProperty.getProperty());
    }

    @Test
    public void testHasValueDeserializer() {
        when(valueDeserializer != null).thenReturn(true);
        assertEquals(true, settableAnyProperty.hasValueDeserializer());
        when(valueDeserializer != null).thenReturn(false);
        assertEquals(false, settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "test";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        doReturn(reference).when(p).currentValue();
        SettableAnyProperty.AnySetterReferring referring = mock(SettableAnyProperty.AnySetterReferring.class);
        when(reference.getRoid()).thenReturn(mock(ReadableObjectId.class));
        when(reference.getRoid().appendReferring(referring)).thenReturn(true);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        verify(p).currentToken();
        verify(p).getCurrentToken();
        verify(valueDeserializer).getNullValue(ctxt);
        verify(setter).getValue(instance);
        verify(setter).callOnWith(instance, propName, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "test";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        doReturn(reference).when(p).currentValue();
        SettableAnyProperty.AnySetterReferring referring = mock(SettableAnyProperty.AnySetterReferring.class);
        when(reference.getRoid()).thenReturn(mock(ReadableObjectId.class));
        when(reference.getRoid().appendReferring(referring)).thenReturn(false);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object expectedResult = new Object();

        doReturn(JsonToken.VALUE_STRING).when(p).getCurrentToken();
        when(typeDeserializer != null).thenReturn(false);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(expectedResult);
        assertEquals(expectedResult, settableAnyProperty.deserialize(p, ctxt));

        doReturn(JsonToken.VALUE_STRING).when(p).getCurrentToken();
        when(typeDeserializer != null).thenReturn(true);
        when(valueDeserializer.deserializeWithType(p, ctxt, typeDeserializer)).thenReturn(expectedResult);
        assertEquals(expectedResult, settableAnyProperty.deserialize(p, ctxt));

        doReturn(JsonToken.VALUE_NULL).when(p).getCurrentToken();
        assertNull(settableAnyProperty.deserialize(p, ctxt));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object propName = "test";
        Object value = "value";

        AnnotatedField field = mock(AnnotatedField.class);
        when(_setterIsField).thenReturn(true);
        when(setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(mock(Map.class));
        settableAnyProperty.set(instance, propName, value);
        verify(field).getValue(instance);
        verify(field.getValue(instance)).put(propName, value);

        AnnotatedMethod method = mock(AnnotatedMethod.class);
        when(_setterIsField).thenReturn(false);
        when(setter).thenReturn(method);
        settableAnyProperty.set(instance, propName, value);
        verify(method, times(1)).callOnWith(instance, propName, value);

        settableAnyProperty.set(instance, propName, value);
        verify(method, times(2)).callOnWith(instance, propName, value);

        Exception e = mock(Exception.class);
        when(_setter.callOnWith(instance, propName, value)).thenThrow(e);
        when(e instanceof IllegalArgumentException).thenReturn(true);
        when(ClassUtil.classNameOf(value)).thenReturn("java.lang.String");
        when(_type.toString()).thenReturn("MockedType");
        when(e.getMessage()).thenReturn("MockedExceptionMessage");
        try {
            settableAnyProperty.set(instance, propName, value);
        } catch (JsonMappingException jme) {
            IllegalArgumentException iae = (IllegalArgumentException) jme.getCause();
            assertEquals("Problem deserializing \"any\" property '" + propName + "' of class MockedType "
                    + "(expected type: MockedType; actual type: java.lang.String), problem: MockedExceptionMessage",
                    iae.getMessage());
        }

        settableAnyProperty.set(instance, propName, value);
        verify(e).getMessage();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IOException.class)
    public void testSetWithIOException() throws IOException {
        Object instance = new Object();
        Object propName = "test";
        Object value = "value";
        AnnotatedField field = mock(AnnotatedField.class);
        when(_setterIsField).thenReturn(true);
        when(setter).thenReturn(field);
        when(field.getValue(instance)).thenThrow(IOException.class);
        settableAnyProperty.set(instance, propName, value);

        AnnotatedMethod method = mock(AnnotatedMethod.class);
        when(_setterIsField).thenReturn(false);
        when(setter).thenReturn(method);
        when(method.callOnWith(instance, propName, value)).thenThrow(IOException.class);
        settableAnyProperty.set(instance, propName, value);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testThrowAsIOE() throws IOException {
        Object propName = "test";
        Object value = "value";
        Exception e = mock(Exception.class);
        when(e instanceof IllegalArgumentException).thenReturn(true);
        when(ClassUtil.getRootCause(e)).thenReturn(e);
        String errorMessage = "error message";
        when(e.getMessage()).thenReturn(errorMessage);

        try {
            settableAnyProperty._throwAsIOE(e, propName, value);
        } catch (JsonMappingException jme) {
            assertEquals(errorMessage, jme.getMessage());
        }

        when(e instanceof IOException).thenReturn(true);
        try {
            settableAnyProperty._throwAsIOE(e, propName, value);
        } catch (IOException ioe) {
            assertEquals(errorMessage, ioe.getMessage());
        }

        when(e instanceof RuntimeException).thenReturn(true);
        try {
            settableAnyProperty._throwAsIOE(e, propName, value);
        } catch (RuntimeException re) {
            assertEquals(errorMessage, re.getMessage());
        }
    }

    @Test
    public void testGetClassName() {
        when(setter.getDeclaringClass()).thenReturn(Object.class);
        assertEquals("java.lang.Object", settableAnyProperty.getClassName());
    }

    @Test
    public void testToString() {
        when(setter.getDeclaringClass()).thenReturn(Object.class);
        assertEquals("[any property on class java.lang.Object]", settableAnyProperty.toString());
    }
}