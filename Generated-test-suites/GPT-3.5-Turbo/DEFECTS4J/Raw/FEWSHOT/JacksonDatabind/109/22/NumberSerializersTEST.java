package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

@SuppressWarnings("serial")
public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws JsonProcessingException, IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mock(SerializerProvider.class), mock(BeanProperty.class));
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, mock(JavaType.class));
        serializer.getSchema(mock(SerializerProvider.class), Short.class);
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException, IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        serializer.serializeWithType(null, mock(JsonGenerator.class), mock(SerializerProvider.class), typeSerializer);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException, IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException, IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException, IOException {
        FloatSerializer serializer = new FloatSerializer();
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException, IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        serializer.serializeWithType(null, mock(JsonGenerator.class), mock(SerializerProvider.class), typeSerializer);
    }

    @Test
    public void testNumberSerializersAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(8, allDeserializers.size());
    }
}