package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    @Test
    public void testSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        NumberSerializer serializer = NumberSerializer.instance;

        // Test serialization for various Number types
        serializer.serialize(123, gen, new SerializerProvider());
        serializer.serialize(123L, gen, new SerializerProvider());
        serializer.serialize(new BigInteger("123"), gen, new SerializerProvider());
        serializer.serialize(new BigDecimal("123.45"), gen, new SerializerProvider());
        serializer.serialize(123d, gen, new SerializerProvider());
        serializer.serialize(123f, gen, new SerializerProvider());
        serializer.serialize(new Integer(123), gen, new SerializerProvider());
        serializer.serialize(new Byte((byte) 123), gen, new SerializerProvider());
        serializer.serialize(new Short((short) 123), gen, new SerializerProvider());
        serializer.serialize(new Double(123.45), gen, new SerializerProvider());
        serializer.serialize(new Float(123.45f), gen, new SerializerProvider());
        serializer.serialize(new BigDecimal("123.45"), gen, new SerializerProvider());
        serializer.serialize(new BigInteger("123456789012345678901234567890"), gen, new SerializerProvider());

        // Test serialization for custom Number types
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        static class CustomNumber extends Number {
            private final int value;

            public CustomNumber(int value) {
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
        }

        CustomNumber customNumber = new CustomNumber(123);
        serializer.serialize(customNumber, gen, new SerializerProvider());

        gen.close();
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        JsonParser p = new JsonFactory().createParser(getClass().getResource("/number-serializer-test.json"));
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(p);

        // Test deserialization for various Number types
        assertEquals(123, mapper.readValue(node.get(0).asText(), Number.class).intValue());
        assertEquals(123L, mapper.readValue(node.get(1).asText(), Number.class).longValue());
        assertEquals(new BigInteger("123"), mapper.readValue(node.get(2).asText(), BigInteger.class));
        assertEquals(new BigDecimal("123.45"), mapper.readValue(node.get(3).asText(), BigDecimal.class));
        assertEquals(123d, mapper.readValue(node.get(4).asText(), Double.class));
        assertEquals(123f, mapper.readValue(node.get(5).asText(), Float.class));
        assertEquals(new Integer(123), mapper.readValue(node.get(6).asText(), Integer.class));
        assertEquals(new Byte((byte) 123), mapper.readValue(node.get(7).asText(), Byte.class));
        assertEquals(new Short((short) 123), mapper.readValue(node.get(8).asText(), Short.class));
        assertEquals(new Double(123.45), mapper.readValue(node.get(9).asText(), Double.class));
        assertEquals(new Float(123.45f), mapper.readValue(node.get(10).asText(), Float.class));
        assertEquals(new BigDecimal("123.45"), mapper.readValue(node.get(11).asText(), BigDecimal.class));
        assertEquals(new BigInteger("123456789012345678901234567890"), mapper.readValue(node.get(12).asText(), BigInteger.class));

        // Test deserialization for custom Number types
        @JsonDeserialize(using = CustomNumberDeserializer.class)
        static class CustomNumberDeserializer extends NumberDeserializers.BaseNumberDeserializer {
            @Override
            protected Number fromString(String text) throws IOException {
                String[] parts = text.split(":");
                int value = Integer.parseInt(parts[0]);
                return new CustomNumber(value);
            }
        }

        assertEquals(new CustomNumber(123), mapper.readValue(node.get(13).asText(), CustomNumber.class));
    }

    @Test
    public void testContextualSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.valueToTree(new Object());
        JsonGenerator gen = mapper.treeAsGenerator(node);

        // Test contextual serialization for BigDecimal
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.serialize(new BigDecimal("123.45"), gen, new SerializerProvider());
        gen.close();

        JsonParser p2 = mapper.treeAsParser(mapper.readTree(node.toString()));
        assertEquals(TypeFactory.defaultInstance().constructType(BigDecimal.class), mapper.readValue(p2, Type.CLASS));
    }
}