package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        GeneratorBase generator2 = new TestGeneratorBase(2, null);
        assertFalse(generator2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator2.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator2.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertEquals(1, generator.getFeatureMask());

        GeneratorBase generator2 = new TestGeneratorBase(2, null);
        assertEquals(2, generator2.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generator2 = new TestGeneratorBase(2, null);
        generator2.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generator2 = new TestGeneratorBase(1, null);
        generator2.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.overrideStdFeatures(1, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generator2 = new TestGeneratorBase(1, null);
        generator2.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(generator2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertNull(generator.getPrettyPrinter());
        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
        assertEquals(DefaultPrettyPrinter.class, generator.getPrettyPrinter().getClass());
    }

    @Test
    public void testSetCodec() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertNull(generator.getCodec());
        ObjectCodec mockCodec = new ObjectCodec() {};
        generator.setCodec(mockCodec);
        assertEquals(mockCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertNull(generator.getOutputContext());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeString("test");
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeRawValue("test");
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        String text = "test";
        generator.writeRawValue(text, 0, text.length());
    }

    @Test
    public void testWriteRawValueWithSerializableString() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        SerializableString text = new Utf8String("test");
        generator.writeRawValue(text);
    }

    @Test
    public void testWriteBinary() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream("test".getBytes()), "test".length());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteNumber() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNumber(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteNumberLong() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNumber(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteNumberDouble() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNumber(1.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteNumberFloat() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNumber(1.0f);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteNumberBigDecimal() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNumber(new BigDecimal("1"));
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeBoolean(true);
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeNull();
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeObject(new Object());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.writeTree(new TreeNode() {});
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.flush();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator.close();
    }

    @Test
    public void testIsClosed() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        assertFalse(generator.isClosed());

        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void test_releaseBuffers() {
        GeneratorBase generator = new TestGeneratorBase(1, null) {
            @Override
            protected void _releaseBuffers() {}
        };
        generator.releaseBuffers();
    }

    @Test(expected = IllegalStateException.class)
    public void test_verifyValueWriteWithNull() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        generator._verifyValueWrite("write value");
    }

    @Test
    public void test_constructDefaultPrettyPrinter() {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        PrettyPrinter prettyPrinter = generator._constructDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertEquals(DefaultPrettyPrinter.class, prettyPrinter.getClass());
    }

    @Test
    public void test_asString() throws IOException {
        GeneratorBase generator = new TestGeneratorBase(1, null);
        BigDecimal value = new BigDecimal("1");
        String result = generator._asString(value);
        assertEquals("1", result);
    }
}

class TestGeneratorBase extends GeneratorBase {
    TestGeneratorBase(int features, ObjectCodec codec) {
        super(features, codec, null);
    }

    TestGeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt) {
        super(features, codec, ctxt);
    }

    @Override
    protected void _releaseBuffers() {
        // Empty implementation for test
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException {
        throw new UnsupportedOperationException("This method should not be called in test");
    }

    @Override
    protected PrettyPrinter _constructDefaultPrettyPrinter() {
        return new PrettyPrinter() {};
    }
}