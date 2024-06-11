package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("127");
        assertEquals(Short.parseShort("127"), shortSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        shortSerializer.serialize(Short.parseShort("127"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("127"), parsedJson.getValueAsTree());
    }

    @Test
    public void testIntegerSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("12345");
        assertEquals(Integer.parseInt("12345"), integerSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(Integer.parseInt("12345"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("12345"), parsedJson.getValueAsTree());
    }

    @Test
    public void testIntLikeSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("12345");
        assertEquals(Integer.parseInt("12345"), intLikeSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(Integer.parseInt("12345"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("12345"), parsedJson.getValueAsTree());
    }

    @Test
    public void testLongSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("9223372036854775807");
        assertEquals(Long.parseLong("9223372036854775807"), longSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        longSerializer.serialize(Long.parseLong("9223372036854775807"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("9223372036854775807"), parsedJson.getValueAsTree());
    }

    @Test
    public void testFloatSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("3.1415926535897932384626433832795");
        assertEquals(Float.parseFloat("3.1415926535897932384626433832795"),
                floatSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        floatSerializer.serialize(Float.parseFloat("3.1415926535897932384626433832795"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("3.1415926535897932384626433832795"), parsedJson.getValueAsTree());
    }

    @Test
    public void testDoubleSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonParser jsonParser = objectMapper.getFactory().createParser("6.283185307179586476925286766559005768");
        assertEquals(Double.parseDouble("6.283185307179586476925286766559005768"),
                doubleSerializer.deserialize(jsonParser, JsonNodeFactory.instance, null));
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        doubleSerializer.serialize(Double.parseDouble("6.283185307179586476925286766559005768"), jsonGenerator, new SerializerProvider());
        JsonParser parsedJson = objectMapper.readTree(new ByteArrayOutputStream());
        assertEquals(TextNode.valueOf("6.283185307179586476925286766559005768"), parsedJson.getValueAsTree());
    }

    // Add edge cases and exception handling test cases here
}