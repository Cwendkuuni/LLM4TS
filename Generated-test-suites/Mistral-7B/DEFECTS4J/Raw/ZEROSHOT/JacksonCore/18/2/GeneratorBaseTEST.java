package com.fasterxml.jackson.core.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {
    private ObjectMapper mapper;
    private GeneratorBase generator;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        generator = mapper.getFactory().createGenerator(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
    }

    @After
    public void teardown() throws IOException {
        if (generator != null) {
            generator.close();
        }
    }

    @Test
    public void testIsEnabled_WriteNumbersAsStrings() throws IOException {
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testEnable_WriteNumbersAsStrings() throws IOException {
        assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable_WriteNumbersAsStrings() throws IOException {
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IOException.class)
    public void testWriteString_Null() throws IOException {
        generator.writeString(null);
    }

    @Test
    public void testWriteString() throws IOException {
        String testString = "test";
        generator.writeString(testString);
        assertEquals(testString, generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(testString, mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        String testString = "test";
        generator.writeRawValue(testString);
        assertEquals(testString, generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(testString, mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteRawValue_String_Offset() throws IOException {
        String testString = "test";
        int offset = 0;
        int length = testString.length();
        generator.writeRawValue(testString, offset, length);
        assertEquals(testString, generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(testString, mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteRawValue_CharArray() throws IOException {
        char[] testCharArray = {'t', 'e', 's', 't'};
        generator.writeRawValue(testCharArray);
        assertEquals(new String(testCharArray), generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(new String(testCharArray), mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteRawValue_CharArray_Offset() throws IOException {
        char[] testCharArray = {'t', 'e', 's', 't'};
        int offset = 0;
        int length = 4;
        generator.writeRawValue(testCharArray, offset, length);
        assertEquals(new String(testCharArray), generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(new String(testCharArray), mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        SerializableString testString = mapper.valueToTree("test");;
        generator.writeRawValue(testString);
        assertEquals(testString.getValue(), generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(testString.getValue(), mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteFieldName() throws IOException {
        String fieldName = "test";
        generator.writeFieldName("test");
        assertEquals(fieldName, generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(fieldName, mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertEquals(JsonNull.class.getName(), generator.getCurrentValueAsString());
        generator.flush();
        assertEquals(JsonNull.class.getName(), mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Int() throws IOException {
        generator.writeNumber(1);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Long() throws IOException {
        generator.writeNumber(1L);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Double() throws IOException {
        generator.writeNumber(1.0);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Float() throws IOException {
        generator.writeNumber(1.0f);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_BigDecimal() throws IOException {
        generator.writeNumber(new BigDecimal("1"));
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertEquals("true", generator.getCurrentValueAsString());
        generator.flush();
        assertEquals("true", mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteObject() throws IOException {
        String testString = "test";
        SerializableString testFieldName = mapper.valueToTree("fieldName");;
        Object testObject = new Object(){};
        generator.writeStartObject();
        generator.writeFieldName(testFieldName);
        generator.writeString(testString);
        generator.writeEndObject();
        assertEquals("{\"fieldName\":\""+testString+"\"}", generator.getCurrentValueAsString());
        generator.flush();
        assertEquals("{\"fieldName\":\""+testString+"\"}", mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }

    @Test
    public void testWriteTree() throws IOException {
        String testString = "test";
        Object testObject = new Object(){};
        TreeNode testTree = mapper.valueToTree(testObject);
        generator.writeTree(testTree);
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.JsonNode\"}", generator.getCurrentValueAsString());
        generator.flush();
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.JsonNode\"}", mapper.readValueAsString(generator.getOutputBuffer().toString()));
    }
}