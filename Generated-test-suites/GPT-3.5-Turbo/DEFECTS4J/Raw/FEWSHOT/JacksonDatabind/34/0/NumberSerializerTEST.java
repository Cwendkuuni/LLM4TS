
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Captor
    private ArgumentCaptor<BigDecimal> bigDecimalCaptor;
    @Captor
    private ArgumentCaptor<BigInteger> bigIntegerCaptor;
    @Captor
    private ArgumentCaptor<Integer> integerCaptor;
    @Captor
    private ArgumentCaptor<Long> longCaptor;
    @Captor
    private ArgumentCaptor<Double> doubleCaptor;
    @Captor
    private ArgumentCaptor<Float> floatCaptor;
    @Captor
    private ArgumentCaptor<Number> numberCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_shouldWriteBigDecimal() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("123.45");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(bigDecimalCaptor.capture());
        assertEquals(value, bigDecimalCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteBigInteger() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("12345");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(bigIntegerCaptor.capture());
        assertEquals(value, bigIntegerCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteInteger() throws IOException {
        // Arrange
        Integer value = 123;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(integerCaptor.capture());
        assertEquals(value, integerCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteLong() throws IOException {
        // Arrange
        Long value = 123L;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(longCaptor.capture());
        assertEquals(value, longCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteDouble() throws IOException {
        // Arrange
        Double value = 123.45;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(doubleCaptor.capture());
        assertEquals(value, doubleCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteFloat() throws IOException {
        // Arrange
        Float value = 123.45f;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(floatCaptor.capture());
        assertEquals(value, floatCaptor.getValue());
    }

    @Test
    public void testSerialize_shouldWriteNumberAsString() throws IOException {
        // Arrange
        Number value = 123.45;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, generator, provider);

        // Assert
        verify(generator, times(1)).writeNumber(numberCaptor.capture().toString());
        assertEquals(value, numberCaptor.getValue());
    }

    @Test
    public void testGetSchema_shouldReturnIntegerSchemaNodeForBigInteger() {
        // Arrange
        BigInteger value = new BigInteger("12345");
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        JsonNode expectedSchemaNode = mock(JsonNode.class);

        // Act
        JsonNode actualSchemaNode = numberSerializer.getSchema(provider, typeHint);

        // Assert
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }

    @Test
    public void testGetSchema_shouldReturnNumberSchemaNodeForBigDecimal() {
        // Arrange
        BigDecimal value = new BigDecimal("123.45");
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        JsonNode expectedSchemaNode = mock(JsonNode.class);

        // Act
        JsonNode actualSchemaNode = numberSerializer.getSchema(provider, typeHint);

        // Assert
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldVisitIntFormatForBigInteger() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonParser.NumberType expectedNumberType = JsonParser.NumberType.BIG_INTEGER;

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitIntFormat(typeHint, expectedNumberType);
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldVisitFloatFormatForBigDecimal() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonParser.NumberType expectedNumberType = JsonParser.NumberType.BIG_DECIMAL;

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitFloatFormat(typeHint, expectedNumberType);
    }
}
