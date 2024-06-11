package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeValueBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.5");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueBigInteger() throws IOException {
        BigInteger value = new BigInteger("100");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueInteger() throws IOException {
        Integer value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueLong() throws IOException {
        Long value = Long.MAX_VALUE;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueDouble() throws IOException {
        Double value = 10.5;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueFloat() throws IOException {
        Float value = 10.5f;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueByte() throws IOException {
        Byte value = Byte.MAX_VALUE;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueShort() throws IOException {
        Short value = Short.MAX_VALUE;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testSerializeValueString() throws IOException {
        String value = "10";
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        //assert output
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Number.class;
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        //assert schema
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        //assert visitIntFormat
    }

    @Test
    public void testAcceptJsonFormatVisitorOther() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        //assert visitFloatFormat
    }
}