package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NumberSerializerTest {
  
    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_WithBigDecimal_ShouldWriteNumber() throws IOException {
        BigDecimal value = new BigDecimal(1.0);
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WithBigInteger_ShouldWriteNumber() throws IOException {
        BigInteger value = new BigInteger("1234567890");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WithInteger_ShouldWriteNumber() throws IOException {
        Integer value = 1;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WithLong_ShouldWriteNumber() throws IOException {
        Long value = 100L;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_WithDouble_ShouldWriteNumber() throws IOException {
        Double value = 1.5;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_WithFloat_ShouldWriteNumber() throws IOException {
        Float value = 1.5f;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_WithByte_ShouldWriteNumber() throws IOException {
        Byte value = (byte) 1;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WithShort_ShouldWriteNumber() throws IOException {
        Short value = (short) 1;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WithOtherNumberType_ShouldWriteNumberToString() throws IOException {
        Number value = mock(Number.class);
        String expectedValue = value.toString();

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(expectedValue);
    }

    @Test
    public void testGetSchema_WithBigInteger_ShouldCreateSchemaNodeWithIntegerType() {
        BigInteger value = new BigInteger("1234567890");
        JsonNode schemaNode = numberSerializer.getSchema(mockSerializerProvider, value.getClass());
        assertEquals("integer", schemaNode.get("type").textValue());
    }

    @Test
    public void testGetSchema_WithBigDecimal_ShouldCreateSchemaNodeWithNumberType() {
        BigDecimal value = new BigDecimal(1.5);
        JsonNode schemaNode = numberSerializer.getSchema(mockSerializerProvider, value.getClass());
        assertEquals("number", schemaNode.get("type").textValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithBigInteger_ShouldVisitIntFormat() throws JsonMappingException {
        BigInteger value = new BigInteger("1234567890");
        JavaType javaType = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor, times(1)).expectNumberFormat(javaType);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithBigDecimal_ShouldVisitFloatFormat() throws JsonMappingException {
        BigDecimal value = new BigDecimal(1.5);
        JavaType javaType = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor, times(1)).expectNumberFormat(javaType);
    }
}