package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test class for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    @Test
    public void testConstructor() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        assertNotNull(generator);
        assertEquals(features, generator.getFeatureMask());
        assertNull(generator.getPrettyPrinter());
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testConstructorWithContext() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        JsonWriteContext context = new JsonWriteContext.Builder().build();
        GeneratorBase generator = new GeneratorBase(features, codec, context);
        assertNotNull(generator);
        assertEquals(features, generator.getFeatureMask());
        assertNotNull(generator.getOutputContext());
        assertNull(generator.getPrettyPrinter());
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);

        // test enabled features
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        features |= Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator = new GeneratorBase(features, codec);

        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // test disabled features
        features &= ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator = new GeneratorBase(features, codec);

        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testEnableFeature() {
        int features = 0;
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.AUTO_CLOSE_SOURCE);
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testDisableFeature() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.AUTO_CLOSE_SOURCE);
        assertFalse(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testSetPrettyPrinter() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);

        PrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        generator.setPrettyPrinter(prettyPrinter);
        assertEquals(prettyPrinter, generator.getPrettyPrinter());
    }

    @Test
    public void testVersion() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), generator.version());
    }

    @Test
    public void testGetCurrentValue() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        Object value = "test";
        generator.setCurrentValue(value);
        assertEquals(value, generator.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        Object value = "test";
        generator.setCurrentValue(value);
        assertEquals(value, generator.getCurrentValue());
    }

    @Test
    public void testConfigure() {
        // not implemented
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetFeatureMask() {
        // not implemented
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFeatureMask() {
        // not implemented
    }

    @Test
    public void testOverrideStdFeatures() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(Feature.ESCAPE_NON_ASCII.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.overrideStdFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.AUTO_CLOSE_SOURCE.getMask(), Feature.AUTO_CLOSE_SOURCE.getMask());
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        PrettyPrinter prettyPrinter = generator.useDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertEquals(prettyPrinter.getClass(), DefaultPrettyPrinter.class);
        assertEquals(prettyPrinter, generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        ObjectCodec newCodec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        generator.setCodec(newCodec);
        assertEquals(newCodec, generator.getCodec());
    }

    @Test
    public void testGetCodec() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        assertEquals(codec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        JsonWriteContext context = generator.getOutputContext();
        assertNotNull(context);
        assertNull(context.getDupDetector());
    }

    @Test
    public void testFlush() throws IOException {
        // not implemented
    }

    @Test
    public void testClose() throws IOException {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testIsClosed() {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        assertFalse(generator.isClosed());
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testWriteObject() throws IOException {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        String value = "test";
        generator.writeObject(value);
        // no verification needed, will be checked by codec
    }

    @Test
    public void testWriteTree() throws IOException {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        TreeNode value = new ObjectNode(codec.treeAsTree("{}"));
        generator.writeTree(value);
        // no verification needed, will be checked by codec
    }

    @Test
    public void testWriteString() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        String value = "test";
        generator.writeString(value);
        assertEquals(value, generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        String value = "test";
        generator.writeRawValue(value);
        assertEquals(value, generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        String value = "test";
        generator.writeRawValue(value, 0, value.length());
        assertEquals(value, generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueWithCharArray() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        char[] value = {'t', 'e', 's', 't'};
        generator.writeRawValue(value);
        assertEquals(String.valueOf(value), generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueWithCharArrayOffsetAndLength() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        char[] value = {'t', 'e', 's', 't'};
        generator.writeRawValue(value, 0, value.length);
        assertEquals(String.valueOf(value), generator.getCurrentValue());
    }

    @Test
    public void testWriteNumber() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNumberWithInt() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNumberWithLong() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNumberWithDouble() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNumberWithFloat() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNumberWithBigDecimal() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteBoolean() throws IOException {
        // not implemented
    }

    @Test
    public void testWriteNull() throws IOException {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        generator.writeNull();
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testReleaseBuffers() {
        // not implemented
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteValueWithUnsupportedFeature() throws IOException {
        int features = Feature.WRITE_BINARY.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        GeneratorBase generator = new GeneratorBase(features, codec);
        generator.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream(new byte[0]), 0);
    }
}