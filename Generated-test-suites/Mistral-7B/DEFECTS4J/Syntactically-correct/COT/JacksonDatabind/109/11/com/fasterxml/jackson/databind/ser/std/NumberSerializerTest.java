package com.example.junit.numberserializer;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = JsonMapperFactory.getMapper();

    /**
     * Test method for {@link NumberSerializer#NumberSerializer(Class)}.
     */
    @Test
    public void testNumberSerializerConstructor() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        assertNotNull(numberSerializer);
    }

    /**
     * Test method for {@link NumberSerializer#createContextual(SerializerProvider, BeanProperty)}.
     */
    @Test
    public void testCreateContextual() {
        SerializerProvider serializerProvider = new SerializerProvider() {};
        BeanProperty property = new BeanProperty(Number.class, "number");
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, property);
        assertSame(numberSerializer, serializer);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Integer testValue = 123;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Long testValue = 123L;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Float testValue = 123.45f;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Double testValue = 123.45;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigDecimal testValue = new BigDecimal("123.45");
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigInteger testValue = new BigInteger("123456789012345678901234567890");
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(testValue, jsonGenerator, new SerializerProvider() {});
        assertEquals("123456789012345678901234567890", jsonGenerator.getOutputBuffer().toString());
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNull() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator();

        numberSerializer.serialize(null, jsonGenerator, new SerializerProvider() {});
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
     */
    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        assertEquals("number", numberSerializer.getSchema(new SerializerProvider() {}, Number.class).toString());
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)}.
     */
    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null;
        assertNull(numberSerializer.acceptJsonFormatVisitor(visitor, Number.class));
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} with BigDecimal.
     */
    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimal() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null;
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_DECIMAL;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        format.setNumberType(numberType);
        visitor.expectNumberFormat(Number.class, format);

        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertNotNull(visitor.getFormatContext());
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} with BigInteger.
     */
    @Test
    public void testAcceptJsonFormatVisitorWithBigInteger() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null;
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_INTEGER;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        format.setNumberType(numberType);
        visitor.expectNumberFormat(Number.class, format);

        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertNotNull(visitor.getFormatContext());
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} with Currency.
     */
    @Test
    public void testAcceptJsonFormatVisitorWithCurrency() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null;
        JsonParser.NumberType numberType = JsonParser.NumberType.TEXT;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        format.setNumberType(numberType);
        visitor.expectNumberFormat(Number.class, format);

        numberSerializer.acceptJsonFormatVisitor(visitor, Currency.class);
        assertNotNull(visitor.getFormatContext());
    }
}