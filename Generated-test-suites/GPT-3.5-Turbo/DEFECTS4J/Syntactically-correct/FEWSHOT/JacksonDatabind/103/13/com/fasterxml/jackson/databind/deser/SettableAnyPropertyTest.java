package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() {
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void getProperty_thenReturnProperty() {
        assertEquals(property, settableAnyProperty.getProperty());
    }

    @Test
    public void hasValueDeserializer_whenValueDeserializerIsNull_thenReturnFalse() {
        assertFalse(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void hasValueDeserializer_whenValueDeserializerIsNotNull_thenReturnTrue() {
        settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void getType_thenReturnType() {
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void deserializeAndSet_whenKeyDeserializerIsNull_thenDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        Object key = propName;
        Object value = new Object();

        when(valueDeserializer.getNullValue(context)).thenReturn(null);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter).callOnWith(Mockito.eq(instance), Mockito.eq(key), Mockito.eq(value));
    }

    @Test
    public void deserializeAndSet_whenKeyDeserializerIsNotNull_thenDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        Object key = new Object();
        Object value = new Object();

        when(keyDeserializer.deserializeKey(propName, context)).thenReturn(key);
        when(valueDeserializer.getNullValue(context)).thenReturn(null);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter).callOnWith(Mockito.eq(instance), Mockito.eq(key), Mockito.eq(value));
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeAndSet_whenUnresolvedForwardReference_thenThrowJsonMappingException() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        UnresolvedForwardReference unresolved = mock(UnresolvedForwardReference.class);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void set_whenSetterIsField_thenPutValueIntoMap() throws IOException {
        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> map = mock(Map.class);
        Object instance = new Object();
        Object propName = "prop";
        Object value = new Object();

        when(setter instanceof AnnotatedField).thenReturn(true);
        when((AnnotatedField) setter).thenReturn(field);
        when(field.getValue(instance)).thenReturn(map);

        settableAnyProperty.set(instance, propName, value);

        verify(map).put(propName, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void set_whenSetterIsNotField_thenCallCallOnWithMethod() throws IOException {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object propName = "prop";
        Object value = new Object();

        when(setter instanceof AnnotatedField).thenReturn(false);
        when((AnnotatedMethod) setter).thenReturn(method);

        settableAnyProperty.set(instance, propName, value);

        verify(method).callOnWith(Mockito.eq(instance), Mockito.eq(propName), Mockito.eq(value));
    }

    @SuppressWarnings("rawtypes")
    @Test(expected = IllegalArgumentException.class)
    public void _throwAsIOE_whenExceptionIsIllegalArgumentException_thenThrowJsonMappingException() throws IOException {
        Exception exception = new IllegalArgumentException();
        Object propName = "prop";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(exception, propName, value);
    }

    @Test(expected = JsonMappingException.class)
    public void _throwAsIOE_whenExceptionIsOtherException_thenThrowJsonMappingException() throws IOException {
        Exception exception = new Exception();
        Object propName = "prop";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(exception, propName, value);
    }

    @SuppressWarnings("rawtypes")
    @Test(expected = JsonMappingException.class)
    public void _throwAsIOE_whenExceptionIsOtherExceptionWithInnerException_thenThrowJsonMappingException() throws IOException {
        Exception exception = new Exception(new NullPointerException());
        Object propName = "prop";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(exception, propName, value);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void _throwAsIOE_whenExceptionIsNull_thenThrowJsonMappingExceptionWithNoErrorMessage() throws IOException {
        Exception exception = null;
        Object propName = "prop";
        Object value = new Object();

        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertNull(e.getOriginalMessage());
        }
    }

    @Test
    public void _throwAsIOE_whenExceptionIsNull_thenThrowJsonMappingExceptionWithErrorMessage() throws IOException {
        Exception exception = null;
        Object propName = "prop";
        Object value = new Object();

        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(" (no error message provided)", e.getMessage());
        }
    }
}