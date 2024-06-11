package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private ObjectMapper mapper;

    public NumberSerializersTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> serializer = NumberSerializers.IntegerSerializer.instance;
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Integer.class));

        // Test with JsonNode
        JsonNode node = mapper.readTree(json);
        JsonNode expectedNode = mapper.valueToTree(number);
        assertEquals(expectedNode, node);

        // Test serialization with different formats
        ObjectNode nodeWithFormat = mapper.createObjectNode();
        nodeWithFormat.put("number", number);
        nodeWithFormat.put("format", "INT");
        String jsonWithFormat = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeWithFormat);
        JsonParser parserWithFormat = mapper.getFactory().createParser(jsonWithFormat);
        assertEquals(number, parserWithFormat.readValueAs(Integer.class));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        Short number = Short.parseShort("123");;
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Short.class));

        // Test with JsonNode
        JsonNode node = mapper.readTree(json);
        JsonNode expectedNode = mapper.valueToTree(number);
        assertEquals(expectedNode, node);

        // Test serialization with different formats
        ObjectNode nodeWithFormat = mapper.createObjectNode();
        nodeWithFormat.put("number", number);
        nodeWithFormat.put("format", "SHORT");
        String jsonWithFormat = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeWithFormat);
        JsonParser parserWithFormat = mapper.getFactory().createParser(jsonWithFormat);
        assertEquals(number, parserWithFormat.readValueAs(Short.class));
    }

    // Add tests for LongSerializer, FloatSerializer, DoubleSerializer

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> serializer = NumberSerializers.IntLikeSerializer.instance;
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Integer.class));

        // Test with JsonNode
        JsonNode node = mapper.readTree(json);
        JsonNode expectedNode = mapper.valueToTree(number);
        assertEquals(expectedNode, node);

        // Test serialization with different formats
        ObjectNode nodeWithFormat = mapper.createObjectNode();
        nodeWithFormat.put("number", number);
        nodeWithFormat.put("format", "INT_LIKE");
        String jsonWithFormat = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeWithFormat);
        JsonParser parserWithFormat = mapper.getFactory().createParser(jsonWithFormat);
        assertEquals(number, parserWithFormat.readValueAs(Integer.class));

        // Test with non-integer value
        Long longValue = 456l;
        JsonNode nodeWithLongValue = mapper.createObjectNode();
        nodeWithLongValue.put("number", longValue);
        String jsonWithLongValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeWithLongValue);
        JsonParser parserWithLongValue = mapper.getFactory().createParser(jsonWithLongValue);
        assertEquals(longValue.longValue(), parserWithLongValue.readValueAs(Long.class));
    }
}