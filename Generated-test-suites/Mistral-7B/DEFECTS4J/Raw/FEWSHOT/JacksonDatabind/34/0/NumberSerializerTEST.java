package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.mapper;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_NUMBERS_AS_STRINGS);

        JsonSerializer<Number> serializer = NumberSerializer.instance;

        JsonGenerator generator = mapper.getFactory().createGenerator();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonSerializer<Number> numberSerializerInt = numberSerializer;

        // Test serialization of various Number types
        testSerialize(generator, serializerProvider, serializer, null, null, (Object) null);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Integer(1), "1", 1);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Long(2), "2", 2);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Double(3.5), "3.5", 3.5);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Float(4.0f), "4.0", 4.0f);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Byte((byte) 5), "5", 5);
        testSerialize(generator, serializerProvider, numberSerializerInt, new Short((short) 6), "6", 6);
        testSerialize(generator, serializerProvider, numberSerializerInt, new BigDecimal("7.8"), "7.8", 7.8);
        testSerialize(generator, serializerProvider, numberSerializerInt, new BigInteger("9"), "9", 9);
        testSerialize(generator, serializerProvider, numberSerializerInt, new BigInteger("100"), "100", 100);
        testSerialize(generator, serializerProvider, numberSerializerInt, new BigDecimal("123.45"), "123.45", 123.45);

        // Test serialization of custom Number types
        Number customNumber = new MyNumber(123);
        testSerialize(generator, serializerProvider, numberSerializerInt, customNumber, "123", 123);
    }

    private void testSerialize(JsonGenerator generator, SerializerProvider serializerProvider, JsonSerializer<Number> serializer,
            Number number, String expectedString, Object expectedNumber) throws IOException {
        if (number != null) {
            serializer.serialize(number, generator, serializerProvider);
            assertEquals(expectedString, generator.getOutputBuffer().toString());
            assertEquals(expectedNumber, generator.getOutputBuffer().toStringToNumber());
            generator.flush();
            generator.getOutputBuffer().clear();
        } else {
            generator.writeNull();
            assertEquals(JsonNodeFactory.instance.nullNode(), generator.getOutputBuffer().readValueAsTree());
        }
    }
}