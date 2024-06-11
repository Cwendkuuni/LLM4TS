package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation. JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = 1;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("1", mapper.readTree(System.in).asText());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = 1L;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("1", mapper.readTree(System.in).asText());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = 3.14;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("3.14", mapper.readTree(System.in).asText());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = BigInteger.valueOf(12345678901234567890L);
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("12345678901234567890", mapper.readTree(System.in).asText());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = new BigDecimal("12345.6789012345");
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("12345.6789012345", mapper.readTree(System.in).asText());
    }

    @Test
    public void testSerializeCustomNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Number number = new MyCustomNumber(123);
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("123", mapper.readTree(System.in).asText());
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = JsonMapper.builder().build();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);
        visitor.expectNumberFormat(null).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.INT).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.LONG).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.BIG_INTEGER).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.FLOAT).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.DOUBLE).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.BIG_DECIMAL).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.TEXT).andReturn(visitor);
        visitor.expectNull();

        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);
        visitor.expectNumberFormat(null).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.INT).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.LONG).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.BIG_INTEGER).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.FLOAT).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.DOUBLE).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.BIG_DECIMAL).andReturn(visitor);
        visitor.expectNumberFormat(Number.class, JsonParser.NumberType.TEXT).andReturn(visitor);
        visitor.expectNull();

        serializer.acceptJsonFormatVisitor(visitor, BigInteger.class);
    }

    @JsonSerialize(using = NumberSerializer.class)
    @JsonDeserialize(using = NumberDeserializer.class)
    static class MyCustomNumber {
        private final int value;

        public MyCustomNumber(int value) {
            this.value = value;
        }

        @JsonProperty("value")
        public int getValue() {
            return value;
        }
    }

    private static JsonFormatVisitorWrapper createMockJsonFormatVisitor() {
        return createMock(JsonFormatVisitorWrapper.class);
    }
}