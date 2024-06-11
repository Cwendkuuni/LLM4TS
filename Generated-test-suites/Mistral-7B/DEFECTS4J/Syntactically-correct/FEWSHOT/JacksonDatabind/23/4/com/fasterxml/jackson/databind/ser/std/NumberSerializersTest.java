package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    // Test for Base class
    @SuppressWarnings("unchecked")
    @Test
    public void testBaseClass() {
        Base<Number> intSerializer = new IntLikeSerializer();
        assertEquals("integer", intSerializer.getSchema(null, null).getClassId());
        assertEquals("number", intSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, intSerializer._numberType);
    }

    // Test for IntegerSerializer
    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer intSerializer = new IntegerSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree(123);
        assertEquals("123", result.asText());
        assertEquals("integer", result.getClassId());
    }

    // Test for IntLikeSerializer
    @Test
    public void testIntLikeSerializer() throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree(123L);
        assertEquals("123", result.asText());
        assertEquals("integer", result.getClassId());
    }

    // Test for LongSerializer
    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer longSerializer = new LongSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree(1234567890123L);
        assertEquals("1234567890123", result.asText());
        assertEquals("number", result.getClassId());
    }

    // Test for FloatSerializer
    @SuppressWarnings("deprecation")
    @Test
    public void testFloatSerializer() throws Exception {
        FloatSerializer floatSerializer = new FloatSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree((float) 1.23);
        assertEquals("1.23", result.asText());
        assertEquals("number", result.getClassId());
    }

    // Test for DoubleSerializer
    @SuppressWarnings("deprecation")
    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree(1.23456789);
        assertEquals("1.23456789", result.asText());
        assertEquals("number", result.getClassId());
    }

    // Test for ShortSerializer
    @Test
    public void testShortSerializer() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        JsonNode result = objectWriter.writeValueAsTree((short) 123);
        assertEquals("123", result.asText());
        assertEquals("integer", result.getClassId());
    }

    // Test for addAll method
    @Test
    public void testAddAllMethod() {
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()));
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()));
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Short.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Long.TYPE.getName()));
    }
}