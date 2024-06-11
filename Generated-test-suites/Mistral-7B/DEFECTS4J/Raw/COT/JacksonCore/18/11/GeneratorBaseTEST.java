package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    

    // Extract and list all the public methods including their signatures

    /*
     * Methods with no argument and no return type
     */

    @Test
    public void test_verifyValueWrite_whenNull_thenWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNull();
        generator.flush();
    }

    @Test
    public void test_releaseBuffers() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._releaseBuffers();
        assertTrue(generator.isClosed());
    }

    /*
     * Methods with arguments and no return type
     */

    @Test
    public void test_constructDefaultPrettyPrinter() {
        GeneratorBase generator = new GeneratorBase(0, null);
        PrettyPrinter pp = generator._constructDefaultPrettyPrinter();
        assertNotNull(pp);
    }

    @Test
    public void test_decodeSurrogate() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        int c = generator._decodeSurrogate(0xD800, 0xDBFF);
        assertEquals(0x10000 + (0xD800 - 0xD800) << 10 + (0xDBFF - 0xDBFF), c);

        c = generator._decodeSurrogate(0xD800, 0xDC00); // invalid surrogate pair
        fail();
    }

    /*
     * Methods with arguments and return type
     */

    @Test
    public void test_version() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), generator.version());
    }

    @Test
    public void test_getCurrentValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCurrentValue("Test Value");
        assertEquals("Test Value", generator.getCurrentValue());
    }

    @Test
    public void test_setCurrentValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCurrentValue(new BigDecimal("3.14"));
        assertEquals(new BigDecimal("3.14"), generator.getCurrentValue());
    }

    @Test
    public void test_isEnabled() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void test_getFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());
    }

    @Test
    public void test_enable() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void test_disable() throws IOException {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void test_setFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.setFeatureMask(0);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void test_overrideStdFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    /*
     * Methods with no arguments and a return type
     */

    @Test
    public void test_getObjectCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getObjectCodec());
    }

    @Test
    public void test_getOutputContext() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNotNull(generator.getOutputContext());
    }

    /*
     * Methods with arguments and a return type
     */

    @Test
    public void test_writeFieldName() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeFieldName(new SerializableString("Test Name"));
        generator.flush();
    }

    @Test
    public void test_writeString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString(new SerializableString("Test String"));
        generator.flush();
    }

    @Test
    public void test_writeRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("Test Raw Value");
        generator.flush();
    }

    @Test
    public void test_writeRawValue_withOffsetAndLength() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        String testString = "Test Raw Value";
        generator.writeRawValue(testString, 0, testString.length());
        generator.flush();
    }

    @Test
    public void test_writeRawValue_withCharArray() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        String testString = "Test Raw Value";
        generator.writeRawValue(testString.toCharArray(), 0, testString.length());
        generator.flush();
    }

    @Test
    public void test_writeRawValue_withSerializableString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        SerializableString testString = new SerializableString("Test Raw Value");
        generator.writeRawValue(testString);
        generator.flush();
    }

    @Test
    public void test_writeObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeObject(new Object());
        generator.flush();
    }

    @Test
    public void test_writeTree() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        TreeNode treeNode = new ObjectMapper().readTree("{}");
        generator.writeTree(treeNode);
        generator.flush();
    }

    @Test
    public void test_writeNumber() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(123);
        generator.flush();
    }

    @Test
    public void test_writeNumber_long() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(123456789012345L);
        generator.flush();
    }

    @Test
    public void test_writeNumber_double() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(123.45);
        generator.flush();
    }

    @Test
    public void test_writeNumber_float() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(123.45f);
        generator.flush();
    }

    @Test
    public void test_writeNumber_BigDecimal() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(new BigDecimal("123.45"));
        generator.flush();
    }

    @Test
    public void test_writeBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeBoolean(true);
        generator.flush();
    }

    @Test
    public void test_writeNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNull();
        generator.flush();
    }

    /*
     * Methods with exceptions
     */

    @Test(expected = IOException.class)
    public void test_writeStartArray_whenClosed_thenIOException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.writeStartArray();
    }

    @Test(expected = IOException.class)
    public void test_writeEndArray_whenClosed_thenIOException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.writeEndArray();
    }

    @Test(expected = IOException.class)
    public void test_writeStartObject_whenClosed_thenIOException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.writeStartObject();
    }

    @Test(expected = IOException.class)
    public void test_writeEndObject_whenClosed_thenIOException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.writeEndObject();
    }

    @Test(expected = IOException.class)
    public void test_writeNumber_whenNotNumber_thenIOException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber('A');
    }

    @Test(expected = IOException.class)
    public void test_write_whenNotWriter_thenIOException() throws IOException {
        Object obj = new Object();
        _verifyExceptionThrown(new IOException("Not a GeneratorBase instance: " + obj.getClass().getName()), () -> obj.writeNumber(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_decodeSurrogate_whenInvalidSurrogatePair_thenIllegalArgumentException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._decodeSurrogate(0xD800, 0xDC00);
    }

    @Test
    public void test_writeBinary_whenUnsupported_thenUnsupportedOperationException() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        InputStream input = new ByteArrayInputStream("base64".getBytes());
        assertThrows(UnsupportedOperationException.class, () -> generator.writeBinary(null, input, 0));
    }

    /*
     * Helper methods
     */

    private void _verifyExceptionThrown(Class<? extends Throwable> expectedExceptionType, Runnable testCode) {
        try {
            testCode.run();
            fail();
        } catch (Throwable e) {
            if (expectedExceptionType.isInstance(e)) {
                return;
            }
            throw e;
        }
    }

    
}