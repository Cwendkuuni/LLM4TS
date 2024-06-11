
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testShortSerializer_serialize() throws IOException {
        Short value = 10;
        ShortSerializer shortSerializer = ShortSerializer.instance;

        shortSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(10);
    }

    @Test
    public void testIntegerSerializer_serialize() throws IOException {
        Integer value = 100;
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        integerSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(100);
    }

    @Test
    public void testIntLikeSerializer_serialize() throws IOException {
        Number value = 200;
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;

        intLikeSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(200);
    }

    @Test
    public void testLongSerializer_serialize() throws IOException {
        Long value = 1000L;
        LongSerializer longSerializer = new LongSerializer(Long.class);

        longSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(1000L);
    }

    @Test
    public void testFloatSerializer_serialize() throws IOException {
        Float value = 10.5f;
        FloatSerializer floatSerializer = FloatSerializer.instance;

        floatSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(10.5f);
    }

    @Test
    public void testDoubleSerializer_serialize() throws IOException {
        Double value = 10.6;
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        doubleSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(10.6);
    }
}
