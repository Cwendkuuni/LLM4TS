package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        // Assert the deserializers are added correctly
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testBaseGetSchema() {
        // Create a dummy SerializerProvider and Type
        SerializerProvider provider = null;
        Type typeHint = null;

        // Create a dummy Base object and call getSchema
        NumberSerializers.Base base = numberSerializers.new Base(Object.class, JsonParser.NumberType.INT, "number");
        JsonNode schema = base.getSchema(provider, typeHint);

        // Assert schema is not null
        assertNotNull(schema);
    }

    @Test
    public void testBaseContextual() {
        // Create a dummy SerializerProvider, BeanProperty and JsonFormat.Value
        SerializerProvider prov = null;
        BeanProperty property = null;
        JsonFormat.Value format = null;

        // Create a dummy Base object and call createContextual
        NumberSerializers.Base base = numberSerializers.new Base(Object.class, JsonParser.NumberType.INT, "number");
        try {
            JsonSerializer<?> contextualSerializer = base.createContextual(prov, property);

            // Assert contextualSerializer is not null
            assertNotNull(contextualSerializer);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShortSerializer() {
        // Create a dummy Short object and JsonGenerator
        Short value = 10;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new ShortSerializer
        NumberSerializers.ShortSerializer serializer = numberSerializers.new ShortSerializer();

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializer() {
        // Create a dummy Integer object and JsonGenerator
        Integer value = 10;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new IntegerSerializer
        NumberSerializers.IntegerSerializer serializer = numberSerializers.new IntegerSerializer(Integer.class);

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializer() {
        // Create a dummy Number object and JsonGenerator
        Number value = 10;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new IntLikeSerializer
        NumberSerializers.IntLikeSerializer serializer = numberSerializers.new IntLikeSerializer();

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLongSerializer() {
        // Create a dummy Long object and JsonGenerator
        Long value = 10L;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new LongSerializer
        NumberSerializers.LongSerializer serializer = numberSerializers.new LongSerializer(Long.class);

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFloatSerializer() {
        // Create a dummy Float object and JsonGenerator
        Float value = 10.0f;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new FloatSerializer
        NumberSerializers.FloatSerializer serializer = numberSerializers.new FloatSerializer();

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializer() {
        // Create a dummy Double object and JsonGenerator
        Double value = 10.0;
        JsonGenerator gen = null;

        // Create a dummy SerializerProvider
        SerializerProvider provider = null;

        // Create a new DoubleSerializer
        NumberSerializers.DoubleSerializer serializer = numberSerializers.new DoubleSerializer(Double.class);

        try {
            // Call serialize method
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}