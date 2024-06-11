package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    private JsonGenerator jsonGenerator = mock(JsonGenerator.class);
    private SerializerProvider serializerProvider = mock(SerializerProvider.class);

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123456.789");
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(bigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger bigInteger = new BigInteger("123456789");
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer integer = 123;
        numberSerializer.serialize(integer, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(integer.intValue());
    }

    // Add more test cases for other data types and edge cases...

    @Test
    public void testGetSchema() {
        assertEquals(numberSerializer.getSchema(serializerProvider, null).asText(), "number");
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        numberSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), serializerProvider.constructType(Number.class));
        // Add assertions based on the behavior of acceptJsonFormatVisitor method
    }
}