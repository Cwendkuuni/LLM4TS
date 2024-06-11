
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        // Assertion
        assert (allDeserializers.containsKey(Integer.class.getName()));
        assert (allDeserializers.containsKey(Integer.TYPE.getName()));
        assert (allDeserializers.containsKey(Long.class.getName()));
        assert (allDeserializers.containsKey(Long.TYPE.getName()));
        assert (allDeserializers.containsKey(Byte.class.getName()));
        assert (allDeserializers.containsKey(Byte.TYPE.getName()));
        assert (allDeserializers.containsKey(Short.class.getName()));
        assert (allDeserializers.containsKey(Short.TYPE.getName()));
        assert (allDeserializers.containsKey(Double.class.getName()));
        assert (allDeserializers.containsKey(Double.TYPE.getName()));
        assert (allDeserializers.containsKey(Float.class.getName()));
        assert (allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Short instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Integer instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Number instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Long instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Float instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = null; // replace with appropriate JsonGenerator instance
        SerializerProvider provider = null; // replace with appropriate SerializerProvider instance
        // Replace value with appropriate Double instance
        serializer.serialize(value, gen, provider);
        // Assertion
        // Add your assertion here
    }
}
