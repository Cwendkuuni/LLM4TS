package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testContextualSerialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> serializer = mapper.findValueSerializer(BigDecimal.class);
        assertSame(NumberSerializer.instance, serializer);

        serializer = mapper.findValueSerializer(BigInteger.class);
        assertSame(NumberSerializer.instance, serializer);

        serializer = mapper.findValueSerializer(int[].class);
        assertNotSame(NumberSerializer.instance, serializer);

        serializer = mapper.findValueSerializer(float[].class);
        assertNotSame(NumberSerializer.instance, serializer);

        serializer = mapper.findValueSerializer(String[].class);
        assertNotSame(NumberSerializer.instance, serializer);
    }

    @Test
    public void testSerializeBasicTypes() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getGenerator();

        for (Number number : new Number[] { 1L, 1.0F, 1.0, 1, BigDecimal.ONE, BigInteger.ONE }) {
            String expectedOutput = number.toString();
            assertEquals(expectedOutput, gen.writeValueAsString(number));
        }
    }

    @Test
    public void testSerializeBigDecimal() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getGenerator();

        BigDecimal bd = new BigDecimal("1234.5678");
        String expectedOutput = bd.toString();
        assertEquals(expectedOutput, gen.writeValueAsString(bd));
    }

    @Test
    public void testSerializeBigInteger() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getGenerator();

        BigInteger bi = new BigInteger("123456789012345678901234567890");
        String expectedOutput = bi.toString();
        assertEquals(expectedOutput, gen.writeValueAsString(bi));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);

        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class.asSubType(Number.class));

        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.hasHandledNumberFormat());

        serializer = new NumberSerializer(BigInteger.class.asSubType(Number.class));
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.hasHandledNumberFormat());

        serializer = new NumberSerializer(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertFalse(visitor.hasHandledNumberFormat());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = new NumberSerializer(Number.class.asSubType(Number.class)).getSchema(mapper.getSerializerProvider(), null);
        assertEquals("number", schema.toString().trim());
    }
}