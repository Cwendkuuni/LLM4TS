package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.impl.ToStringSerializer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Mock
    private Class rawType;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonSerializer converter;

    @Mock
    private JsonNode schema;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private JsonFormatVisitorWrapper visitor;

    @Mock
    private JavaType javaType;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNumberSerializer() {
        numberSerializer = new NumberSerializer(rawType);
        assertEquals(rawType, numberSerializer.handledType());
        assertEquals(false, numberSerializer.isEmpty());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
  
        assertNull(numberSerializer.createContextual(serializerProvider, null));

        when(annotation.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(serializerProvider.findValueSerializer(Number.class, beanProperty)).thenReturn(converter);

        assertEquals(converter, numberSerializer.createContextual(serializerProvider, beanProperty));
    }

    @Test
    public void testSerialize() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = new BigDecimal("0.1");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_withBigDecimal() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        BigDecimal value = new BigDecimal("0.1");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_withBigInteger() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        BigInteger value = new BigInteger("100");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_withLong() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = 100L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_withDouble() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = 100.0;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_withFloat() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = 100.0f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_withInteger() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = 100;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withByte() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = (byte) 1;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withShort() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = (short) 1;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withUnknownType() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
        Number value = (String) "100";

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema_withIsInt() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
        when(rawType == BigInteger.class).thenReturn(true);

        JsonNode result = numberSerializer.getSchema(serializerProvider, null);

        assertEquals("integer", result);
    }

    @Test
    public void testGetSchema_withIsNotInt() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
        when(rawType == BigInteger.class).thenReturn(false);
        when(rawType == BigDecimal.class).thenReturn(true);

        JsonNode result = numberSerializer.getSchema(serializerProvider, null);

        assertEquals("number", result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIsInt() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
        when(rawType == BigInteger.class).thenReturn(true);

        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor, times(1)).visitIntFormat(eq(javaType), eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIsBigDecimal() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
        when(rawType == BigDecimal.class).thenReturn(true);

        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor, times(1)).visitFloatFormat(eq(javaType), eq(JsonParser.NumberType.BIG_DECIMAL));
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIsOtherType() throws JsonMappingException {
        numberSerializer = new NumberSerializer(rawType);
        when(rawType == BigInteger.class).thenReturn(false);

        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor, times(1)).expectNumberFormat(javaType);
    }

    @Test
    public void testDummyMethod() throws IOException {
        numberSerializer = new NumberSerializer(rawType);
    }
}