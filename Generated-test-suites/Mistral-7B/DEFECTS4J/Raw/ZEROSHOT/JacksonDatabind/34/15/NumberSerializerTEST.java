package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactoryBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNumberNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Unit test for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;

    public NumberSerializerTest() {
        mapper = new ObjectMapper(new JsonMapperFactoryBuilder().builder());
        nodeFactory = mapper.getNodeFactory();
    }

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        int intValue = 123;
        Number number = intValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "123";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        long longValue = 456;
        Number number = longValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "456";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        float floatValue = 789.1f;
        Number number = floatValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "789.1";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        double doubleValue = 1011.1234;
        Number number = doubleValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "1011.1234";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        BigDecimal bigDecimalValue = new BigDecimal("12345.6789");
        Number number = bigDecimalValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "12345.6789";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        BigInteger bigIntegerValue = new BigInteger("12345678901234567890");
        Number number = bigIntegerValue;

        serializer.serialize(number, provider);
        generator.flush();

        String expectedJson = "12345678901234567890";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeUnsupportedNumberType()
            throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        provider.setCurrentOutput(generator);

        Byte byteValue = new Byte("12");
        Number number = byteValue;

        try {
            serializer.serialize(number, provider);
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            generator.flush();
        }

        String expectedJson = "";
        String actualJson = generator.getOutput().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("123"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("123");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        assertEquals(123, deserializedNumber.intValue());
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("456"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("456");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        assertEquals(456, deserializedNumber.longValue());
    }

    @Test
    public void testDeserializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("789.1"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("789.1");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        assertEquals(789.1f, deserializedNumber.floatValue(), 0.0f);
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("1011.1234"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("1011.1234");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        assertEquals(1011.1234, deserializedNumber.doubleValue(), 0.0);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("12345.6789"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("12345.6789");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        BigDecimal expectedBigDecimal = new BigDecimal("12345.6789");
        BigDecimal actualBigDecimal = ((BigDecimal) deserializedNumber).stripTrailingZeros();

        assertEquals(expectedBigDecimal, actualBigDecimal);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("12345678901234567890"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("12345678901234567890");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);

        BigInteger expectedBigInteger = new BigInteger("12345678901234567890");
        BigInteger actualBigInteger = ((BigInteger) deserializedNumber).stripTrailingZeros();

        assertEquals(expectedBigInteger, actualBigInteger);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedNumberType()
            throws JsonGenerationException, JsonMappingException, IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("12"));

        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        JsonNumberNode numberNode = (JsonNumberNode) node.get("12");

        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(numberNode.getValueAsString());
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        provider.setCurrentOutput(generator);

        Number deserializedNumber = serializer.deserialize(jsonParser, Number.class);
        provider.setCurrentOutput(null);
    }
}