package com.example.junit.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito. Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NumberSerializersTest {

    @Test
    public void testAddAll() throws Exception {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);

        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        assertEquals(IntegerSerializer.class, intSerializer.getClass());

        JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());
        assertEquals(LongSerializer.class, longSerializer.getClass());

        JsonSerializer<?> shortSerializer = allDeserializers.get(Short.class.getName());
        assertEquals(ShortSerializer.class, shortSerializer.getClass());

        JsonSerializer<?> byteSerializer = allDeserializers.get(Byte.class.getName());
        assertEquals(IntLikeSerializer.class, byteSerializer.getClass());

        JsonSerializer<?> floatSerializer = allDeserializers.get(Float.class.getName());
        assertEquals(FloatSerializer.class, floatSerializer.getClass());

        JsonSerializer<?> doubleSerializer = allDeserializers.get(Double.class.getName());
        assertEquals(DoubleSerializer.class, doubleSerializer.getClass());
    }

    @Test
    public void testIntegerSerializer_serialize() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Object value = Integer.valueOf(123);

        integerSerializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(123);
    }

    @Test
    public void testIntegerSerializer_serializeWithType() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        Object value = Integer.valueOf(123);

        integerSerializer.serializeWithType(value, gen, provider, typeSerializer);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(123);
    }

    @Test(expected = IOException.class)
    public void testIntegerSerializer_serialize_withNullValue() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Object value = null;

        integerSerializer.serialize(value, gen, provider);
    }

    // Add test cases for other serializers and their methods similarly.

}