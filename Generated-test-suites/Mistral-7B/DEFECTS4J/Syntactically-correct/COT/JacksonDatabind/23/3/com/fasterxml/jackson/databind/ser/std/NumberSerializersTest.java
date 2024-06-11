package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    

    @Test
    public void testIntegerSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Integer input = 123;
        String output = mapper.writeValueAsString(input, serializer);
        Integer outputInteger = mapper.readValue(output, Integer.class);

        assertEquals(input, outputInteger);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntegerSerializerWithNonIntegerValue() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        String input = "not an integer";
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Integer.class);
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        JsonSerializer<Number> serializer = NumberSerializers.IntLikeSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Integer input = 456;
        String output = mapper.writeValueAsString(input, serializer);
        Integer outputInteger = mapper.readValue(output, Integer.class);

        assertEquals(input, outputInteger);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntLikeSerializerWithNonNumberValue() throws Exception {
        JsonSerializer<Number> serializer = NumberSerializers.IntLikeSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        String input = "not a number";
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Integer.class);
    }

    @Test
    public void testLongSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.LongSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Long input = 789L;
        String output = mapper.writeValueAsString(input, serializer);
        Long outputLong = mapper.readValue(output, Long.class);

        assertEquals(input, outputLong);
    }

    @Test(expected = JsonProcessingException.class)
    public void testLongSerializerWithNonLongValue() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.LongSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        String input = "not a long";
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Long.class);
    }

    @Test
    public void testShortSerializer() throws Exception {
        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Short input = 23;
        String output = mapper.writeValueAsString(input, serializer);
        Short outputShort = mapper.readValue(output, Short.class);

        assertEquals(input, outputShort);
    }

    @Test(expected = JsonProcessingException.class)
    public void testShortSerializerWithNonShortValue() throws Exception {
        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Integer input = 456;
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Short.class);
    }

    @Test
    public void testFloatSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.FloatSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Float input = 123.45f;
        String output = mapper.writeValueAsString(input, serializer);
        Float outputFloat = mapper.readValue(output, Float.class);

        assertEquals(input, outputFloat);
    }

    @Test(expected = JsonProcessingException.class)
    public void testFloatSerializerWithNonFloatValue() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.FloatSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Double input = 789.0;
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Float.class);
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.DoubleSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Double input = 456.78;
        String output = mapper.writeValueAsString(input, serializer);
        Double outputDouble = mapper.readValue(output, Double.class);

        assertEquals(input, outputDouble);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDoubleSerializerWithNonDoubleValue() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.DoubleSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        Float input = 123.45f;
        String output = mapper.writeValueAsString(input, serializer);

        mapper.readValue(output, Double.class);
    }

    @Test
    public void testAddAllMethodWithValidTypes() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        for (Map.Entry<String, JsonSerializer<?>> entry : allDeserializers.entrySet()) {
            String className = entry.getKey();
            JsonSerializer<?> serializer = entry.getValue();

            assertEquals(className, serializer.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllMethodWithInvalidType() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers numberSerializers = new NumberSerializers();

        Class<?> invalidClass = String.class;
        numberSerializers.addAll(allDeserializers);
    }

    
}