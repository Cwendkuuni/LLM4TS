package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        assertEquals("10", serialize(serializer, value));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 100;
        assertEquals("100", serialize(serializer, value));
    }

    @Test
    public void testIntLikeSerializer(){
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        Integer value = 500;
        assertEquals("500", serialize(serializer, value));
    }

    @Test
    public void testLongSerializer(){
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 1000000L;
        assertEquals("1000000", serialize(serializer, value));
    }

    @Test
    public void testFloatSerializer(){
        FloatSerializer serializer = FloatSerializer.instance;
        Float value = 3.14f;
        assertEquals("3.14", serialize(serializer, value));
    }

    @Test
    public void testDoubleSerializer(){
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 2.71828;
        assertEquals("2.71828", serialize(serializer, value));
    }

    private String serialize(JsonSerializer<?> serializer, Object value) {
        try {
            // Create a dummy JsonGenerator and SerializerProvider
            JsonFactory factory = new JsonFactory();
            JsonGenerator gen = factory.createGenerator(System.out);
            SerializerProvider provider = new ObjectMapper().getSerializerProvider();
            serializer.serialize(value, gen, provider);
            gen.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}