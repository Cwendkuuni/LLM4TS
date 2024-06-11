package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = mock(Map.class);
        numberSerializers.addAll(allDeserializers);
        verify(allDeserializers, times(1)).put(Integer.class.getName(), IntegerSerializer.class);
        verify(allDeserializers, times(1)).put(Integer.TYPE.getName(), IntegerSerializer.class);
        verify(allDeserializers, times(1)).put(Long.class.getName(), LongSerializer.instance);
        verify(allDeserializers, times(1)).put(Long.TYPE.getName(), LongSerializer.instance);
        verify(allDeserializers, times(1)).put(Byte.class.getName(), IntLikeSerializer.instance);
        verify(allDeserializers, times(1)).put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        verify(allDeserializers, times(1)).put(Short.class.getName(), ShortSerializer.instance);
        verify(allDeserializers, times(1)).put(Short.TYPE.getName(), ShortSerializer.instance);
        verify(allDeserializers, times(1)).put(Float.class.getName(), FloatSerializer.instance);
        verify(allDeserializers, times(1)).put(Float.TYPE.getName(), FloatSerializer.instance);
        verify(allDeserializers, times(1)).put(Double.class.getName(), DoubleSerializer.instance);
        verify(allDeserializers, times(1)).put(Double.TYPE.getName(), DoubleSerializer.instance);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        shortSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        integerSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        intLikeSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer();
        Long value = 100L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        longSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 1.2f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        floatSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double value = 3.14;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        doubleSerializer.serialize(value, gen, provider);
        verify(gen, times(1)).writeNumber(value.doubleValue());
    }
}