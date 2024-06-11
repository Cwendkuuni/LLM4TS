
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();

        shortSerializer = new NumberSerializers.ShortSerializer();
        integerSerializer = new NumberSerializers.IntegerSerializer();
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        longSerializer = new NumberSerializers.LongSerializer();
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer();
    }

    @Test
    public void testAddAll() {
        NumberSerializers.addAll(allDeserializers);

        assertEquals(shortSerializer, allDeserializers.get(Short.class.getName()));
        assertEquals(shortSerializer, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(integerSerializer, allDeserializers.get(Integer.class.getName()));
        assertEquals(integerSerializer, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(intLikeSerializer, allDeserializers.get(Byte.class.getName()));
        assertEquals(intLikeSerializer, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(shortSerializer, allDeserializers.get(Short.class.getName()));
        assertEquals(shortSerializer, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(longSerializer, allDeserializers.get(Long.class.getName()));
        assertEquals(longSerializer, allDeserializers.get(Long.TYPE.getName()));
        assertEquals(floatSerializer, allDeserializers.get(Float.class.getName()));
        assertEquals(floatSerializer, allDeserializers.get(Float.TYPE.getName()));
        assertEquals(doubleSerializer, allDeserializers.get(Double.class.getName()));
        assertEquals(doubleSerializer, allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        Short value = 123;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        shortSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        Integer value = 123;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        integerSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        Byte value = 123;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        intLikeSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }

    @Test
    public void testLongSerializer() throws IOException {
        Long value = 123L;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        longSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        Float value = 123.45f;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        floatSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123.45";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        Double value = 123.45;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        doubleSerializer.serialize(value, generator, provider);
        final String expectedJsonResult = "123.45";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedNode = objectMapper.readTree(expectedJsonResult);

        assertEquals(expectedNode, generator.getOutputContext());
    }
}
