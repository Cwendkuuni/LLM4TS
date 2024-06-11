
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    @Test
    public void testSerialize_ShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        short value = 10;

        shortSerializer.serialize(value, gen, provider);

        verify(gen, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_IntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        int value = 100;

        integerSerializer.serialize(value, gen, provider);

        verify(gen, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_FloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        float value = 10.5f;

        floatSerializer.serialize(value, gen, provider);

        verify(gen, times(1)).writeNumber(value);
    }
}

