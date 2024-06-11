
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NumberSerializerTest {
    private NumberSerializer serializer;
    private JsonGenerator generator;
    private SerializerProvider provider;

    @Before
    public void setup() {
        serializer = new NumberSerializer(Number.class);
        generator = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        Number value = new BigDecimal("123.45");
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber((BigDecimal) value);
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        Number value = new BigInteger("12345");
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber((BigInteger) value);
    }

    @Test
    public void testSerialize_Long() throws IOException {
        Number value = 12345L;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_Double() throws IOException {
        Number value = 123.45;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_Float() throws IOException {
        Number value = 123.45f;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        Number value = 12345;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        Number value = (byte) 123;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_Short() throws IOException {
        Number value = (short) 12345;
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_OtherNumber() throws IOException {
        Number value = 12345; // Example of an "other" number
        serializer.serialize(value, generator, provider);
        verify(generator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema_IntegerType() {
        serializer.getSchema(provider, Integer.class);
        verify(provider).createSchemaNode("integer", true);
    }

    @Test
    public void testGetSchema_BigDecimalType() {
        serializer.getSchema(provider, BigDecimal.class);
        verify(provider).createSchemaNode("number", true);
    }

    @Test
    public void testAcceptJsonFormatVisitor_IntegerType() throws IOException {
        serializer.acceptJsonFormatVisitor(null, com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(Integer.class));
        verify(generator).expectNumberFormat(com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(Integer.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigDecimalType() throws IOException {
        serializer.acceptJsonFormatVisitor(null, com.fasterxml.jackson.databinag.type.TypeFactory.defaultInstance().constructType(BigDecimal.class));
        verify(generator).expectNumberFormat(com.fasterxml.jackson.databinag.type.TypeFactory.defaultInstance().constructType(BigDecimal.class));
    }
}
