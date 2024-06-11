
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.*;
import java.time.*;
import java.util.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat_CustomDateFormat_ReturnsCorrectFormat() {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null, customFormat);

        // Act
        DateTimeSerializerBase<Date> result = serializer.withFormat(false, customFormat);

        // Assert
        Assert.assertEquals(customFormat, result._customFormat);
        Assert.assertFalse(result._useTimestamp);
    }

    @Test
    public void testCreateContextual_NullProperty_ReturnsThis() {
        // Arrange
        DateTimeSerializerBase<LocalDate> serializer = new DateTimeSerializerBase<LocalDate>(LocalDate.class, false, null) {
            @Override
            public DateTimeSerializerBase<LocalDate> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(LocalDate value) {
                return 0;
            }

            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        Assert.assertSame(serializer, result);
    }

    @Test
    public void testGetSchema_UseTimestamp_ReturnsSchemaNodeWithTypeNumber() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null, null);
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        Assert.assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testGetSchema_UseTimestamp_ReturnsSchemaNodeWithIsRequired() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null, null);
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        Assert.assertTrue(result.get("required").asBoolean());
    }

    @Test
    public void testSerialize_NullValue_CallsGetSchemaAndSerializeWithNull() throws IOException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null, null);
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        // Mocking JsonGenerator
        gen = Mockito.mock(JsonGenerator.class);

        // Act
        serializer.serialize(null, gen, serializers);

        // Assert
        Mockito.verify(serializer, Mockito.times(1)).getSchema(serializers, null);
        Mockito.verify(gen, Mockito.times(1)).writeNull();
    }

}
