package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Comprehensive test cases for {@link NumberSerializers} class.
 */
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(10, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeseralizers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testBase() throws JsonProcessingException {
        final JsonFactory jsonFactory = new JsonFactory();
        final ObjectWriter writer = jsonFactory.writer();

        for (Base<?> base : NumberSerializers.getAllSerializers()) {
            String testValue = base.handledType().getName();
            Object testObject = base.handledType().cast(0);

            JsonNode resultNode = writer.writeValueAsTree(testObject);
            String resultString = resultNode.asText();

            assertEquals("0", resultString);
            assertEquals(testValue, base.schemaType());

            JsonNode schemaNode = base.getSchema(jsonFactory.getSerializerProvider(), base.handledType());
            assertEquals(testValue, schemaNode.toString());

            JsonFormatVisitorWrapper formatVisitor = jsonFactory.getSerializerProvider().getDefaultSerializerProvider().getSchemaFactory().getSchema(writer.getConfig(), base.handledType());
            JsonNode formatSchema = formatVisitor.getSchemaNode();
            assertEquals(testValue, formatSchema.toString());
        }
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        final Object shortObject = (short) 123;

        JsonNode resultNode = NumberSerializers.ShortSerializer.instance.serializeWithType(shortObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123", resultString);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        final int intValue = 123;
        final Object intObject = intValue;

        JsonNode resultNode = NumberSerializers.IntegerSerializer.instance.serializeWithType(intObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123", resultString);

        resultNode = NumberSerializers.IntegerSerializer.instance.serialize(intObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        resultString = resultNode.asText();

        assertEquals("123", resultString);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        final Number intLikeObject = 123;

        JsonNode resultNode = NumberSerializers.IntLikeSerializer.instance.serializeWithType(intLikeObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123", resultString);

        resultNode = NumberSerializers.IntLikeSerializer.instance.serialize(intLikeObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        resultString = resultNode.asText();

        assertEquals("123", resultString);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        final long longValue = 123456789L;
        final Object longObject = longValue;

        JsonNode resultNode = NumberSerializers.LongSerializer.instance.serializeWithType(longObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123456789", resultString);

        resultNode = NumberSerializers.LongSerializer.instance.serialize(longObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        resultString = resultNode.asText();

        assertEquals("123456789", resultString);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        final float floatValue = 123.45f;
        final Object floatObject = floatValue;

        JsonNode resultNode = NumberSerializers.FloatSerializer.instance.serializeWithType(floatObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123.45", resultString);

        resultNode = NumberSerializers.FloatSerializer.instance.serialize(floatObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        resultString = resultNode.asText();

        assertEquals("123.45", resultString);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        final double doubleValue = 123.456789;
        final Object doubleObject = doubleValue;

        JsonNode resultNode = NumberSerializers.DoubleSerializer.instance.serializeWithType(doubleObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        String resultString = resultNode.asText();

        assertEquals("123.456789", resultString);

        resultNode = NumberSerializers.DoubleSerializer.instance.serialize(doubleObject, jsonFactory.getSerializerProvider().getEmptyRootValue(), null);
        resultString = resultNode.asText();

        assertEquals("123.456789", resultString);
    }

    private static JsonFactory jsonFactory = new JsonFactory();
}