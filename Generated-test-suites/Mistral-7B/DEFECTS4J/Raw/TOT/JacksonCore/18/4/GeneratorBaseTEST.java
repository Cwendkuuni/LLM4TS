package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Tests for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    /**
     * Test constructor with default features and ObjectCodec.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        assertEquals(GeneratorBase.DEFAULT_FEATURES, generator.getFeatureMask());
        assertEquals(JsonFactory.class.getName(), generator.getCodec().getClass().getName());
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), generator.version());
        generator.close();
    }

    /**
     * Test constructor with custom features and ObjectCodec.
     */
    @Test
    public void testCustomConstructor() throws IOException {
        int customFeatures = GeneratorBase.DEFAULT_FEATURES | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        JsonGenerator generator = new GeneratorBase(customFeatures, new JsonFactory(), null);
        assertEquals(customFeatures, generator.getFeatureMask());
        assertEquals(JsonFactory.class.getName(), generator.getCodec().getClass().getName());
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), generator.version());
        generator.close();
    }

    /**
     * Test checking if features are enabled or disabled.
     */
    @Test
    public void testFeatures() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        // Test default features
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_BINARY));
        // Test enabling and disabling features
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        // Test disabling all features
        generator.setFeatureMask(0);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_BINARY));
        generator.close();
    }

    /**
     * Test writing different types of values.
     */
    @Test
    public void testWritingValues() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), outputStream));

        // Test writing a null value
        generator.writeNull();
        assertEquals("null", outputStream.toString());

        // Test writing a boolean value
        generator.writeBoolean(true);
        assertEquals("true", outputStream.toString());
        generator.writeBoolean(false);
        assertEquals("false", outputStream.toString());

        // Test writing a number value
        generator.writeNumber(123);
        assertEquals("123", outputStream.toString());
        generator.writeNumber(123.45);
        assertEquals("123.45", outputStream.toString());

        // Test writing a string value
        String testString = "Test string value";
        generator.writeString(testString);
        assertEquals(testString, outputStream.toString());

        // Test writing a BigDecimal value
        BigDecimal bigDecimal = new BigDecimal("1234567890.123456789");
        generator.writeNumber(bigDecimal);
        assertEquals(bigDecimal.toString(), outputStream.toString());

        generator.close();
    }

    /**
     * Test writing arrays and objects.
     */
    @Test
    public void testWritingArraysAndObjects() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        // Test writing an empty array
        generator.writeStartArray();
        generator.writeEndArray();
        assertEquals("[]", generator.getOutputContext().toString());

        // Test writing a single value array
        generator.writeStartArray();
        generator.writeNumber(123);
        generator.writeEndArray();
        assertEquals("[123]", generator.getOutputContext().toString());

        // Test writing a nested array
        generator.writeStartArray();
        generator.writeStartArray();
        generator.writeNumber(456);
        generator.writeEndArray();
        generator.writeEndArray();
        assertEquals("[[]", generator.getOutputContext().toString());

        // Test writing an empty object
        generator.writeStartObject();
        generator.writeEndObject();
        assertEquals("{}", generator.getOutputContext().toString());

        // Test writing a simple object
        generator.writeStartObject();
        generator.writeFieldName("testField");
        generator.writeNumber(789);
        generator.writeEndObject();
        assertEquals("{\"testField\":789}", generator.getOutputContext().toString());

        generator.close();
    }

    /**
     * Test writing binary values.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWritingBinaries() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        generator.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream("Test binary data".getBytes()), "Test binary data".getBytes().length);
    }

    /**
     * Test writing raw values.
     */
    @Test
    public void testWritingRawValues() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        generator.writeRawValue("Test raw string value");
        assertEquals("\"Test raw string value\"", generator.getOutputContext().toString());

        generator.writeRawValue("Test raw binary data".getBytes());
        assertEquals("\"Test raw binary data\"", generator.getOutputContext().toString());

        generator.writeRawValue("Test raw byte[] data".getBytes(), 0, "Test raw byte[] data".getBytes().length);
        assertEquals("\"Test raw byte[] data\"", generator.getOutputContext().toString());

        generator.close();
    }

    /**
     * Test writing invalid values.
     */
    @Test(expected = JsonProcessingException.class)
    public void testWritingInvalidValues() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());

        // Test writing an invalid number
        generator.writeNumber(-1);

        // Test writing an invalid boolean
        generator.writeBoolean("Test boolean value");

        // Test writing an invalid null
        generator.writeNull();
        generator.writeNumber(1);

        // Test writing an invalid string
        generator.writeString("Test string value\u0000");

        // Test writing an invalid BigDecimal
        generator.writeNumber(new BigDecimal("Test BigDecimal value"));

        generator.close();
    }

    /**
     * Test releasing buffers.
     */
    @Test
    public void testReleasingBuffers() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutputContext(new JsonWriteContext(new DefaultPrettyPrinter(), outputStream));

        // Write some data
        generator.writeStartArray();
        generator.writeNumber(123);
        generator.writeString("Test string value");
        generator.writeEndArray();

        generator._releaseBuffers();
        assertEquals("", outputStream.toString());

        generator.close();
    }

    /**
     * Test writing Unicode characters using surrogate pairs.
     */
    @Test
    public void testWritingUnicodeCharacters() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());

        // Test writing a single character using a surrogate pair
        generator.writeString("\uD835\uDD24");
        assertEquals("\uD835\uDD24", generator.getOutputContext().toString());

        // Test writing multiple characters using surrogate pairs
        generator.writeString("\uD835\uDD1E\uDC00\uDCAF");
        assertEquals("\uD835\uDD1E\uDC00\uDCAF", generator.getOutputContext().toString());

        generator.close();
    }

    /**
     * Test strict duplicate detection.
     */
    @Test(expected = JsonProcessingException.class)
    public void testStrictDuplicateDetection() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES | Feature.STRICT_DUPLICATE_DETECTION.getMask(), new JsonFactory());
        generator.writeStartArray();
        generator.writeNumber(123);
        generator.writeNumber(123);
        generator.writeEndArray();
    }

    /**
     * Test escaping non-ASCII characters.
     */
    @Test
    public void testEscapingNonAsciiCharacters() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES | Feature.ESCAPE_NON_ASCII.getMask(), new JsonFactory());
        generator.setHighestNonEscapedChar(127);
        generator.writeString("Test \u00FCstring value");
        assertEquals("Test \\u00FCstring value", generator.getOutputContext().toString());

        generator.close();
    }

    /**
     * Test string representation.
     */
    @Test
    public void testToString() throws IOException {
        JsonGenerator generator = new GeneratorBase(GeneratorBase.DEFAULT_FEATURES, new JsonFactory());
        assertEquals("JsonGenerator{features=[" + GeneratorBase.Feature.NAME_WRITE_NUMBERS_AS_STRINGS + "], codec=class com.fasterxml.jackson.core.JsonFactory$DefaultUseDefaults$1, outputContext=null, closed=false}", generator.toString());

        generator.setFeatureMask(GeneratorBase.DEFAULT_FEATURES | Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertEquals("JsonGenerator{features=[" + GeneratorBase.Feature.NAME_WRITE_NUMBERS_AS_STRINGS + "], codec=class com.fasterxml.jackson.core.JsonFactory$DefaultUseDefaults$1, outputContext=null, closed=false}", generator.toString());

        generator.setFeatureMask(0);
        assertEquals("JsonGenerator{features=[], codec=class com.fasterxml.jackson.core.JsonFactory$DefaultUseDefaults$1, outputContext=null, closed=false}", generator.toString());

        generator.close();
    }
}