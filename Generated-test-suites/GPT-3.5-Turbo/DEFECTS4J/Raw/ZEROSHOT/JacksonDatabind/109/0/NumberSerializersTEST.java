package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        shortSerializer.serialize(12345, jsonGenerator, serializerProvider);

        // Add assertions here

    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        integerSerializer.serialize(12345, jsonGenerator, serializerProvider);

        // Add assertions here

    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        intLikeSerializer.serialize(12345, jsonGenerator, serializerProvider);

        // Add assertions here

    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        longSerializer.serialize(123456789L, jsonGenerator, serializerProvider);

        // Add assertions here

    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = FloatSerializer.instance;

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        floatSerializer.serialize(3.14f, jsonGenerator, serializerProvider);

        // Add assertions here

    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        doubleSerializer.serialize(3.14159, jsonGenerator, serializerProvider);

        // Add assertions here

    }
}