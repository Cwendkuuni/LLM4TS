package com.yourpackagename.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    @Test
    public void testNumberSerializer() throws JsonProcessingException {
        // Test serialization of Java Number types
        Number number = 123;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        ObjectWriter objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Number.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123", generator.getOutputContentAsString());

        number = 123L;
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Long.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123", generator.getOutputContentAsString());

        number = 123.0;
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Double.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123.0", generator.getOutputContentAsString());

        number = 123.0f;
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Float.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123.0", generator.getOutputContentAsString());

        number = 123.0d;
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Double.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123.0", generator.getOutputContentAsString());

        number = 123.0f;
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, Float.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123.0", generator.getOutputContentAsString());

        number = new BigInteger("123");
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, BigInteger.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123", generator.getOutputContentAsString());

        number = new BigDecimal("123.45");
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, BigDecimal.class);
        objectWriter.writeValue(generator, number);
        generator.flush();
        assertEquals("123.45", generator.getOutputContentAsString());

        // Test serialization of custom Number type
        @JsonSerialize(using = NumberSerializer.class)
        @JsonDeserialize(using = NumberDeserializer.class)
        static class CustomNumber {
            private Number value;

            public CustomNumber(Number value) {
                this.value = value;
            }

            public Number getValue() {
                return value;
            }
        }

        CustomNumber customNumber = new CustomNumber(123.45);
        generator = new JsonFactory().createGenerator(System.out);
        objectWriter = new JsonFactory().getSerializerProvider().getWriterWithView(generator, CustomNumber.class);
        objectWriter.writeValue(generator, customNumber);
        generator.flush();
        assertEquals("123.45", generator.getOutputContentAsString());

        // Test deserialization of JSON strings
        JsonParser parser = new JsonFactory().createParser("123");
        Number deserializedNumber = objectMapper.readValue(parser, Number.class);
        assertEquals(123, deserializedNumber.intValue());

        parser = new JsonFactory().createParser("123L");
        deserializedNumber = objectMapper.readValue(parser, Long.class);
        assertEquals(123, deserializedNumber.longValue());

        parser = new JsonFactory().createParser("123.0");
        deserializedNumber = objectMapper.readValue(parser, Double.class);
        assertEquals(123.0, deserializedNumber.doubleValue(), 0.0);

        parser = new JsonFactory().createParser("123.0f");
        deserializedNumber = objectMapper.readValue(parser, Float.class);
        assertEquals(123.0f, deserializedNumber, 0.0f);

        parser = new JsonFactory().createParser("123.0d");
        deserializedNumber = objectMapper.readValue(parser, Double.class);
        assertEquals(123.0, deserializedNumber.doubleValue(), 0.0);

        parser = new JsonFactory().createParser("123");
        CustomNumber customDeserializedNumber = objectMapper.readValue(parser, CustomNumber.class);
        assertEquals(123.0, customDeserializedNumber.getValue().doubleValue(), 0.0);

        parser = new JsonFactory().createParser("\"123.45\"");
        deserializedNumber = objectMapper.readValue(parser, Number.class);
        assertEquals(new BigDecimal("123.45"), deserializedNumber);

        parser = new JsonFactory().createParser("\"123.45E-2\"");
        deserializedNumber = objectMapper.readValue(parser, Number.class);
        assertEquals(new BigDecimal("0.012345"), deserializedNumber);

        parser = new JsonFactory().createParser("\"-123.45\"");
        deserializedNumber = objectMapper.readValue(parser, Number.class);
        assertEquals(new BigDecimal("-123.45"), deserializedNumber);
    }

    /**
     * Test deserialization of BigDecimal from JSON strings.
     */
    static class NumberDeserializer extends StdDeserializer<Number> {
        public NumberDeserializer() {
            this(null);
        }

        public NumberDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            TextReader reader = p.getCodec().getReaderFor(TextReader.class);
            String text = reader.readLine(p, null);
            return new BigDecimal(text);
        }
    }
}