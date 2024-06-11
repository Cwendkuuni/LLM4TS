package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    private static final int DERIVED_FEATURES_MASK =
            JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
            | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask()
            | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask();

    @Test
    public void testConstructor() {
        ObjectCodec codec = null;
        int features = DERIVED_FEATURES_MASK;
        JsonWriteContext ctxt = null;

        GeneratorBase generator = new GeneratorBase(features, codec);
        assertNotNull(generator);

        generator = new GeneratorBase(features, codec, ctxt);
        assertNotNull(generator);
    }

    @Test
    public void testVersion() {
        GeneratorBase generator = new GeneratorBase(0, null);
        Version version = generator.version();
        assertNotNull(version);
        assertEquals("2.13.0", version.toString());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisableFeature() {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);

        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));

        generator.disable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testEnableFeature() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));

        generator.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() throws Exception {
        ObjectCodec codec = null;
        JsonWriteContext ctxt = null;
        GeneratorBase generator = new GeneratorBase(0, codec, ctxt);

        int newMask = DERIVED_FEATURES_MASK;
        generator.setFeatureMask(newMask);
        assertEquals(newMask, generator.getFeatureMask());

        int changed = newMask ^ generator.getFeatureMask();
        generator._checkStdFeatureChanges(newMask, changed);

        // Test writeNumberAsStrings
        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals("123", generator.writeNumber(123).getValueAsString());

        // Test escapeNonAscii
        generator.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals("\"abc\u00E9def\"", generator.writeString("abcédef").getValueAsString());

        // Test strictDuplicateDetection
        generator.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
        try {
            generator.writeObject(new Object());
            generator.writeObject(new Object());
            fail("Expected com.fasterxml.jackson.core.JsonGenerationException");
        } catch (JsonProcessingException e) {
            // expected
        }

        generator.disable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        ObjectCodec newCodec = null;
        generator.setCodec(newCodec);
        assertEquals(newCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        ObjectCodec codec = null;
        JsonWriteContext ctxt = null;
        GeneratorBase generator = new GeneratorBase(0, codec, ctxt);

        JsonWriteContext outputContext = generator.getOutputContext();
        assertEquals(ctxt, outputContext);
    }

    @Test
    public void testWriteFieldName() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        String fieldName = "example";
        generator.writeFieldName(new SerializableString(fieldName));
        assertEquals(fieldName, generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteString() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        String testString = "example";
        generator.writeString(new SerializableString(testString));
        assertEquals(testString, generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteRawValue() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        String testString = "example";
        generator.writeRawValue(new SerializableString(testString));
        assertEquals(testString, generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteRawValueWithOffset() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        String testString = "example";
        generator.writeRawValue(new SerializableString(testString), 0, testString.length());
        assertEquals(testString, generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteRawValueWithArray() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        char[] testArray = {'e', 'x', 'a', 'm', 'p', 'l', 'e'};
        generator.writeRawValue(new SerializableString(new String(testArray)), 0, testArray.length);
        assertEquals(new String(testArray), generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteNull() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        generator.writeNull();
        assertEquals("null", generator.getCurrentValueAsString());
    }

    // Uncomment the following methods and write their test cases if you want to test write methods for primitives
    /*
    @Test
    public void testWriteNumber() throws Exception {
        // ...
    }

    @Test
    public void testWriteNumberWithInt() throws Exception {
        // ...
    }

    @Test
    public void testWriteNumberWithLong() throws Exception {
        // ...
    }

    @Test
    public void testWriteNumberWithDouble() throws Exception {
        // ...
    }

    @Test
    public void testWriteNumberWithFloat() throws Exception {
        // ...
    }

    @Test
    public void testWriteNumberWithBigDecimal() throws Exception {
        // ...
    }

    @Test
    public void testWriteBoolean() throws Exception {
        // ...
    }
    */

    @Test
    public void testWriteObject() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        Object testObject = new Object();
        generator.writeObject(testObject);
        assertEquals(testObject.getClass().getName(), generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteTree() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        TreeNode testTree = new Object();
        generator.writeTree(testTree);
        assertEquals(testTree.getClass().getName(), generator.getCurrentValueAsString());
    }

    @Test
    public void testFlush() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        generator.flush();
    }

    @Test
    public void testClose() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterClose() throws Exception {
        ObjectCodec codec = null;
        GeneratorBase generator = new GeneratorBase(0, codec);

        generator.close();
        generator.writeString(new SerializableString("test"));
    }
}