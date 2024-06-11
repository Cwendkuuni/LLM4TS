package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of BigDecimal
        BigDecimal bigDecimal = new BigDecimal("123456.789");
        serializer.serialize(bigDecimal, generator, new SerializerProvider());
        generator.flush();
        assertEquals("123456.789", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of BigInteger
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        serializer.serialize(bigInteger, generator, new SerializerProvider());
        generator.flush();
        assertEquals("12345678901234567890", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of int
        int intValue = 123;
        serializer.serialize(intValue, generator, new SerializerProvider());
        generator.flush();
        assertEquals("123", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of double
        double doubleValue = 123.45;
        serializer.serialize(doubleValue, generator, new SerializerProvider());
        generator.flush();
        assertEquals("123.45", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeStringAsNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of String as Number
        String stringValue = "123456.789";
        serializer.serialize(stringValue, generator, new SerializerProvider());
        generator.flush();
        assertEquals("123456.789", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeUnsupportedType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;

        // test serialization of unsupported type
        String unsupportedType = "UnsupportedType";
        try {
            serializer.serialize(unsupportedType, generator, new SerializerProvider());
            generator.flush();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeBigDecimal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of BigDecimal
        String jsonString = "123456.789";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigDecimal(jsonString), deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of BigInteger
        String jsonString = "12345678901234567890";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigInteger(jsonString), deserializedNumber);
    }

    @Test
    public void testDeserializeInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of int
        String jsonString = "123";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new Integer(jsonString), deserializedNumber);
    }

    @Test
    public void testDeserializeDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of double
        String jsonString = "123.45";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new Double(jsonString), deserializedNumber);
    }

    @Test
    public void testDeserializeStringAsNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of String as Number
        String jsonString = "123456.789";
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigDecimal(jsonString), deserializedNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // test deserialization of unsupported type
        String jsonString = "UnsupportedType";
        mapper.readValue(jsonString, Number.class);
    }
}