package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_WhenValueIsBigDecimal_ShouldWriteNumber() throws IOException {
        BigDecimal value = new BigDecimal("123.45");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value);
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsBigInteger_ShouldWriteNumber() throws IOException {
        BigInteger value = new BigInteger("12345");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value);
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsInteger_ShouldWriteNumber() throws IOException {
        Integer value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsLong_ShouldWriteNumber() throws IOException {
        Long value = 123L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.longValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsDouble_ShouldWriteNumber() throws IOException {
        Double value = 123.45;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.doubleValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsFloat_ShouldWriteNumber() throws IOException {
        Float value = 123.45f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.floatValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsByte_ShouldWriteNumber() throws IOException {
        Byte value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsShort_ShouldWriteNumber() throws IOException {
        Short value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WhenValueIsOtherNumber_ShouldWriteNumberAsString() throws IOException {
        Number value = new CustomNumber();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.toString());
        verifyNoMoreInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetSchema_WhenIsInt_ShouldReturnIntegerSchemaNode() {
        when(serializerProvider.createSchemaNode(eq("integer"), eq(true))).thenReturn(mock(JsonNode.class));

        JsonNode result = numberSerializer.getSchema(serializerProvider, null);

        verify(serializerProvider, times(1)).createSchemaNode("integer", true);
        verifyNoMoreInteractions(serializerProvider);
        assertEquals("integer", result.asText());
    }

    @Test
    public void testGetSchema_WhenNotInt_ShouldReturnNumberSchemaNode() {
        when(serializerProvider.createSchemaNode(eq("number"), eq(true))).thenReturn(mock(JsonNode.class));

        JsonNode result = numberSerializer.getSchema(serializerProvider, null);

        verify(serializerProvider, times(1)).createSchemaNode("number", true);
        verifyNoMoreInteractions(serializerProvider);
        assertEquals("number", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenIsInt_ShouldVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenIsBigDecimal_ShouldVisitFloatFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(typeHint.getRawClass()).thenReturn(BigDecimal.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).visitFloatFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenIsNotIntOrBigDecimal_ShouldExpectNumberFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(typeHint.getRawClass()).thenReturn(Double.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectNumberFormat(eq(typeHint));
        verifyNoMoreInteractions(visitor);
    }

    private class CustomNumber extends Number {

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }
}