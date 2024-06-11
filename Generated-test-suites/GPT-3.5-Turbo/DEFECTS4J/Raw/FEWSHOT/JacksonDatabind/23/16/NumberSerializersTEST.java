package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        assertEquals(Short.class, serializer.handledType());

        Short value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();

        assertEquals(Integer.class, serializer.handledType());

        Integer value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        assertEquals(Number.class, serializer.handledType());

        Number value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();

        assertEquals(Long.class, serializer.handledType());

        Long value = 10L;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        assertEquals(Float.class, serializer.handledType());

        Float value = 10.5f;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();

        assertEquals(Double.class, serializer.handledType());

        Double value = 10.5;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.doubleValue());
    }
}