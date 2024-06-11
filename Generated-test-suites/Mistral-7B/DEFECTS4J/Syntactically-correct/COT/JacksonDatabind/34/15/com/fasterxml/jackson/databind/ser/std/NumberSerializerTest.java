package com.example.numberserializer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaValidator;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Test cases for NumberSerializer class
 */
public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1"
        assertTrue(System.out.toString().equals("1"));
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1L, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1"
        assertTrue(System.out.toString().equals("1"));
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.0f, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1.0"
        assertTrue(System.out.toString().equals("1.0"));
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.0, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1.0"
        assertTrue(System.out.toString().equals("1.0"));
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize((byte) 1, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1"
        assertTrue(System.out.toString().equals("1"));
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize((short) 1, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "1"
        assertTrue(System.out.toString().equals("1"));
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        BigInteger bigInteger = BigInteger.valueOf(12345678901234567890L);
        serializer.serialize(bigInteger, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "12345678901234567890"
        assertTrue(System.out.toString().equals("12345678901234567890"));
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        serializer.serialize(bigDecimal, generator, mapper.getSerializerProvider());
        generator.flush();
        // verify output is "123.456"
        assertTrue(System.out.toString().equals("123.456"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeUnsupportedType() throws IOException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize("test", generator, mapper.getSerializerProvider());
        serializer.serialize(generator, generator, mapper.getSerializerProvider());
        generator.flush();
        // this test case should throw an IllegalArgumentException
    }

    @Test
    public void testDeserializeInt() throws IOException {
        String jsonString = "1";
        Object readValue = mapper.readValue(jsonString, Number.class);
        assertEquals(1, readValue.intValue());
    }

    @Test
    public void testDeserializeLong() throws IOException {
        String jsonString = "1";
        Object readValue = mapper.readValue(jsonString, Long.class);
        assertEquals(1L, readValue.longValue());
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        String jsonString = "1.0";
        Object readValue = mapper.readValue(jsonString, Float.class);
        assertEquals(1.0f, readValue.floatValue(), 0.001f);
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        String jsonString = "1.0";
        Object readValue = mapper.readValue(jsonString, Double.class);
        assertEquals(1.0, readValue.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeByte() throws IOException {
        String jsonString = "1";
        Object readValue = mapper.readValue(jsonString, Byte.class);
        assertEquals((byte) 1, readValue.byteValue());
    }

    @Test
    public void testDeserializeShort() throws IOException {
        String jsonString = "1";
        Object readValue = mapper.readValue(jsonString, Short.class);
        assertEquals((short) 1, readValue.shortValue());
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        String jsonString = "12345678901234567890";
        Object readValue = mapper.readValue(jsonString, BigInteger.class);
        BigInteger expected = BigInteger.valueOf(12345678901234567890L);
        assertEquals(expected, readValue);
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        String jsonString = "123.456";
        Object readValue = mapper.readValue(jsonString, BigDecimal.class);
        BigDecimal expected = new BigDecimal("123.456");
        assertEquals(expected, readValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedType() throws IOException {
        String jsonString = "\"test\"";
        mapper.readValue(jsonString, Number.class);
        // this test case should throw a JsonMappingException
    }

    @Test
    public void testValidateSchema() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonNode schema = mapper.readTree(NumberSerializerTest.class.getResource("/number-schema.json"));
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonSchema jschema = serializer.getSchema(mapper.getSerializerProvider().getProviderInstance(JsonSchema.class), null);
        SchemaValidator validator = new SchemaValidator();
        validator.validate(schema, jschema);
        assertTrue(validator.isValid());
    }
}