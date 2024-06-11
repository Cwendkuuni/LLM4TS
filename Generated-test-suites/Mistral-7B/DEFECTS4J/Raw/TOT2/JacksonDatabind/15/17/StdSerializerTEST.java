package com.fasterxml.jackson.databind.ser.std; // import statements omitted for brevity

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<String> stringSerializer;

    @Before
    public void setup() {
        this.stringSerializer = new StdSerializer<String>(String.class);
    }

    @Test
    public void testSerializeString() throws IOException {
        String input = "Hello World";
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        stringSerializer.serialize(input, jsonGenerator, serializerProvider);

        String output = jsonGenerator.getOutputContentAsString();
        assertEquals("\"Hello World\"", output);
    }

    @Test
    public void testGetSchemaString() throws JsonMappingException {
        JsonNode schema = stringSerializer.getSchema(createMockSerializerProvider(null), String.class);
        assertEquals(createSchemaNode("string").toString(), schema.toString());
    }

    // Add test cases for other methods with similar structure

    // Test edge cases and exceptions

    @Test(expected = IOException.class)
    public void testSerializeNullString() throws IOException {
        String input = null;
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        SerializerProvider serializerProvider = createMockSerializerProvider();

        stringSerializer.serialize(input, jsonGenerator, serializerProvider);
    }

    // Add test cases for other methods with edge cases and exceptions

    // Combine all test cases into a single test file
}