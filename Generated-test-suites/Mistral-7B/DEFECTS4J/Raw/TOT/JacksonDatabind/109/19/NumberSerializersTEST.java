import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonSerializer<Object> serializer = integerSerializer;

        // Test serialization of positive integer values
        Integer intValue = 123;
        String jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(intValue);
        assertEquals("123", jsonString);

        // Test serialization of negative integer values
        Integer negativeIntValue = -123;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeIntValue);
        assertEquals("-123", jsonString);

        // Test serialization of zero
        Integer zeroIntValue = 0;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroIntValue);
        assertEquals("0", jsonString);

        // Test serialization of maximum and minimum integer values
        Integer maxValue = Integer.MAX_VALUE;
        Integer minValue = Integer.MIN_VALUE;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(maxValue);
        assertEquals(Integer.toString(maxValue), jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(minValue);
        assertEquals(Integer.toString(minValue), jsonString);

        // Test serialization with customJsonFormat
        JsonNode customJsonNode = objectMapper.readTree("{\"number\": 42}");
        JsonFormat customJsonFormat = new JsonFormat(customJsonNode);
        ObjectWriter customObjectWriter = objectMapper.writer().withRootValueSerializer(serializer)
                .withSerializerProviders(new SimpleSerializerProvider())
                .withDefaultPrettyPrinter();
        String customJsonString = customObjectWriter.writeValueAsString(123);
        JsonNode customJsonResult = objectMapper.readTree(customJsonString);
        JsonNode expectedCustomJsonResult = customJsonNode.get("number");
        assertEquals(expectedCustomJsonResult.asInt(), customJsonResult.get("number").asInt());

        // Test deserialization
        ObjectMapper objectMapperForDeserialization = new ObjectMapper();
        JsonParser jsonParser = objectMapperForDeserialization.getFactory()
                .createParser("123");
        JsonDeserializer<Integer> deserializer = objectMapper.findValueDeserializer(JsonType.getIntValue());
        Integer deserializedInteger = jsonParser.readValueAs(deserializer);
        assertEquals(123, deserializedInteger.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;
        JsonSerializer<Object> serializer = intLikeSerializer;

        // Test serialization of positive integer values
        Byte byteValue = 12;
        Short shortValue = 32767;
        Integer intValue = 123;
        Long longValue = 123456789L;
        String jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(byteValue);
        assertEquals("12", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(shortValue);
        assertEquals("32767", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(intValue);
        assertEquals("123", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(longValue);
        assertEquals("123456789", jsonString);

        // Test serialization of negative integer values
        Byte negativeByteValue = -12;
        Short negativeShortValue = -32768;
        Integer negativeIntValue = -123;
        Long negativeLongValue = -123456789L;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeByteValue);
        assertEquals("-12", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeShortValue);
        assertEquals("-32768", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeIntValue);
        assertEquals("-123", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeLongValue);
        assertEquals("-123456789", jsonString);

        // Test serialization of zero
        Byte zeroByteValue = 0;
        Short zeroShortValue = 0;
        Integer zeroIntValue = 0;
        Long zeroLongValue = 0L;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroByteValue);
        assertEquals("0", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroShortValue);
        assertEquals("0", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroIntValue);
        assertEquals("0", jsonString);
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroLongValue);
        assertEquals("0", jsonString);

        // Test serialization with customJsonFormat
        JsonNode customJsonNode = objectMapper.readTree("{" +
                "\"number\": { \"numberType\": \"int\", \"format\": \"custom\" } }");
        JsonFormat customJsonFormat = new JsonFormat(customJsonNode);
        ObjectWriter customObjectWriter = objectMapper.writer().withRootValueSerializer(serializer)
                .withSerializerProviders(new SimpleSerializerProvider())
                .withDefaultPrettyPrinter();
        String customJsonString = customObjectWriter.writeValueAsString((byte) 123);
        JsonNode customJsonResult = objectMapper.readTree(customJsonString);
        JsonNode expectedCustomJsonResult = customJsonNode.path("number");
        assertEquals(expectedCustomJsonResult.get("numberType").asText(), "int");
        assertEquals(expectedCustomJsonResult.get("format").asText(), "custom");
        assertEquals(expectedCustomJsonResult.get("number").asInt(), 123);

        // Test deserialization
        ObjectMapper objectMapperForDeserialization = new ObjectMapper();
        JsonParser jsonParser = objectMapperForDeserialization.getFactory()
                .createParser("12");
        JsonDeserializer<Number> deserializer = objectMapper.findValueDeserializer(JsonType.getNumberValue());
        Number deserializedNumber = jsonParser.readValueAs(deserializer);
        assertEquals(12, deserializedNumber.byteValue());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonSerializer<Object> serializer = longSerializer;

        // Test serialization of positive long values
        Long longValue = 1234567890L;
        String jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(longValue);
        assertEquals("1234567890", jsonString);

        // Test serialization of negative long values
        Long negativeLongValue = -1234567890L;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeLongValue);
        assertEquals("-1234567890", jsonString);

        // Test serialization of zero
        Long zeroLongValue = 0L;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroLongValue);
        assertEquals("0", jsonString);

        // Test serialization with customJsonFormat
        JsonNode customJsonNode = objectMapper.readTree("{" +
                "\"number\": { \"numberType\": \"long\", \"format\": \"custom\" } }");
        JsonFormat customJsonFormat = new JsonFormat(customJsonNode);
        ObjectWriter customObjectWriter = objectMapper.writer().withRootValueSerializer(serializer)
                .withSerializerProviders(new SimpleSerializerProvider())
                .withDefaultPrettyPrinter();
        String customJsonString = customObjectWriter.writeValueAsString(1234567890L);
        JsonNode customJsonResult = objectMapper.readTree(customJsonString);
        JsonNode expectedCustomJsonResult = customJsonNode.path("number");
        assertEquals(expectedCustomJsonResult.get("numberType").asText(), "long");
        assertEquals(expectedCustomJsonResult.get("format").asText(), "custom");
        assertEquals(Long.toString(1234567890L), customJsonResult.get("number").asText());

        // Test deserialization
        ObjectMapper objectMapperForDeserialization = new ObjectMapper();
        JsonParser jsonParser = objectMapperForDeserialization.getFactory()
                .createParser("1234567890");
        JsonDeserializer<Long> deserializer = objectMapper.findValueDeserializer(JsonType.getLongValue());
        Long deserializedLong = jsonParser.readValueAs(deserializer);
        assertEquals(1234567890L, deserializedLong);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FloatSerializer floatSerializer = FloatSerializer.instance;
        JsonSerializer<Object> serializer = floatSerializer;

        // Test serialization of positive float values
        Float floatValue = 123.45f;
        String jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(floatValue);
        assertEquals("123.45", jsonString);

        // Test serialization of negative float values
        Float negativeFloatValue = -123.45f;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeFloatValue);
        assertEquals("-123.45", jsonString);

        // Test serialization of zero
        Float zeroFloatValue = 0.0f;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroFloatValue);
        assertEquals("0.0", jsonString);

        // Test serialization with customJsonFormat
        JsonNode customJsonNode = objectMapper.readTree("{" +
                "\"number\": { \"numberType\": \"float\", \"format\": \"custom\" } }");
        JsonFormat customJsonFormat = new JsonFormat(customJsonNode);
        ObjectWriter customObjectWriter = objectMapper.writer().withRootValueSerializer(serializer)
                .withSerializerProviders(new SimpleSerializerProvider())
                .withDefaultPrettyPrinter();
        String customJsonString = customObjectWriter.writeValueAsString(123.45f);
        JsonNode customJsonResult = objectMapper.readTree(customJsonString);
        JsonNode expectedCustomJsonResult = customJsonNode.path("number");
        assertEquals(expectedCustomJsonResult.get("numberType").asText(), "float");
        assertEquals(expectedCustomJsonResult.get("format").asText(), "custom");
        assertEquals(Float.toString(123.45f), customJsonResult.get("number").asText());

        // Test deserialization
        ObjectMapper objectMapperForDeserialization = new ObjectMapper();
        JsonParser jsonParser = objectMapperForDeserialization.getFactory()
                .createParser("123.45");
        JsonDeserializer<Float> deserializer = objectMapper.findValueDeserializer(JsonType.getFloatValue());
        Float deserializedFloat = jsonParser.readValueAs(deserializer);
        assertEquals(123.45f, deserializedFloat);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonSerializer<Object> serializer = doubleSerializer;

        // Test serialization of positive double values
        Double doubleValue = 123.45;
        String jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(doubleValue);
        assertEquals("123.45", jsonString);

        // Test serialization of negative double values
        Double negativeDoubleValue = -123.45;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(negativeDoubleValue);
        assertEquals("-123.45", jsonString);

        // Test serialization of zero
        Double zeroDoubleValue = 0.0;
        jsonString = objectMapper.writerWithRootValueSerializer(serializer)
                .writeValueAsString(zeroDoubleValue);
        assertEquals("0.0", jsonString);

        // Test serialization with customJsonFormat
        JsonNode customJsonNode = objectMapper.readTree("{" +
                "\"number\": { \"numberType\": \"double\", \"format\": \"custom\" } }");
        JsonFormat customJsonFormat = new JsonFormat(customJsonNode);
        ObjectWriter customObjectWriter = objectMapper.writer().withRootValueSerializer(serializer)
                .withSerializerProviders(new SimpleSerializerProvider())
                .withDefaultPrettyPrinter();
        String customJsonString = customObjectWriter.writeValueAsString(123.45);
        JsonNode customJsonResult = objectMapper.readTree(customJsonString);
        JsonNode expectedCustomJsonResult = customJsonNode.path("number");
        assertEquals(expectedCustomJsonResult.get("numberType").asText(), "double");
        assertEquals(expectedCustomJsonResult.get("format").asText(), "custom");
        assertEquals(Double.toString(123.45), customJsonResult.get("number").asText());

        // Test deserialization
        ObjectMapper objectMapperForDeserialization = new ObjectMapper();
        JsonParser jsonParser = objectMapperForDeserialization.getFactory()
                .createParser("123.45");
        JsonDeserializer<Double> deserializer = objectMapper.findValueDeserializer(JsonType.getDoubleValue());
        Double deserializedDouble = jsonParser.readValueAs(deserializer);
        assertEquals(123.45, deserializedDouble);
    }
}