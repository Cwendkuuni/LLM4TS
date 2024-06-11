package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        NumberSerializers numberSerializers = new NumberSerializers();
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        numberSerializers.addAll(allDeserializers);
        // Check if all expected serializers have been added
        assertEquals(10, allDeserializers.size());
        assertEquals(Integer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(Integer.TYPE, allDeserializers.get(Integer.TYPE.getName()));
        // ... similar assertions for other types
    }

    @Test
    public void testIntegerSerializerSerializesInteger() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer number = 10;
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerSerializesNegativeNumber() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer number = -10;
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerSerializesZero() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer number = 0;
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number, parser.readValueAs(Integer.class));
    }

    // Add test cases for other methods with similar logic

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerThrowsExceptionWhenNullIsProvided() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(null);
        JsonParser parser = mapper.getFactory().createParser(json);
        parser.nextToken(); // consume the START_ARRAY token
        integerSerializer.serialize(null, parser.getCodec().getSerializerProvider(), parser);
    }

}