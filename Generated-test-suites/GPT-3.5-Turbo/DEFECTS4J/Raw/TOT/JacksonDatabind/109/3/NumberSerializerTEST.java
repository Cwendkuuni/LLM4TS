
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private BeanProperty beanProperty;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        beanProperty = mock(BeanProperty.class);
    }

    @Test
    public void testCreateContextual_WithFormatString_ReturnsToStringSerializer() throws JsonMappingException {
        // Arrange
        when(beanProperty.findPropertyFormat(any())).thenReturn(new JsonFormat.Value().withShape(JsonFormat.Shape.STRING));

        // Act
        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, beanProperty);

        // Assert
        assertEquals(serializer, ToStringSerializer.instance);
    }
    
    @Test
    public void testCreateContextual_WithInvalidFormat_ReturnsNumberSerializer() throws JsonMappingException {
        // Arrange
        when(beanProperty.findPropertyFormat(any())).thenReturn(new JsonFormat.Value().withShape(JsonFormat.Shape.OBJECT));

        // Act
        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, beanProperty);

        // Assert
        assertEquals(serializer, numberSerializer);
    }

    @Test
    public void testSerialize_WithBigDecimal_WritesNumber() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("123.456");

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithBigInteger_WritesNumber() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("123456789012345678901234567890");

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithLong_WritesNumber() throws IOException {
        // Arrange
        Long value = 1234567890L;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithDouble_WritesNumber() throws IOException {
        // Arrange
        Double value = 123.456;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithFloat_WritesNumber() throws IOException {
        // Arrange
        Float value = 123.456f;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithInteger_WritesNumber() throws IOException {
        // Arrange
        Integer value = 123456789;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WithByte_WritesNumber() throws IOException {
        // Arrange
        Byte value = 127;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithInt_WritesIntFormat() throws JsonMappingException {
        // Arrange
        ContextualSerializer.SerializerContextualVisitor visitor = mock(JsonFormatVisitorWrapper.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, com.fasterxml.jackson.databind.JavaType.construct(Integer.class));

        // Assert
        verify(visitor).expectIntegerFormat(com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithBigDecimal_WritesFloatFormat() throws JsonMappingException {
        // Arrange
        ContextualSerializer.SerializerContextualVisitor visitor = mock(JsonFormatVisitorWrapper.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, com.fasterxml.jackson.databind.JavaType.construct(BigDecimal.class));

        // Assert
        verify(visitor).expectFloatFormat(com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithOtherType_WritesNumberFormat() throws JsonMappingException {
        // Arrange
        ContextualSerializer.SerializerContextualVisitor visitor = mock(JsonFormatVisitorWrapper.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, com.fasterxml.jackson.databind.JavaType.construct(Float.class));

        // Assert
        verify(visitor).expectNumberFormat(com.fasterxml.jackson.databind.JavaType.construct(Float.class));
    }

    @Test
    public void testGetSchema_WithInt_ReturnsIntegerSchemaNode() {
        // Arrange
        
        // Act
        JsonNode schema = numberSerializer.getSchema(serializerProvider, Integer.class);

        // Assert
        assertEquals(schema.toString(), "{\"type\":\"integer\"}");
    }

    @Test
    public void testGetSchema_WithBigDecimal_ReturnsNumberSchemaNode() {
        // Arrange
        
        // Act
        JsonNode schema = numberSerializer.getSchema(serializerProvider, BigDecimal.class);

        // Assert
        assertEquals(schema.toString(), "{\"type\":\"number\"}");
    }
}

