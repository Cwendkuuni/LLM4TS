package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.StringReader;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        // Test 1: Basic functionality
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        String expectedJson = "{\"name\":\"John Doe\"}";
        String actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        assertEquals(expectedJson, actualJson);

        // Test 2: Edge case: Empty string as value
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        expectedJson = "{\"name\":\"\"}";
        actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        assertEquals(expectedJson, actualJson);

        // Test 3: Error scenario: Null value
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeNull();
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        expectedJson = "{\"name\":null}";
        actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testWriteFieldName() throws IOException {
        // Test 1: Basic functionality
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        String expectedJson = "{\"name\":\"John Doe\"}";
        String actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        JsonParser parser = factory.createParser(new StringReader(actualJson));
        assertTrue(parser.nextToken().isObject());
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("name", parser.getCurrentName());

        parser.close();

        // Test 2: Edge case: Empty string as field name
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        expectedJson = "{\"\":\"John Doe\"}";
        actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        JsonParser parser2 = factory.createParser(new StringReader(actualJson));
        assertTrue(parser2.nextToken().isObject());
        assertTrue(parser2.nextToken().isFieldName());
        assertEquals("", parser2.getCurrentName());

        parser2.close();

        // Test 3: Error scenario: Null field name
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(null);
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        String expectedErrorMessage = "Failed to serialize value of type java.lang.String to JSON String";
        String actualErrorMessage = "";

        try {
            jsonGenerator.getOutputFactory().getSerializerProvider().defaultSerializeValue(null, jsonGenerator, null);
            jsonGenerator.flush();
        } catch (IOException e) {
            actualErrorMessage = e.getMessage();
        }

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        // Test 1: Basic functionality
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        String expectedJson = "{\"name\":\"John Doe\"}";
        String actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        assertEquals(expectedJson, actualJson);

        // Test 2: Edge case: Nested objects
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("firstName");
        jsonGenerator.writeString("John");
        jsonGenerator.writeFieldName("lastName");
        jsonGenerator.writeString("Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();

        jsonGenerator.flush();

        expectedJson = "{\"name\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}";
        actualJson = new String(System.in.readAllBytes()); // Read the generated JSON from the console

        assertEquals(expectedJson, actualJson);

        // Test 3: Error scenario: Write start object twice
        jsonGenerator = factory.createGenerator(System.out);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStartObject();

        try {
            jsonGenerator.writeStringField("name", "John Doe");
        } catch (JsonProcessingException e) {
            String expectedErrorMessage = "Failed to serialize value of type java.lang.String to JSON String";
            String actualErrorMessage = e.getMessage();

            assertEquals(expectedErrorMessage, actualErrorMessage);
        }

        jsonGenerator.close();
    }
}