package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for {@link GeneratorBase#version()} method.
     */
    @Test
    public void testVersion() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        assertEquals("2.12.1", generator.version().toString());
    }

    /**
     * Test for {@link GeneratorBase#getCurrentValue()} and {@link GeneratorBase#setCurrentValue(Object)} methods.
     */
    @Test
    public void testCurrentValue() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        Object currentValue = "current value";
        generator.setCurrentValue(currentValue);
        assertEquals(currentValue, generator.getCurrentValue());
    }

    /**
     * Test for {@link GeneratorBase#isEnabled(Feature)} and {@link GeneratorBase#enable(Feature)} methods.
     */
    @Test
    public void testEnableDisableFeatures() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    /**
     * Test for {@link GeneratorBase#writeString(String)} method.
     */
    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        String testString = "test string";
        generator.writeString(testString);
        assertEquals(testString, mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeRawValue(String)} method.
     */
    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        String testString = "test string";
        generator.writeRawValue(testString);
        assertEquals(testString, mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeRawValue(char[])} method.
     */
    @Test
    public void testWriteRawValueChars() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        char[] testChars = {'t', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g'};
        generator.writeRawValue(testChars, 0, testChars.length);
        assertEquals(new String(testChars), mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeRawValue(SerializableString)} method.
     */
    @Test
    public void testWriteRawValueSerializableString() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(String.class);
        SerializableString testString = mapper.readTree("{\"value\":\"test string\"}").get("value");
        generator.writeRawValue(testString);
        assertEquals(testString.getValue(), mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(int)} method.
     */
    @Test
    public void testWriteNumberInt() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Number.class);
        generator.writeNumber(42);
        assertEquals("42", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(long)} method.
     */
    @Test
    public void testWriteNumberLong() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Number.class);
        generator.writeNumber(1234567890123456L);
        assertEquals("1234567890123456", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(double)} method.
     */
    @Test
    public void testWriteNumberDouble() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Number.class);
        generator.writeNumber(Math.PI);
        assertEquals("3.141592653589793", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(float)} method.
     */
    @Test
    public void testWriteNumberFloat() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Number.class);
        generator.writeNumber(1.23f);
        assertEquals("1.23", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(BigDecimal)} method.
     */
    @Test
    public void testWriteNumberBigDecimal() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Number.class);
        BigDecimal testDecimal = new BigDecimal("1234567890.1234567890");
        generator.writeNumber(testDecimal);
        assertEquals(testDecimal.toString(), mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeBoolean(boolean)} method.
     */
    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Boolean.class);
        generator.writeBoolean(true);
        assertEquals("true", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeNull()} method.
     */
    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Object.class);
        generator.writeNull();
        assertEquals("null", mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method.
     */
    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Object.class);
        String testString = "test string";
        generator.writeObject(testString);
        assertEquals(testString, mapper.readTree(generator.getOutputBuffer().toString()).asText());
    }

    /**
     * Test for {@link GeneratorBase#writeTree(TreeNode)} method.
     */
    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = mapper.getPolymorphicSerializer(Object.class);
        TreeNode testNode = mapper.readTree("{\"value\":\"test string\"}");
        generator.writeTree(testNode);
        assertEquals(testNode.toString(), mapper.readTree(generator.getOutputBuffer().toString()).toString());
    }
}