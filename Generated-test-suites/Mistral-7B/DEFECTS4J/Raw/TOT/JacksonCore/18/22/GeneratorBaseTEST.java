package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GeneratorBaseTest {

    private GeneratorBase jsonGenerator;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws IOException {
        jsonGenerator = new GeneratorBase(0, null, null);
        objectMapper = new ObjectMapper();
    }

    /**
     * Test writeString method with valid ASCII characters
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteString_ASCII() throws JsonProcessingException {
        jsonGenerator.writeString("Hello World!");
        String actual = jsonGenerator.getCurrentValue().asText();
        Assertions.assertEquals("Hello World!", actual);
    }

    /**
     * Test writeString method with invalid UTF-8 characters
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteString_InvalidUTF8() throws JsonProcessingException {
        jsonGenerator.setCurrentValue("abad\uD835\uDD1E\uD835\uDD30");
        jsonGenerator.writeString("Surrogate pair");
        String actual = jsonGenerator.getCurrentValue().asText();
        String expected = "{\"@type\":\"q\"," +
                "\"c\":[{\"c\":1,\"index\":0,\"size\":5,\"bytes\":[73,108,97,108,105]},{" +
                "\"c\":2,\"index\":5,\"size\":11,\"bytes\":[195,189,191,194,195,187,194,191,193,195,194]}]," +
                "\"value\":\"Surrogate pair\"}";
        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(actual.getBytes()));
        String parsed = parser.readValueAsString();
        Assertions.assertEquals(expected, parsed);
    }

    /**
     * Test writeNumber method with valid number
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteNumber() throws JsonProcessingException {
        jsonGenerator.setCurrentValue(123);
        jsonGenerator.writeNumber(123);
        String actual = jsonGenerator.getCurrentValue().asText();
        Assertions.assertEquals("123", actual);
    }

    /**
     * Test writeNull method
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteNull() throws JsonProcessingException {
        jsonGenerator.writeNull();
        String actual = jsonGenerator.getCurrentValue().asText();
        Assertions.assertEquals("null", actual);
    }

    /**
     * Test writeBoolean method with valid boolean value
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        jsonGenerator.setCurrentValue(true);
        jsonGenerator.writeBoolean(true);
        String actual = jsonGenerator.getCurrentValue().asText();
        Assertions.assertEquals("true", actual);
    }

    /**
     * Test writeBoolean method with invalid boolean value
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteBoolean_Invalid() throws JsonProcessingException {
        jsonGenerator.setCurrentValue("invalidBooleanValue");
        try {
            jsonGenerator.writeBoolean(true);
        } catch (JsonGenerationException e) {
            // expected
        }
        String actual = jsonGenerator.getCurrentValue().asText();
        Assertions.assertEquals("invalidBooleanValue", actual);
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws IOException {
        jsonGenerator.close();
        Assertions.assertTrue(jsonGenerator.isClosed());
    }

    /**
     * Test isClosed method
     */
    @Test
    public void testIsClosed() {
        Assertions.assertFalse(jsonGenerator.isClosed());
        jsonGenerator.close();
        Assertions.assertTrue(jsonGenerator.isClosed());
    }
}