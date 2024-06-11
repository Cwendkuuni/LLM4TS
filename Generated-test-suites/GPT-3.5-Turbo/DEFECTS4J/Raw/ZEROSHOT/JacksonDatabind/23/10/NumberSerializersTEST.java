package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setup() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        assertEquals(10, allDeserializers.size());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
    }

    @Test
    public void testShortSerialize() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();

        Short value = 123;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        shortSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        Object value = 123;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        integerSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(((Integer) value).intValue());
    }

    @Test
    public void testIntLikeSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        Number value = 123;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        intLikeSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerialize() throws IOException {
        LongSerializer longSerializer = new LongSerializer();

        Object value = 123L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        longSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(((Long) value).longValue());
    }

    @Test
    public void testFloatSerialize() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();

        Object value = 123.45f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        floatSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(((Float) value).floatValue());
    }

    @Test
    public void testDoubleSerialize() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        Object value = 123.45;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        doubleSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(((Double) value).doubleValue());
    }

}