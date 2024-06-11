package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        short testValue = 123;
        ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        int testValue = 456;
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("456", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        int testValue = 789;
        Number testNumber = 789;
        IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testNumber, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("789", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        long testValue = 1234567890L;
        LongSerializer serializer = new LongSerializer(Long.class);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("1234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        float testValue = 123.45f;
        FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        double testValue = 123.456789;
        DoubleSerializer serializer = new DoubleSerializer(Double.class);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        gen.close();

        assertEquals("123.456789", gen.getOutputContent().toString());
    }

    @Test
    public void testBaseClass() {
        Base<Object> serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        assertNotNull(serializer);
    }

    @Test
    public void testContextualSerialization() throws JsonProcessingException {
        @JsonFormat(with = JsonFormat.Feature.WRITE_NULL_NUMBER_AS_ZERO)
        int nullableTestValue = 456;
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.readValue("{\"num\": null}", Map.class).get("num");
        int deserializedValue = mapper.readerForUpdating(result).readValueAs(Integer.class);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(nullableTestValue, gen, new ObjectMapper().getSerializerProvider());
        gen.writeObject(mapper.valueToTree(result));
        gen.close();

        assertEquals("456", gen.getOutputContent().toString());
    }
}