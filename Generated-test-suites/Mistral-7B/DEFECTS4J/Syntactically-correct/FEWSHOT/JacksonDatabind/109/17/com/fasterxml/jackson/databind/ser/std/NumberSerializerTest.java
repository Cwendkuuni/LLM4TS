package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    /**
     * Static instance that is only to be used for {@link java.lang.Number}.
     */
    private static final NumberSerializer INSTANCE = new NumberSerializer(Number.class);

    @Test
    public void testSerializedNumber() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        Number number = 123;
        INSTANCE.serialize(number, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(number, parser.readValueAs(Number.class));
    }

    @Test
    public void testSerializedBigDecimal() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        INSTANCE.serialize(bigDecimal, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(bigDecimal, parser.readValueAs(BigDecimal.class));
    }

    @Test
    public void testSerializedBigInteger() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        INSTANCE.serialize(bigInteger, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(bigInteger, parser.readValueAs(BigInteger.class));
    }

    @Test
    public void testSerializedLong() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        long longNumber = 123;
        INSTANCE.serialize(longNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(longNumber, parser.readValueAs(long.class));
    }

    @Test
    public void testSerializedDouble() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        double doubleNumber = 123.456;
        INSTANCE.serialize(doubleNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(doubleNumber, parser.readValueAs(Double.class), 0.001);
    }

    @Test
    public void testSerializedFloat() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        float floatNumber = 123.456f;
        INSTANCE.serialize(floatNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(floatNumber, parser.readValueAs(Float.class), 0.001f);
    }

    @Test
    public void testSerializedInteger() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        int integerNumber = 123;
        INSTANCE.serialize(integerNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(integerNumber, parser.readValueAs(int.class));
    }

    @Test
    public void testSerializedByte() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        byte byteNumber = (byte) 123;
        INSTANCE.serialize(byteNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(byteNumber, parser.readValueAs(byte.class));
    }

    @Test
    public void testSerializedShort() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        short shortNumber = (short) 123;
        INSTANCE.serialize(shortNumber, generator, new ObjectMapper().getSerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(System.in);
        assertEquals(shortNumber, parser.readValueAs(short.class));
    }

    @Test
    public void testCreateContextualSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat format = JsonFormat.value().withShape(JsonFormat.Shape.STRING);
        JsonSerializer<Number> serializer = mapper.findContextualSerializer(JsonParser.NumberType.BIG_DECIMAL);
        assertEquals(ToStringSerializer.instance, serializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().findValueSerializerProvider(JsonParser.NumberType.BIG_INTEGER).getSerializerProvider().getJsonFormatVisitor(JsonMapping.Type.NUMBER);
        assertTrue(INSTANCE.acceptJsonFormatVisitor(visitor, BigInteger.class.getType()));
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithUnsupportedType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().findValueSerializerProvider(JsonParser.NumberType.BIG_INTEGER).getSerializerProvider().getJsonFormatVisitor(JsonMapping.Type.STRING);
        INSTANCE.acceptJsonFormatVisitor(visitor, String.class.getType());
    }
}