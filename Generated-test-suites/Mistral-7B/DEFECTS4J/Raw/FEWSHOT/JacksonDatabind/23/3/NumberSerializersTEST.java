package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

/**
 * Test cases for {@link NumberSerializers} class
 */
@SuppressWarnings("unchecked")
public class NumberSerializersTest {

    // Helper method to create a mock JsonGenerator for testing
    private JsonGenerator createMockJsonGenerator() {
        JsonGenerator mockGen = mock(JsonGenerator.class);
        when(mockGen.writeNumber(Integer.MIN_VALUE)).thenCallRealMethod();
        when(mockGen.writeNumber(0)).thenCallRealMethod();
        when(mockGen.writeNumber(123)).thenCallRealMethod();
        when(mockGen.writeNumber(Float.MIN_VALUE)).thenCallRealMethod();
        when(mockGen.writeNumber(Float.NaN)).thenCallRealMethod();
        when(mockGen.writeNumber(Double.MIN_VALUE)).thenCallRealMethod();
        when(mockGen.writeNumber(Double.POSITIVE_INFINITY)).thenCallRealMethod();
        return mockGen;
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer intSerializer = new IntegerSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        intSerializer.serialize(1, jsonGenerator, new SerializerProvider());
        intSerializer.serializeWithType(1, jsonGenerator, new SerializerProvider(), null);
        intSerializer.serialize(-1, jsonGenerator, new SerializerProvider());
        intSerializer.serializeWithType(-1, jsonGenerator, new SerializerProvider(), null);
        intSerializer.serialize(Integer.MIN_VALUE, jsonGenerator, new SerializerProvider());
        intSerializer.serializeWithType(Integer.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testIntegerSerializerContextual() throws IOException {
        JsonSerializer<?> intSerializer = new IntegerSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        ObjectNode rootNode = objectWriter.writeValueAsObject(new Integer(1));
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDefaultContextualSerializer(Integer.class, intSerializer);
        intSerializer.serialize(1, jsonGenerator, serializerProvider);
        intSerializer.serializeWithType(1, jsonGenerator, serializerProvider, null);
        intSerializer.serialize(-1, jsonGenerator, serializerProvider);
        intSerializer.serializeWithType(-1, jsonGenerator, serializerProvider, null);
        intSerializer.serialize(Integer.MIN_VALUE, jsonGenerator, serializerProvider);
        intSerializer.serializeWithType(Integer.MIN_VALUE, jsonGenerator, serializerProvider, null);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        intLikeSerializer.serialize(1, jsonGenerator, new SerializerProvider());
        intLikeSerializer.serializeWithType(1, jsonGenerator, new SerializerProvider(), null);
        intLikeSerializer.serialize(-1, jsonGenerator, new SerializerProvider());
        intLikeSerializer.serializeWithType(-1, jsonGenerator, new SerializerProvider(), null);
        intLikeSerializer.serialize(Byte.MIN_VALUE, jsonGenerator, new SerializerProvider());
        intLikeSerializer.serializeWithType(Byte.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        longSerializer.serialize(1L, jsonGenerator, new SerializerProvider());
        longSerializer.serializeWithType(1L, jsonGenerator, new SerializerProvider(), null);
        longSerializer.serialize(-1L, jsonGenerator, new SerializerProvider());
        longSerializer.serializeWithType(-1L, jsonGenerator, new SerializerProvider(), null);
        longSerializer.serialize(Long.MIN_VALUE, jsonGenerator, new SerializerProvider());
        longSerializer.serializeWithType(Long.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        shortSerializer.serialize((short) 1, jsonGenerator, new SerializerProvider());
        shortSerializer.serializeWithType((short) 1, jsonGenerator, new SerializerProvider(), null);
        shortSerializer.serialize((short) -1, jsonGenerator, new SerializerProvider());
        shortSerializer.serializeWithType((short) -1, jsonGenerator, new SerializerProvider(), null);
        shortSerializer.serialize((short) Short.MIN_VALUE, jsonGenerator, new SerializerProvider());
        shortSerializer.serializeWithType((short) Short.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        floatSerializer.serialize(0f, jsonGenerator, new SerializerProvider());
        floatSerializer.serializeWithType(0f, jsonGenerator, new SerializerProvider(), null);
        floatSerializer.serialize(Float.MIN_VALUE, jsonGenerator, new SerializerProvider());
        floatSerializer.serializeWithType(Float.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
        floatSerializer.serialize(Float.NaN, jsonGenerator, new SerializerProvider());
        floatSerializer.serializeWithType(Float.NaN, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        doubleSerializer.serialize(0d, jsonGenerator, new SerializerProvider());
        doubleSerializer.serializeWithType(0d, jsonGenerator, new SerializerProvider(), null);
        doubleSerializer.serialize(Double.MIN_VALUE, jsonGenerator, new SerializerProvider());
        doubleSerializer.serializeWithType(Double.MIN_VALUE, jsonGenerator, new SerializerProvider(), null);
        doubleSerializer.serialize(Double.POSITIVE_INFINITY, jsonGenerator, new SerializerProvider());
        doubleSerializer.serializeWithType(Double.POSITIVE_INFINITY, jsonGenerator, new SerializerProvider(), null);
    }

    @Test
    public void testContextualSerializationWithJsonFormat() throws IOException {
        JsonSerializer<?> integerSerializer = new IntegerSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        ObjectNode rootNode = objectWriter.writeValueAsObject(new JsonFormat.Value());
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.INT);
        rootNode.put("format", jsonFormat);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDefaultContextualSerializer(JsonFormat.class, integerSerializer);
        Integer integer = 1;
        serializerProvider.getAnnotationIntrospector().findAnnotation(integer.getClass(), JsonFormat.class);
        integerSerializer.serialize(integer, jsonGenerator, serializerProvider);
        integerSerializer.serializeWithType(integer, jsonGenerator, serializerProvider, null);
        assertNull(jsonGenerator.getCurrentValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testContextualSerializationWithJsonFormatInvalidShape() throws IOException {
        JsonSerializer<?> integerSerializer = new IntegerSerializer();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        ObjectNode rootNode = objectWriter.writeValueAsObject(new JsonFormat.Value());
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        rootNode.put("format", jsonFormat);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDefaultContextualSerializer(JsonFormat.class, integerSerializer);
        Integer integer = 1;
        serializerProvider.getAnnotationIntrospector().findAnnotation(integer.getClass(), JsonFormat.class);
        integerSerializer.serialize(integer, jsonGenerator, serializerProvider);
        integerSerializer.serializeWithType(integer, jsonGenerator, serializerProvider, null);
    }
}