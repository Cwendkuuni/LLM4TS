
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldPropertyTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Test
    public void testDeserializeAndSet_givenJsonPropertyIsNullAndSkipNulls_isTrue_shouldSkipDeserialization()
            throws IOException {
        // Arrange
        Field field = getField(FieldProperty.class, "_field");
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(field, null));

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        fieldProperty._skipNulls = true;

        // Act
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(jsonParser, never()).getValueAsString();
        verify(jsonParser, never()).getValueAsInt();
        verify(jsonParser, never()).getValueAsLong();
        verify(jsonParser, never()).getValueAsDouble();
        verify(jsonParser, never()).getValueAsBoolean();
        verify(jsonParser, never()).getDecimalValue();
    }

    @Test
    public void testDeserializeAndSet_givenJsonPropertyIsNullAndSkipNulls_isFalse_shouldDeserialize()
            throws IOException {
        // Arrange
        Field field = getField(FieldProperty.class, "_field");
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(field, null));

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        // Act
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(jsonParser, times(1)).getValueAsString();
        verify(jsonParser, times(1)).getValueAsInt();
        verify(jsonParser, times(1)).getValueAsLong();
        verify(jsonParser, times(1)).getValueAsDouble();
        verify(jsonParser, times(1)).getValueAsBoolean();
        verify(jsonParser, times(1)).getDecimalValue();
    }

    @Test
    public void testDeserializeAndSet_givenJsonPropertyIsNotNullAndValueTypeDeserializerIsNull_shouldDeserialize()
            throws IOException {
        // Arrange
        Field field = getField(FieldProperty.class, "_field");
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(field, null));

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        // Act
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(fieldProperty._valueDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_givenJsonPropertyIsNotNullAndValueTypeDeserializerNotNull_shouldDeserializeWithType()
            throws IOException {
        // Arrange
        Field field = getField(FieldProperty.class, "_field");
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(field, null));

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueDeserializer.deserializeWithType(jsonParser, deserializationContext, null))
                .thenReturn("test");

        // Act
        fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(fieldProperty._valueDeserializer, times(1)).deserializeWithType(jsonParser, deserializationContext,
                null);
    }

    @Test
    public void testDeserializeAndSet_givenFieldSetThrowsException_shouldThrowIOException() throws IOException {
        // Arrange
        Field field = getField(FieldProperty.class, "_field");
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(field, null));

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");
        doThrow(new IllegalAccessException()).when(field).set(any(), any());

        // Act & Assert
        assertThatThrownBy(() -> fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance))
                .isInstanceOf(IOException.class);
    }

    private Field getField(Class<?> cls, String fieldName) {
        try {
            return cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
