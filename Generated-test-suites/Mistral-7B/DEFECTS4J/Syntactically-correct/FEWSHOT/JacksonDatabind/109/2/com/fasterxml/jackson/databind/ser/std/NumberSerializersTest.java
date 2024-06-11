package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws JsonProcessingException, IOException {
        Short serializableShort = Short.valueOf((short) 123);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(serializableShort);
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        Short deserializedShort = parser.readValueAs(Short.class);

        assertEquals(serializableShort, deserializedShort);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException, IOException {
        Integer serializableInteger = Integer.valueOf(123);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(serializableInteger);
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        Integer deserializedInteger = parser.readValueAs(Integer.class);

        assertEquals(serializableInteger, deserializedInteger);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException, IOException {
        Integer serializableInteger = Integer.valueOf(123);
        Number number = serializableInteger;

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(number);
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        Integer deserializedInteger = parser.readValueAs(Integer.class);

        assertEquals(serializableInteger, deserializedInteger);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException, IOException {
        Long serializableLong = Long.valueOf(123L);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(serializableLong);
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        Long deserializedLong = parser.readValueAs(Long.class);

        assertEquals(serializableLong, deserializedLong);
    }

    @Test
    @JsonSerialize(using = NumberSerializers.DoubleSerializer.class)
    public static class TestDoubleSerializerClass {
        public double value = 3.14;
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException, IOException {
        TestDoubleSerializerClass serializableDouble = new TestDoubleSerializerClass();

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(serializableDouble);
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        TestDoubleSerializerClass deserializedDouble = parser.readValueAs(TestDoubleSerializerClass.class);

        assertEquals(serializableDouble.value, deserializedDouble.value, 0.0);
    }

    @Test
    public void testContextualSerializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        Base<?> serializer = new IntegerSerializer(Integer.class);
        module.addSerializer(Integer.class, serializer);
        mapper.registerModule(module);

        Integer serializableInteger = Integer.valueOf(123);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(serializableInteger);
        JsonParser parser = mapper.getFactory().createParser(json);
        Integer deserializedInteger = parser.readValueAs(Integer.class);

        assertEquals(serializableInteger, deserializedInteger);
    }
}