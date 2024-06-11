package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializationOfNumber() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));

        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        for (Number number : new Number[] {BigDecimal.ONE, BigInteger.ONE, 1, 1.0F, 1.0D, 1, Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE}) {
            jsonGenerator.writeStartArray();
            numberSerializer.serialize(number, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndArray();
            jsonGenerator.flush();
            jsonParser.nextValue();

            Object deserializedObject = objectCodec.readValue(jsonParser, Number.class);
            assertEquals(number, deserializedObject);
            jsonParser.close();
            jsonGenerator.close();
        }
    }

    @Test
    public void testSerializationOfBigDecimal() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        jsonGenerator.writeStartArray();
        numberSerializer.serialize(new BigDecimal("1.0"), jsonGenerator, serializerProvider);
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        Object deserializedObject = objectCodec.readValue(jsonParser, BigDecimal.class);
        assertEquals(new BigDecimal("1.0"), deserializedObject);
        jsonParser.close();
        jsonGenerator.close();
    }

    @Test
    public void testSerializationOfBigInteger() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        jsonGenerator.writeStartArray();
        numberSerializer.serialize(BigInteger.ONE, jsonGenerator, serializerProvider);
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        Object deserializedObject = objectCodec.readValue(jsonParser, BigInteger.class);
        assertEquals(BigInteger.ONE, deserializedObject);
        jsonParser.close();
        jsonGenerator.close();
    }

    @Test
    public void testContextualSerialization() throws Exception {
        JsonFormat.Shape shape = JsonFormat.Value.shapeForSerializable(Integer.class);

        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectCodec.getSerializationConfig().findFormatMethod(Integer.class, shape);
        ContextualSerializer contextualSerializer = jsonFormatVisitorWrapper.expectNumberFormat(null);

        assertEquals(NumberSerializer.class, contextualSerializer.getClass());
    }

    @Test
    public void testContextualSerializationOfBigDecimal() throws Exception {
        JsonFormat.Shape shape = JsonFormat.Value.shapeForSerializable(BigDecimal.class);

        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectCodec.getSerializationConfig().findFormatMethod(BigDecimal.class, shape);
        ContextualSerializer contextualSerializer = jsonFormatVisitorWrapper.expectNumberFormat(null);

        assertEquals(ToStringSerializer.class, contextualSerializer.getClass());
    }

    @Test
    public void testSerializationOfNull() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator.writeStartArray();
        numberSerializer.serialize(null, jsonGenerator, serializerProvider);
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        assertEquals(JsonNull.INSTANCE, jsonParser.getValueAsToken());
        jsonParser.close();
        jsonGenerator.close();
    }

    @Test
    public void testSerializationOfInvalidNumber() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator.writeStartArray();
        try {
            numberSerializer.serialize(new Object(), jsonGenerator, serializerProvider);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to serialize value of type java.lang.Object to Number, no String serializer found. Use a custom serializer or write a custom serializer to extend com.fasterxml.jackson.databind.ser.std.StdSerializerBase<T> and override methods as needed", e.getMessage());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        // Expected result: null
        assertNull(jsonParser.getValueAsToken());
        jsonParser.close();
        jsonGenerator.close();
    }

    @Test
    public void testSerializationOfInvalidBigDecimal() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        jsonGenerator.writeStartArray();
        try {
            numberSerializer.serialize(new Object(), jsonGenerator, serializerProvider);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to serialize value of type java.lang.Object to Number, no String serializer found. Use a custom serializer or write a custom serializer to extend com.fasterxml.jackson.databind.ser.std.StdSerializerBase<T> and override methods as needed", e.getMessage());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        // Expected result: null
        assertNull(jsonParser.getValueAsToken());
        jsonParser.close();
        jsonGenerator.close();
    }

    @Test
    public void testSerializationOfInvalidBigInteger() throws Exception {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonParser jsonParser = new JsonFactory().createParser(new StringReader("[]"));
        ObjectCodec objectCodec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        SerializerProvider serializerProvider = objectCodec.getDefaultSerializerProvider();

        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        jsonGenerator.writeStartArray();
        try {
            numberSerializer.serialize(new Object(), jsonGenerator, serializerProvider);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to serialize value of type java.lang.Object to Number, no String serializer found. Use a custom serializer or write a custom serializer to extend com.fasterxml.jackson.databind.ser.std.StdSerializerBase<T> and override methods as needed", e.getMessage());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        jsonParser.nextValue();

        // Expected result: null
        assertNull(jsonParser.getValueAsToken());
        jsonParser.close();
        jsonGenerator.close();
    }
}