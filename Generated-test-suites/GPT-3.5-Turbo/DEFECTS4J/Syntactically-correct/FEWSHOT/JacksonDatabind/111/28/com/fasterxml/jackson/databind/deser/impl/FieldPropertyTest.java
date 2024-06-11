package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    private FieldProperty fieldProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        Field annotatedField = mock(Field.class);
        AnnotatedField field = mock(AnnotatedField.class);

        when(field.getAnnotated()).thenReturn(annotatedField);
        when(annotatedField.getName()).thenReturn("field");
        when(propDef.getName()).thenReturn(new PropertyName("field"));

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testDeserializeAndSet_SkipsNull() throws Exception {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verifyZeroInteractions(annotatedField);
    }

    @Test
    public void testDeserializeAndSet_WithNullProvider() throws Exception {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        fieldProperty = fieldProperty.withNullProvider(nullValueProvider);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(123);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verifyZeroInteractions(annotatedField);
        verify(nullValueProvider).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_NullValueDeserializer() throws Exception {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verifyZeroInteractions(annotatedField);
        verifyZeroInteractions(valueDeserializer);
    }

    @Test
    public void testDeserializeAndSet_WithNullValueDeserializer() throws Exception {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);
        fieldProperty = fieldProperty.withNullProvider(nullValueProvider);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(123);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verifyZeroInteractions(annotatedField);
        verifyZeroInteractions(valueDeserializer);
        verify(nullValueProvider).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_WithValueTypeDeserializer() throws Exception {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);
        fieldProperty = fieldProperty.withValueTypeDeserializer(valueTypeDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verifyZeroInteractions(annotatedField);
        verifyZeroInteractions(valueDeserializer);
        verifyZeroInteractions(valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSet_SetsValue() throws Exception {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(123);

        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(annotatedField).set(instance, 123);
        verify(valueDeserializer).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_ReturnsInstance() throws Exception {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        fieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(123);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(annotatedField).set(instance, 123);
        verify(valueDeserializer).deserialize(jsonParser, deserializationContext);

        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn_SetsValue() throws Exception {
        Object value = 123;

        fieldProperty.setAndReturn(instance, value);

        verify(annotatedField).set(instance, value);
    }

    @Test
    public void testSetAndReturn_ReturnsInstance() throws Exception {
        Object value = 123;

        Object result = fieldProperty.setAndReturn(instance, value);

        verify(annotatedField).set(instance, value);

        assertEquals(instance, result);
    }

    @Test(expected = IOException.class)
    public void testSet_ThrowsException() throws Exception {
        Object value = new Object();
        Exception exception = new IllegalArgumentException();

        doThrow(exception).when(annotatedField).set(instance, value);

        fieldProperty.set(instance, value);
    }

    @Test
    public void testSet_NullValue() throws Exception {
        fieldProperty.set(instance, null);

        verify(annotatedField).set(instance, null);
    }

    @Test
    public void testReadResolve() throws Exception {
        FieldProperty result = (FieldProperty) fieldProperty.readResolve();

        assertEquals(fieldProperty.getName(), result.getName());
        assertEquals(fieldProperty.getInternalType(), result.getInternalType());
        assertEquals(fieldProperty.getAnnotations(), result.getAnnotations());
        assertEquals(fieldProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(fieldProperty.getMember(), result.getMember());
        assertEquals(fieldProperty.getNullValueProvider(), result.getNullValueProvider());
    }
}