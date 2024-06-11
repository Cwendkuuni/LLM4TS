package com.example.junit.test.numberserializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.BaseSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    public NumberSerializersTest() throws JsonProcessingException {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = NumberSerializers.findIntSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(integerSerializer, 1);
        Assert.assertEquals(1, serializedNode.numberValue().intValue());

        testEdgeCasesAndExceptions(integerSerializer, new Integer[]{null, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.valueOf("123")});
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = NumberSerializers.findShortSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(shortSerializer, (short) 1);
        Assert.assertEquals(1, serializedNode.numberValue().intValue());

        testEdgeCasesAndExceptions(shortSerializer, new Short[]{null, Short.MIN_VALUE, Short.MAX_VALUE, Short.valueOf("123")});
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.findIntLikeSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(intLikeSerializer, 1);
        Assert.assertEquals(1, serializedNode.numberValue().intValue());

        testEdgeCasesAndExceptions(intLikeSerializer, new Object[]{null, Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE, Float.NaN, Double.NaN, new BigDecimal("123"), new BigInteger("123")});
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = NumberSerializers.findLongSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(longSerializer, 1L);
        Assert.assertEquals(1, serializedNode.numberValue().longValue());

        testEdgeCasesAndExceptions(longSerializer, new Long[]{null, Long.MIN_VALUE, Long.MAX_VALUE, Long.valueOf("123")});
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = NumberSerializers.findFloatSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(floatSerializer, 1.0f);
        Assert.assertEquals(1.0f, serializedNode.numberValue().floatValue(), 0.01f);

        testEdgeCasesAndExceptions(floatSerializer, new Float[]{null, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY});
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = NumberSerializers.findDoubleSerializer(this.objectMapper);

        JsonNode serializedNode = testSerializationAndDeserialization(doubleSerializer, 1.0);
        Assert.assertEquals(1.0, serializedNode.numberValue().doubleValue(), 0.01);

        testEdgeCasesAndExceptions(doubleSerializer, new Double[]{null, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY});
    }

    private JsonNode testSerializationAndDeserialization(BaseSerializer<?> serializer, Object value) throws IOException {
        String json = toJsonString(serializer.getClass().getSimpleName() + ": " + value, this.objectMapper);
        JsonNode serializedNode = mapperToJsonNode(json);
        Object deserializedValue = mapperFromJsonNodeToObject(serializedNode, serializer.handledType());

        return serializedNode;
    }

    private void testEdgeCasesAndExceptions(BaseSerializer<?> serializer, Object[] edgeCases) throws IOException {
        for (Object edgeCase : edgeCases) {
            try {
                String json = toJsonString(serializer.getClass().getSimpleName() + ": " + edgeCase, this.objectMapper);
                JsonNode serializedNode = mapperToJsonNode(json);
                Object deserializedValue = mapperFromJsonNodeToObject(serializedNode, serializer.handledType());
                Assert.assertEquals(edgeCase, deserializedValue);
            } catch (JsonProcessingException e) {
                if (serializer.getClass().isAnnotationPresent(JsonFormat.class)) {
                    JsonFormat jsonFormat = serializer.getClass().getAnnotation(JsonFormat.class);
                    Assert.assertTrue(e.getMessage().contains(jsonFormat.shape().toString()));
                } else {
                    Assert.fail("Unexpected exception: " + e.getMessage());
                }
            }
        }
    }

    private String toJsonString(String description, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(description);
    }

    private JsonNode mapperToJsonNode(String jsonString) throws IOException {
        return this.objectMapper.readTree(jsonString);
    }

    private <T> T mapperFromJsonNodeToObject(JsonNode jsonNode, Type type) throws IOException {
        return this.objectMapper.readValue(jsonNode, type);
    }
}