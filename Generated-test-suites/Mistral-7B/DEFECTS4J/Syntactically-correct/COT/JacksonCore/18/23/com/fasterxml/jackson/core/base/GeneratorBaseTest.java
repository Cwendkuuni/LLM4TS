package com.fasterxml.jackson.core.base; // adjust the package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

    private GeneratorBase generator;

    @Before
    public void setUp() {
        generator = new GeneratorBase(0, null); // create an instance with default features
    }

    @Test
    public void testVersion() {
        assertEquals("2.12.3", generator.version().toString()); // adjust the version number as needed
    }

    @Test
    public void testEnableWriteNumbersAsStrings() {
        generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisableWriteNumbersAsStrings() {
        generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseTwice() throws IOException {
        generator.close();
        generator.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testWriteNull() throws IOException {
        byte[] buffer = new byte[128];
        int written = generator.writeNull();
        assertNull(generator.getCurrentValue());
        assertEquals(-1, written); // or check the written bytes if needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBooleanNull() throws IOException {
        generator.writeBoolean(null);
    }

    @Test
    public void testWriteBooleanTrue() throws IOException {
        generator.writeBoolean(true);
        assertEquals("true", generator.getCurrentValue());
    }

    @Test
    public void testWriteBooleanFalse() throws IOException {
        generator.writeBoolean(false);
        assertEquals("false", generator.getCurrentValue());
    }

    @Test
    public void testWriteNumberInt() throws IOException {
        generator.writeNumber(Integer.MIN_VALUE);
        assertEquals("-2147483648", generator.getCurrentValue());
    }

    @Test
    public void testWriteNumberLong() throws IOException {
        generator.writeNumber(Long.MIN_VALUE);
        assertEquals("-9223372036854775808", generator.getCurrentValue());
    }

    @Test
    public void testWriteNumberDouble() throws IOException {
        generator.writeNumber(Double.MIN_VALUE);
        assertEquals("-4.94066E-324", generator.getCurrentValue());
    }

    @Test
    public void testWriteNumberFloat() throws IOException {
        generator.writeNumber(Float.MIN_VALUE);
        assertEquals("-3.4028235E38", generator.getCurrentValue());
    }

    @Test
    public void testWriteNumberBigDecimal() throws IOException {
        BigDecimal decimal = new BigDecimal("1.23456789");
        generator.writeNumber(decimal);
        assertEquals("1.23456789", generator.getCurrentValue());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("test");
        assertEquals("test", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueString() throws IOException {
        generator.writeRawValue("test");
        assertEquals("test", generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueStringWithOffset() throws IOException {
        String test = "test";
        generator.writeRawValue(test, 0, test.length());
        assertEquals(test, generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueCharArray() throws IOException {
        char[] test = {'t', 'e', 's', 't'};
        generator.writeRawValue(test, 0, test.length);
        assertEquals(new String(test), generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValueCharArrayWithOffset() throws IOException {
        char[] test = {'t', 'e', 's', 't'};
        generator.writeRawValue(test, 1, test.length - 1);
        assertEquals(new String(test, 1, test.length - 1), generator.getCurrentValue());
    }

    @Test(expected = IOException.class)
    public void testWriteBinaryNull() throws IOException {
        generator.writeBinary(null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBinaryNullInputStream() throws IOException {
        generator.writeBinary(Base64Variant.OCTET_STREAM, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBinaryNegativeLength() throws IOException {
        generator.writeBinary(Base64Variant.OCTET_STREAM, new ByteArrayInputStream("test".getBytes()), -1);
    }
}