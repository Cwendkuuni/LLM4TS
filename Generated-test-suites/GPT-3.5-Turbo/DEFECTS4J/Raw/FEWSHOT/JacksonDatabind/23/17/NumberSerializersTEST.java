package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Short value = 10;
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Integer value = 10;
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number value = 10;
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Long value = 10L;
        NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Float value = 10.0f;
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Double value = 10.0;
        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.doubleValue());
    }
}