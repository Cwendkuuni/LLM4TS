package com.fasterxml.jackson.databind.ser.std; // import statements go here

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws Exception {
        NumberSerializers numberSerializers = new NumberSerializers();
        IntegerSerializer integerSerializer = numberSerializers.new IntegerSerializer();

        // Test serialization of an integer value
        ObjectMapper mapper = new ObjectMapper();
        Integer intValue = 123;
        String json = mapper.writeValueAsString(intValue);
        JsonParser parser = mapper.getFactory().createParser(json);
        int actualInt = parser.readValueAs(Integer.class);
        assertEquals(intValue, actualInt);

        // Test serialization of a negative integer value
        intValue = -123;
        json = mapper.writeValueAsString(intValue);
        parser = mapper.getFactory().createParser(json);
        actualInt = parser.readValueAs(Integer.class);
        assertEquals(intValue, actualInt);

        // Test exception handling when trying to serialize a non-integer value
        String nonIntegerValue = "abc";
        try {
            json = mapper.writeValueAsString(nonIntegerValue);
            parser = mapper.getFactory().createParser(json);
            parser.nextToken(); // read next token to trigger exception
            fail("Expected JsonMappingException when serializing non-integer value");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    // Repeat the above pattern for other serializers: ShortSerializer, LongSerializer, etc.

    @Test
    public void testAddAllMethod() throws Exception {
        NumberSerializers numberSerializers = new NumberSerializers();
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        // Test that all expected serializers are added to the Map
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        // Add other assertions for other serializers
    }
}