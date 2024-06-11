package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test cases for NumberSerializers class
 */
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers, mapper.getSerializationConfig());
        assertEquals(11, allDeserializers.size()); // Check the number of deserializers added
    }

    @Test
    public void testSerializeShort() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        ShortSerializer shortSerializer = new ShortSerializer();

        Short number = Short.MIN_VALUE;
        shortSerializer.serialize(number, gen, mapper.getSerializationConfig());
        gen.flush(); // Flush the generator to get the serialized string
        String serializedShort = gen.getOutputBuffer().toString();
        gen.close();

        JsonParser parser = mapper.getFactory().getParser();
        Map<String, Object> deserializedShort = mapper.readValue(new ByteArrayInputStream(serializedShort.getBytes()), new TypeReference<Map<String, Object>>() {});
        Object deserializedShortValue = deserializedShort.get("@type"); // Assuming the serialized value has "@type" key
        assertEquals(Short.class, Class.forName((String) deserializedShortValue)); // Check the deserialized type is Short

        Short deserializedShortNum = mapper.readValue(new ByteArrayInputStream(serializedShort.getBytes()), Short.class);
        assertEquals(number, deserializedShortNum); // Check the deserialized value is equal to the original number
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        IntegerSerializer intSerializer = new IntegerSerializer();

        String invalidNumberString = "abc";
        intSerializer.serialize(invalidNumberString, gen, mapper.getSerializationConfig()); // Assume an invalid number is string "abc"
        gen.flush();
        gen.close();
    }

    // Add more test cases for other methods (ShortSerializer, IntegerSerializer, ...) and edge cases
}