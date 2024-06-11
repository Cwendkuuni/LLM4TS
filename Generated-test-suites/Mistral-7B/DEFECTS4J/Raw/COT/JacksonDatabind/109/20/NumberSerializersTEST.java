package com.example.numberserializers; // replace with the actual package name

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
class NumberSerializersTest {

    @BeforeAll
    static void setup() {
        // Initialize the test environment here if needed
    }

    @AfterAll
    static void tearDown() {
        // Clean up the test environment here if needed
    }

    // Test case for addAll method
    @Test
    void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size()); // Check number of added serializers
    }

    // Test case for IntegerSerializer
    @Test
    void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(new Integer(1), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(integerSerializer);
        String json = objectWriter.writeValueAsString(new Integer(1));
        assertEquals("1", json);
    }

    // Test case for IntegerSerializer with NaN value
    @Test
    void testIntegerSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> integerSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> integerSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }

    // Test case for LongSerializer
    @Test
    void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Object> longSerializer = NumberSerializers.findValueSerializer(new Long(1), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(longSerializer);
        String json = objectWriter.writeValueAsString(new Long(1));
        assertEquals("1", json);
    }

    // Test case for LongSerializer with NaN value
    @Test
    void testLongSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> longSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> longSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }

    // Test case for ShortSerializer
    @Test
    void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Object> shortSerializer = NumberSerializers.findValueSerializer(new Short((short) 1), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(shortSerializer);
        String json = objectWriter.writeValueAsString(new Short((short) 1));
        assertEquals("1", json);
    }

    // Test case for ShortSerializer with NaN value
    @Test
    void testShortSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> shortSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> shortSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }

    // Test case for FloatSerializer
    @Test
    void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Object> floatSerializer = NumberSerializers.findValueSerializer(new Float(1.0f), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(floatSerializer);
        String json = objectWriter.writeValueAsString(new Float(1.0f));
        assertEquals("1.0", json);
    }

    // Test case for FloatSerializer with NaN value
    @Test
    void testFloatSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> floatSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> floatSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }

    // Test case for DoubleSerializer
    @Test
    void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Object> doubleSerializer = NumberSerializers.findValueSerializer(new Double(1.0), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(doubleSerializer);
        String json = objectWriter.writeValueAsString(new Double(1.0));
        assertEquals("1.0", json);
    }

    // Test case for DoubleSerializer with NaN value
    @Test
    void testDoubleSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> doubleSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> doubleSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }

    // Test case for IntLikeSerializer
    @Test
    void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Object> intLikeSerializer = NumberSerializers.findValueSerializer(new Byte("1"), null);
        ObjectWriter objectWriter = new ObjectMapper().writerWithRootValueSerializer(intLikeSerializer);
        String json = objectWriter.writeValueAsString(new Byte("1"));
        assertEquals("1", json);
    }

    // Test case for IntLikeSerializer with NaN value
    @Test
    void testIntLikeSerializerWithNaN() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("NaN");
        JsonSerializer<Object> intLikeSerializer = NumberSerializers.findValueSerializer(parser.getValueAsToken(), null);
        assertThrows(IllegalArgumentException.class, () -> intLikeSerializer.serialize(parser.getValueAsToken(), new JsonGenerator(), new SerializerProvider()));
    }
}