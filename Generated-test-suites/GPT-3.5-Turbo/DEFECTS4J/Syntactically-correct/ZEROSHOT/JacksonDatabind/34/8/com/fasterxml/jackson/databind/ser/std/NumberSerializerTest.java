package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Mock
    private Number value;

    @Mock
    private JsonGenerator g;

    @Mock
    private SerializerProvider provider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        serializer.serialize(bigDecimal, g, provider);
        verify(g).writeNumber(bigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger bigInteger = new BigInteger("1234567890");
        serializer.serialize(bigInteger, g, provider);
        verify(g).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        int intValue = 123;
        Integer integer = new Integer(intValue);
        serializer.serialize(integer, g, provider);
        verify(g).writeNumber(intValue);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        long longValue = 1234567890L;
        Long longObj = new Long(longValue);
        serializer.serialize(longObj, g, provider);
        verify(g).writeNumber(longValue);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        double doubleValue = 12.34;
        Double doubleObj = new Double(doubleValue);
        serializer.serialize(doubleObj, g, provider);
        verify(g).writeNumber(doubleValue);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        float floatValue = 12.34f;
        Float floatObj = new Float(floatValue);
        serializer.serialize(floatObj, g, provider);
        verify(g).writeNumber(floatValue);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        byte byteValue = 123;
        Byte byteObj = new Byte(byteValue);
        serializer.serialize(byteObj, g, provider);
        verify(g).writeNumber(byteValue);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        short shortValue = 123;
        Short shortObj = new Short(shortValue);
        serializer.serialize(shortObj, g, provider);
        verify(g).writeNumber(shortValue);
    }

    @Test
    public void testSerializeFallback() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        String value = "123.45";
        serializer.serialize(value, g, provider);
        verify(g).writeNumber(value);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(provider, null);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorFloat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).visitFloatFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorFallback() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectNumberFormat(eq(typeHint));
    }
}