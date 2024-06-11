package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = 1;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1", jsonParser.getText());
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = Long.valueOf(1L);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1", jsonParser.getText());
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = Double.valueOf(1.0);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1.0", jsonParser.getText());
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = Float.valueOf(1.0f);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1.0", jsonParser.getText());
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = BigInteger.valueOf(1L);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1", jsonParser.getText());
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = new BigDecimal("1");
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(JsonNodeType.NUMBER, jsonParser.getCurrentNode().getNodeType());
        assertEquals("1", jsonParser.getText());
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = new Object() {
            @JsonFormat(shape = JsonFormat.Shape.NUMBER)
            public Number getValue() {
                return 1;
            }
        };
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        try {
            serializer.serialize(number, jsonGenerator, null);
        } catch (IOException e) {
            assertTrue(e.getCause() instanceof JsonMappingException);
        }
    }

    @Test
    public void testDeserializeInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", 1);
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(1, deserializedNumber.intValue());
        assertEquals("1", deserializedNumber.toString());
    }

    @Test
    public void testDeserializeLong() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", 1L);
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(1L, deserializedNumber.longValue());
        assertEquals("1", deserializedNumber.toString());
    }

    @Test
    public void testDeserializeDouble() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", 1.0);
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(1.0, deserializedNumber.doubleValue(), 0.0);
        assertEquals("1.0", deserializedNumber.toString());
    }

    @Test
    public void testDeserializeFloat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", 1.0f);
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(1.0f, deserializedNumber.floatValue(), 0.0f);
        assertEquals("1.0", deserializedNumber.toString());
    }

    @Test
    public void testDeserializeBigInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", "1");
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(BigInteger.ONE, deserializedNumber);
        assertEquals("1", deserializedNumber.toString());
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", "1");
        Number deserializedNumber = mapper.readValue(node.toString(), Number.class);
        assertEquals(new BigDecimal("1"), deserializedNumber);
        assertEquals("1", deserializedNumber.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedNumberType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("number", "Not a number");
        mapper.readValue(node.toString(), Number.class);
    }

    @Test
    public void testCreateContextual() {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("testProperty", Number.class, null);
        JsonFormat.Value format = provider.getTextFactory().createValueFormat("testFormat");
        assertEquals(serializer, serializer.createContextual(provider, property));

        // Test custom format
        JsonFormat.Value customFormat = JsonFormat.Value.builderWithSerializedName("testFormat")
                .build();
        assertEquals(serializer, serializer.createContextual(provider, property, customFormat));

        // Test other types
        JsonFormat.Value bigDecimalFormat = JsonFormat.Value.builderWithSerializedName("testFormat")
                .withShape(JsonFormat.Shape.NUMBER).withNumberType(JsonParser.NumberType.BIG_DECIMAL)
                .build();
        assertNotEquals(serializer, serializer.createContextual(provider, property, bigDecimalFormat));
        assertEquals(ToStringSerializer.instance,
                serializer.createContextual(provider, property, bigDecimalFormat));
    }
}