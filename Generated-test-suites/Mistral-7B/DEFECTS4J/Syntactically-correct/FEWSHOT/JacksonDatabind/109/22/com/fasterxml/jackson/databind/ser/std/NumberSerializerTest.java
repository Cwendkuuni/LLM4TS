package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for NumberSerializer class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    /**
     * Test for default serialization behavior.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testDefaultSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("integer", 123);
        node.put("long", Long.MAX_VALUE);
        node.put("float", 1.2f);
        node.put("double", 3.14159265359);
        node.put("bigDecimal", new BigDecimal("123.456"));
        node.put("bigInteger", new BigInteger("12345678901234567890"));
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test serialization with custom Number type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testCustomNumberTypeSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("customNumber", new MyNumber(123));
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test serialization with {@link BigDecimal} type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testBigDecimalSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("bigDecimal", new BigDecimal("123.456"));
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test serialization with {@link BigInteger} type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testBigIntegerSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("bigInteger", new BigInteger("12345678901234567890"));
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test JSON format visitor when handling {@link BigDecimal} type.
     */
    @Test
    public void testJsonFormatVisitorBigDecimal() {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getVisibleAnnotationIntrospector().findAnnotation(Number.class, node);
        assertNotNull(visitor);
        visitor.expectNumberFormat(node.getType());
    }

    /**
     * Test JSON format visitor when handling other Number types.
     */
    @Test
    public void testJsonFormatVisitorOtherNumberTypes() {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getVisibleAnnotationIntrospector().findAnnotation(Integer.class, node);
        assertNotNull(visitor);
        visitor.expectNumberFormat(node.getType());

        visitor = factory.getSerializerProvider().getVisibleAnnotationIntrospector().findAnnotation(Double.class, node);
        assertNotNull(visitor);
        visitor.expectNumberFormat(node.getType());

        visitor = factory.getSerializerProvider().getVisibleAnnotationIntrospector().findAnnotation(Float.class, node);
        assertNotNull(visitor);
        visitor.expectNumberFormat(node.getType());
    }

    /**
     * Test for the case when the format visitor is not found.
     */
    @Test
    public void testJsonFormatVisitorNotFound() {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getVisibleAnnotationIntrospector().findAnnotation(String.class, node);
        assertNull(visitor);
    }

    /**
     * Test contextual serialization.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testContextualSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("contextualNumber", new ContextualNumber(123));
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test serialization with null value.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testNullValueSerialization() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("nullNumber", null);
        mapper.writeValue(gen, node);
        gen.close();
    }

    /**
     * Test deserialization with {@link BigDecimal} type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testBigDecimalDeserialization() throws IOException {
        JsonParser parser = new JsonFactory().createParser(getClass().getResource("/number-serializer-test.json"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readTree(parser);
        assertEquals(new BigDecimal("123.456"), node.path("bigDecimal").asText(new BigDecimalTextDeserializer()));
        parser.close();
    }

    /**
     * Test deserialization with {@link BigInteger} type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testBigIntegerDeserialization() throws IOException {
        JsonParser parser = new JsonFactory().createParser(getClass().getResource("/number-serializer-test.json"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readTree(parser);
        assertEquals(new BigInteger("12345678901234567890"), node.path("bigInteger").asText(new BigIntegerTextDeserializer()));
        parser.close();
    }

    /**
     * Test deserialization with custom Number type.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testCustomNumberTypeDeserialization() throws IOException {
        JsonParser parser = new JsonFactory().createParser(getClass().getResource("/number-serializer-test.json"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readTree(parser);
        assertEquals(new MyNumber(123), node.path("customNumber").as(MyNumber.class));
        parser.close();
    }

    /**
     * Test deserialization with null value.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testNullValueDeserialization() throws IOException {
        JsonParser parser = new JsonFactory().createParser(getClass().getResource("/number-serializer-test.json"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readTree(parser);
        assertNull(node.path("nullNumber"));
        parser.close();
    }

    /**
     * Test schema generation for integer type.
     */
    @Test
    public void testSchemaGenerationInteger() {
        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonSchema schema = serializer.schemaProvider.getDefaultSerializerProvider().getSchemaFactory().getSchema(serializer, Number.class);
        assertEquals("integer", schema.getType());
    }

    /**
     * Test schema generation for {@link BigDecimal} type.
     */
    @Test
    public void testSchemaGenerationBigDecimal() {
        JsonSerializer<Number> serializer = new NumberSerializer(BigDecimal.class);
        JsonSchema schema = serializer.schemaProvider.getDefaultSerializerProvider().getSchemaFactory().getSchema(serializer, BigDecimal.class);
        assertEquals("number", schema.getType());
    }

    /**
     * Test schema generation for other Number types.
     */
    @Test
    public void testSchemaGenerationOtherNumberTypes() {
        JsonSerializer<Number> serializer = new NumberSerializer(Integer.class);
        JsonSchema schema = serializer.schemaProvider.getDefaultSerializerProvider().getSchemaFactory().getSchema(serializer, Integer.class);
        assertEquals("integer", schema.getType());

        serializer = new NumberSerializer(Double.class);
        schema = serializer.schemaProvider.getDefaultSerializerProvider().getSchemaFactory().getSchema(serializer, Double.class);
        assertEquals("number", schema.getType());

        serializer = new NumberSerializer(Float.class);
        schema = serializer.schemaProvider.getDefaultSerializerProvider().getSchemaFactory().getSchema(serializer, Float.class);
        assertEquals("number", schema.getType());
    }

    /**
     * Custom Number class for testing.
     */
    static class MyNumber extends Number {
        private int value;

        public MyNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    /**
     * Custom Number deserializer for testing.
     */
    static class MyNumberDeserializer extends JsonDeserializer<MyNumber> {
        @Override
        public MyNumber deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            int value = p.getIntValue();
            return new MyNumber(value);
        }
    }

    /**
     * Custom BigDecimal deserializer for testing.
     */
    static class BigDecimalTextDeserializer extends JsonDeserializer<BigDecimal> {
        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new BigDecimal(p.getValueAsString());
        }
    }

    /**
     * Custom BigInteger deserializer for testing.
     */
    static class BigIntegerTextDeserializer extends JsonDeserializer<BigInteger> {
        @Override
        public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new BigInteger(p.getValueAsString());
        }
    }

    /**
     * Custom Number class for testing contextual serialization.
     */
    static class ContextualNumber extends Number implements ContextualSerializable {
        private int value;

        public ContextualNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

        @Override
        public ContextualSerializer<?> getContextualSerializer() {
            return new ContextualNumberSerializer();
        }

        static class ContextualNumberSerializer extends JsonSerializer<ContextualNumber> {
            @Override
            public void serialize(ContextualNumber value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeNumber(value.intValue());
            }
        }
    }
}