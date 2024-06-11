package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals("123", shortSerializer.serializeToString(Short.valueOf("123")));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertEquals("456", integerSerializer.serializeToString(Integer.valueOf("456")));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals("789", intLikeSerializer.serializeToString(Byte.valueOf("789")));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertEquals("-12345678901234567890", longSerializer.serializeToString(Long.valueOf("-12345678901234567890")));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        try {
            assertEquals("3.14", floatSerializer.serializeToString(Float.valueOf("3.14")));
            assertEquals("NaN", floatSerializer.serializeToString(Float.NaN));
            assertEquals("Infinity", floatSerializer.serializeToString(Float.POSITIVE_INFINITY));
            assertEquals("-Infinity", floatSerializer.serializeToString(Float.NEGATIVE_INFINITY));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        ObjectWriter objectWriter = new ObjectMapper().writer();
        try {
            assertEquals("3.14", doubleSerializer.serializeToString(Double.valueOf("3.14")));
            assertEquals("NaN", doubleSerializer.serializeToString(Double.NaN));
            assertEquals("Infinity", doubleSerializer.serializeToString(Double.POSITIVE_INFINITY));
            assertEquals("-Infinity", doubleSerializer.serializeToString(Double.NEGATIVE_INFINITY));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAllToMap() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(6, allDeserializers.size());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()));
        JsonSerializer<?> shortSerializer = allDeserializers.get(Short.class.getName());
        assertEquals(ShortSerializer.class, shortSerializer.getClass());
    }
}