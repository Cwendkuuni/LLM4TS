package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_withBigDecimal_shouldSerializeBigDecimal() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("123.456");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_withBigInteger_shouldSerializeBigInteger() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigInteger value = new BigInteger("123456");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_withInteger_shouldSerializeIntegerAsIntValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Integer value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withLong_shouldSerializeLongAsLongValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Long value = 123L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_withDouble_shouldSerializeDoubleAsDoubleValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Double value = 123.456;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_withFloat_shouldSerializeFloatAsFloatValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Float value = 123.456f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_withByte_shouldSerializeByteAsIntValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Byte value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withShort_shouldSerializeShortAsIntValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Short value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withOtherNumberType_shouldSerializeToString() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number value = new TestNumber();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema_withIntegerType_shouldReturnIntegerSchemaNode() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonNode expectedSchemaNode = new JsonNode();

        JsonNode result = numberSerializer.getSchema(serializerProvider, Integer.class);

        assertEquals(expectedSchemaNode, result);
    }

    @Test
    public void testGetSchema_withBigDecimalType_shouldReturnNumberSchemaNode() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonNode expectedSchemaNode = new JsonNode();

        JsonNode result = numberSerializer.getSchema(serializerProvider, BigDecimal.class);

        assertEquals(expectedSchemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIntType_shouldVisitIntFormat() throws IOException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).visitIntFormat(numberSerializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withBigDecimalType_shouldVisitFloatFormat() throws IOException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).visitFloatFormat(numberSerializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
}

class TestNumber extends Number {
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