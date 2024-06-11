
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviderImpl;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() {
        assertEquals(10, allDeserializers.size());
    }

    @Test
    public void testShortSerializer_serialize() throws IOException {
        Base.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals((short) 100, node.shortValue());
    }

    @Test
    public void testIntegerSerializer_serialize() throws IOException {
        Base.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(1, node.intValue());
    }

    @Test
    public void testIntegerSerializer_serializeWithType() throws IOException {
        Base.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
        JsonNode node = serializeWithType(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(1, node.intValue());
    }

    @Test
    public void testIntLikeSerializer_serialize() throws IOException {
        Base.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(1, node.intValue());
    }

    @Test
    public void testLongSerializer_serialize() throws IOException {
        Base.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(1000L, node.longValue());
    }

    @Test
    public void testFloatSerializer_serialize() throws IOException {
        Base.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(1.23f, node.floatValue(), 0.0);
    }

    @Test
    public void testDoubleSerializer_serialize() throws IOException {
        Base.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        JsonNode node = serialize(serializer);

        assertNotNull(node);
        assertTrue(node.isNumber());
        assertEquals(3.14, node.doubleValue(), 0.0);
    }

    private JsonNode serialize(Base<?> serializer) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonFactory.createGenerator(System.out));
        SerializerProvider serializerProvider = new SerializerProviderImpl();
        serializer.serialize(1, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputTarget().toString();
        return objectMapper.readTree(json);
    }

    private JsonNode serializeWithType(Base<?> serializer) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonFactory.createGenerator(System.out));
        SerializerProvider serializerProvider = new SerializerProviderImpl();
        serializer.serializeWithType(1, jsonGenerator, serializerProvider, null);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputTarget().toString();
        return objectMapper.readTree(json);
    }
}

