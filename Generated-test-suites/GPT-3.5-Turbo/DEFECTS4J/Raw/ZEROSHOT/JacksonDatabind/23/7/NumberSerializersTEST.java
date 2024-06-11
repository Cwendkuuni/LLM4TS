package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;
    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
        allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testShortSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assert(serializer instanceof NumberSerializers.ShortSerializer);

        NumberSerializers.ShortSerializer shortSerializer = (NumberSerializers.ShortSerializer) serializer;
        Short value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            shortSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }

    @Test
    public void testIntegerSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assert(serializer instanceof NumberSerializers.IntegerSerializer);

        NumberSerializers.IntegerSerializer integerSerializer = (NumberSerializers.IntegerSerializer) serializer;
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            integerSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }

    @Test
    public void testIntLikeSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Number.class.getName());
        assert(serializer instanceof NumberSerializers.IntLikeSerializer);

        NumberSerializers.IntLikeSerializer intLikeSerializer = (NumberSerializers.IntLikeSerializer) serializer;
        Number value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            intLikeSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }

    @Test
    public void testLongSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        assert(serializer instanceof NumberSerializers.LongSerializer);

        NumberSerializers.LongSerializer longSerializer = (NumberSerializers.LongSerializer) serializer;
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            longSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }

    @Test
    public void testFloatSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        assert(serializer instanceof NumberSerializers.FloatSerializer);

        NumberSerializers.FloatSerializer floatSerializer = (NumberSerializers.FloatSerializer) serializer;
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            floatSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }

    @Test
    public void testDoubleSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assert(serializer instanceof NumberSerializers.DoubleSerializer);

        NumberSerializers.DoubleSerializer doubleSerializer = (NumberSerializers.DoubleSerializer) serializer;
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            doubleSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert serialization produce the correct output
    }
}