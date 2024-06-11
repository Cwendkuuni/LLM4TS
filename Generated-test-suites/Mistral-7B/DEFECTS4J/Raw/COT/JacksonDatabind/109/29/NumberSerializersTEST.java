package com.example.junit; // replace with your testing package if necessary

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        // Add other assertions for other classes
    }

    @Test
    public void testIntegerSerializer_intValue() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree(5);
        Object deserialized = NumberSerializers.findValueDeserializer(JsonSerializers.Serializers.FIND_BY_CLASS, jsonNode, Integer.class);
        assertEquals(5, deserialized);
    }

    @Test
    public void testIntegerSerializer_longValue() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree(5L);
        try {
            Object deserialized = integerSerializer.deserialize(null, JsonFactory.instance.createParser(jsonNode.traverse()));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIntegerSerializer_floatValue() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree((float) 5.5);
        try {
            Object deserialized = integerSerializer.deserialize(null, JsonFactory.instance.createParser(jsonNode.traverse()));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIntegerSerializer_doubleValue() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree(5.0);
        try {
            Object deserialized = integerSerializer.deserialize(null, JsonFactory.instance.createParser(jsonNode.traverse()));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testSerializeWithType() throws JsonProcessingException {
        JsonSerializer<Object> intSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree(5);
        Object deserialized = NumberSerializers.findValueDeserializer(JsonSerializers.Serializers.FIND_BY_CLASS, jsonNode, Integer.class);
        JsonSerializer<Object> intSerializerWithType = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        ObjectWriter objectWriterWithType = new ObjectMapper().writerWithTypeHandler(new TypeSerializer());
        JsonNode jsonNodeWithType = objectWriterWithType.writeValueAsTree(deserialized);
        Object deserializedWithType = intSerializerWithType.deserialize(null, JsonFactory.instance.createParser(jsonNodeWithType.traverse()));
        assertEquals(deserialized, deserializedWithType);
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testJsonFormatVisitorWrapper_acceptIntFormat() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode jsonNode = objectWriter.writeValueAsTree(5, new JsonFormat.Feature() {{
            setIntValuesUseString(true);
        }});
        JsonNode expectedJsonNode = objectWriter.writeValueAsTree(5.0);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapper(new ObjectMapper().getFactory());
        JsonSerializer<Object> intSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        intSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, JsonTypeFactory
                .constructType(Integer.TYPE));
        JsonNode actualJsonNode = jsonFormatVisitorWrapper.getSerializerProvider().getValueSerializer(JsonTypeFactory
                .constructType(Integer.TYPE)).serializeWithType(null, jsonFormatVisitorWrapper, null, null);
        assertEquals(expectedJsonNode, actualJsonNode);
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testCreateContextual() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> intSerializer = NumberSerializers.findValueSerializer(JsonSerializers.Serializers.FIND_BY_CLASS, Integer.class, null);
        JsonSerializer<Object> intSerializerWithFormat = new IntFormatSerializer();
        ObjectWriter objectWriter = new ObjectMapper()
                .writer()
                .setSerializerProvider(new BasicSerializerProvider() {
                    @Override
                    public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
                        if (type.equals(Integer.class)) {
                            return intSerializerWithFormat;
                        }
                        return intSerializer;
                    }
                });
        ObjectNode objectNode = objectWriter.writeValueAsObject(new Object());
        JsonNode jsonNode = objectNode.get("test");
        JsonParser jsonParser = objectMapper.parser();
        JsonParser.NullValueReader nullValueReader = jsonParser.getParserConfig().getNullValueReader();
        JsonParser.CurrentName currentName = jsonParser.getCurrentName();
        JsonParser parser = new JsonParser(jsonNode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JsonParser.Token token = parser.nextToken();
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextToken(JsonToken.VALUE_NUMBER_INT);
        int deserialized = NumberSerializers.findValueDeserializer(JsonSerializers.Serializers.FIND_BY_CLASS, jsonNode, Integer.class).deserialize(null, parser);
        assertEquals(5, deserialized);
    }

    // Add test cases for other methods and edge cases

}