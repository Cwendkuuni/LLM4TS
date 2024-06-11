package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FieldPropertyTest {

    private JsonDeserializer<Object> valueDeserializer;
    private NullValueProvider nullValueProvider;
    private AnnotatedField annotatedField;
    private FieldProperty fieldProperty;
    private Object testInstance;

    @Before
    public void setup() throws Exception {
        valueDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(new Object() {});
        fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        testInstance = new Object();
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");

        // When
        fieldProperty.deserializeAndSet(parser, context, testInstance);

        // Then
        assertEquals("testValue", fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws JsonProcessingException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        // When
        if (fieldProperty._skipNulls) {
            fieldProperty.deserializeAndSet(parser, context, testInstance);
        }

        // Then
        assertNull(fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws JsonProcessingException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new Object());
        when(context.findValueDeserializer(fieldProperty.getPropertyDefinition().getType())).thenReturn(valueDeserializer);
        when(context.findTypeDeserializer(fieldProperty.getPropertyDefinition().getType(), null)).thenReturn(typeDeserializer);

        // When
        fieldProperty = new FieldProperty(fieldProperty, valueDeserializer, nullValueProvider);
        fieldProperty.deserializeAndSet(parser, context, testInstance);

        // Then
        assertEquals(new Object(), fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");

        // When
        Object result = fieldProperty.deserializeSetAndReturn(parser, context, testInstance);

        // Then
        assertEquals(testInstance, result);
        assertEquals("testValue", fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testSet() throws Exception {
        // Given

        // When
        fieldProperty.set(testInstance, new Object());

        // Then
        assertEquals(new Object(), fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testSet_NullValue() throws Exception {
        // Given

        // When
        fieldProperty.set(testInstance, null);

        // Then
        assertNull(fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testSet_ValueDeserializer() throws JsonProcessingException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object expectedValue = new Object();

        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(expectedValue);

        // When
        fieldProperty = new FieldProperty(fieldProperty, valueDeserializer, nullValueProvider);
        fieldProperty.set(testInstance, parser);

        // Then
        assertEquals(expectedValue, fieldProperty.getField().get(testInstance));
    }

    @Test
    public void testFixAccess() throws Exception {
        // Given

        // When
        fieldProperty.fixAccess(null);

        // Then
        // No specific verification needed, as the test does not give us access to the private field
    }
}