package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private TypeDeserializer typeDeserializer;

    private SettableAnyProperty settableAnyProperty;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        settableAnyProperty = new SettableAnyProperty(property, setter, type,
                keyDeserializer, valueDeserializer, typeDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p, times(1)).getCurrentToken();
        verify(valueDeserializer, times(1)).deserialize(eq(p), eq(ctxt));
    }

    @Test
    public void testDeserializeAndSet_UnresolvedForwardReference() throws IOException {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(settableAnyProperty, "test");
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference(reference);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p, times(1)).getCurrentToken();
        verify(p, times(1)).getParsingContext();
        verify(ctxt, times(1)).isEnabled(eq(MapperFeature.USE_GETTERS_AS_SETTERS));
        verify(ctxt, times(1)).reportUnresolvedForwardReference(
                eq(unresolvedForwardReference), any(ReadableObjectId.class), eq(propName));

    }

    @Test
    public void testDeserialize_NullValueDeser() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        settableAnyProperty = settableAnyProperty.withValueDeserializer(null);

        Object result = settableAnyProperty.deserialize(p, ctxt);

        assertNull(result);
        verify(p, times(1)).getCurrentToken();
        verify(valueDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        verify(valueDeserializer, never()).deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class));
    }

    @Test
    public void testDeserialize_WithType() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        settableAnyProperty.deserialize(p, ctxt);

        verify(p, times(1)).getCurrentToken();
        verify(valueDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        verify(p, never()).getValueAsString();
        verify(keyDeserializer, never()).deserializeKey(anyString(), any(DeserializationContext.class));
        verify(valueDeserializer, times(1)).deserializeWithType(eq(p), eq(ctxt), eq(typeDeserializer));
    }

    @Test
    public void testSet_SetterIsField() throws IOException {
        AnnotatedField field = mock(AnnotatedField.class);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when(setter).thenReturn(field);
        Map<Object, Object> val = mock(Map.class);
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();
        when(field.getValue(instance)).thenReturn(val);

        settableAnyProperty.set(instance, propName, value);

        verify(val, times(1)).put(eq(propName), eq(value));
    }

    @Test
    public void testSet_SetterIsField_NullVal() throws IOException {
        AnnotatedField field = mock(AnnotatedField.class);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when(setter).thenReturn(field);
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();
        when(field.getValue(instance)).thenReturn(null);

        settableAnyProperty.set(instance, propName, value);

        verify(field, times(1)).getValue(instance);
    }

    @Test
    public void testSet_SetterIsField_IllegalArgumentException() {
        AnnotatedField field = mock(AnnotatedField.class);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when(setter).thenReturn(field);
        Map<Object, Object> val = mock(Map.class);
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();
        when(field.getValue(instance)).thenReturn(val);

        doThrow(new IllegalArgumentException()).when(val).put(eq(propName), eq(value));

        try {
            settableAnyProperty.set(instance, propName, value);
        } catch (IOException e) {
            assertEquals("Problem deserializing \"any\" property '" + propName +
                    "' of class " + settableAnyProperty.getClassName() +
                    " (expected type: " + settableAnyProperty.getType() +
                    "; actual type: " + ClassUtil.classNameOf(value) + ") (no error message provided)", e.getMessage());
        }

        verify(val, times(1)).put(eq(propName), eq(value));
    }

    @Test
    public void testSet_SetterIsMethod() throws IOException {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        when(setter instanceof AnnotatedField).thenReturn(false);
        when(setter).thenReturn(method);
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();

        settableAnyProperty.set(instance, propName, value);

        verify(method, times(1)).callOnWith(eq(instance), eq(propName), eq(value));
    }

    @Test
    public void testThrow_AsIOE_IllegalArgumentException() throws IOException {
        IllegalArgumentException exception = mock(IllegalArgumentException.class);
        Object propName = new Object();
        Object value = new Object();
        when(exception.getMessage()).thenReturn(null);

        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            assertEquals("Problem deserializing \"any\" property '" + propName +
                    "' of class " + settableAnyProperty.getClassName() +
                    " (expected type: " + settableAnyProperty.getType() +
                    "; actual type: " + ClassUtil.classNameOf(value) + ") (no error message provided)", e.getMessage());
        }

        verify(exception, times(1)).getMessage();
    }

    @Test
    public void testThrow_AsIOE_Exception() throws IOException {
        Exception exception = mock(Exception.class);
        Object propName = new Object();
        Object value = new Object();
        when(exception.getMessage()).thenReturn(null);

        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (IOException e) {
            assertEquals(exception.getMessage(), e.getMessage());
        }

        verify(exception, times(1)).getMessage();
        verify(exception, times(1)).initCause(any(Throwable.class));
    }

    @Test
    public void testToString() {
        String result = settableAnyProperty.toString();

        assertEquals("[any property on class " + settableAnyProperty.getClassName() + "]", result);
    }

}