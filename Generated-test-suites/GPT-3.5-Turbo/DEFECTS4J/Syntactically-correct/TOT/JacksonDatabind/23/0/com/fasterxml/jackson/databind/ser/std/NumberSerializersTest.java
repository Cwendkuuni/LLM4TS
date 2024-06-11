
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.*;

import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {

    private JsonSerializer<?> integerSerializer;
    private JsonSerializer<?> longSerializer;
    private JsonSerializer<?> intLikeSerializer;
    private JsonSerializer<?> shortSerializer;
    private JsonSerializer<?> floatSerializer;
    private JsonSerializer<?> doubleSerializer;

    @Before
    public void setUp() {
        integerSerializer = new NumberSerializers.IntegerSerializer();
        longSerializer = new NumberSerializers.LongSerializer();
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        shortSerializer = new NumberSerializers.ShortSerializer();
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer();
    }

    @Test
    public void testIntegerSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Integer integer = 10;

        try {
            integerSerializer.serialize(integer, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }

    @Test
    public void testLongSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Long l = 1000L;

        try {
            longSerializer.serialize(l, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Byte b = 1;

        try {
            intLikeSerializer.serialize(b, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }

    @Test
    public void testShortSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Short s = 100;

        try {
            shortSerializer.serialize(s, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }

    @Test
    public void testFloatSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Float f = 1.5f;

        try {
            floatSerializer.serialize(f, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializer() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Double d = 3.0;

        try {
            doubleSerializer.serialize(d, generator, provider);
        } catch (Exception e) {
            assertEquals("Unexpected exception thrown", "", e.getMessage());
        }
    }
}
