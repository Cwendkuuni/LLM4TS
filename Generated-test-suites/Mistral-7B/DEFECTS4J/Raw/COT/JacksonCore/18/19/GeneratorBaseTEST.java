package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    

    @Test
    public void testConstructor() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNotNull(generator);
    }

    @Test
    public void testIsEnabled_WriteNumbersAsStrings() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable_WriteNumbersAsStrings() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable_WriteNumbersAsStrings() throws Exception {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_NegativeMask() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_HigherThanMaxMask() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnable_UnsupportedFeature() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.WRITE_BOOLEAN_AS_INTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisable_UnsupportedFeature() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.disable(Feature.WRITE_BOOLEAN_AS_INTS);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFeatureMask_ClosedGenerator() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.setFeatureMask(0);
    }

    @Test
    public void testVersion() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals("unknown", generator.version().toString());

        generator = new GeneratorBase(2, null);
        assertEquals("2.0.0", generator.version().toString());
    }

    @Test
    public void testGetCurrentValue() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getCurrentValue());

        generator.setCurrentValue("Test");
        assertEquals("Test", generator.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCurrentValue("Test");
        assertEquals("Test", generator.getCurrentValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCurrentValue_ClosedGenerator() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.setCurrentValue("Test");
    }

    @Test
    public void testIsEnabled_WriteEscapedJson() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testEnable_WriteEscapedJson() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testDisable_WriteEscapedJson() throws Exception {
        GeneratorBase generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetHighestNonEscapedChar_ClosedGenerator() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.setHighestNonEscapedChar(127);
    }

    @Test
    public void testSetHighestNonEscapedChar_Default() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getHighestNonEscapedChar());

        generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(127, generator.getHighestNonEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        GeneratorBase generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(127, generator.getHighestNonEscapedChar());

        generator.setHighestNonEscapedChar(150);
        assertEquals(150, generator.getHighestNonEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedChar_NegativeValue() throws Exception {
        GeneratorBase generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        generator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testWriteString() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("Test");

        byte[] output = new byte[10];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteString_Empty() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("");

        byte[] output = new byte[5];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("\"\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteRawValue() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("Test");
        generator.flush();

        byte[] output = new byte[5];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteRawValue_Empty() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("");
        generator.flush();

        byte[] output = new byte[3];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("\"\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteRawValue_EmptyStringWithLength() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("", 4);
        generator.flush();

        byte[] output = new byte[4];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("\"\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteRawValue_StringWithLength() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("Test", 6);
        generator.flush();

        byte[] output = new byte[6];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
    }

    @Test
    public void testWriteRawValue_CharArrayWithLength() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        char[] test = {'T', 'e', 's', 't'};
        generator.writeRawValue(test, 4);
        generator.flush();

        byte[] output = new byte[6];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
    }

    @Test(expected = IOException.class)
    public void testWriteString_Null() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString(null);
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_Null() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteString_InvalidUtf8() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("\uD800\uDC00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteRawValue_InvalidUtf8() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("\uD800\uDC00".getBytes());
    }

    @Test
    public void testWriteRawValue_SurrogatePair() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        char character = '\uD844\uDD1E';
        generator.writeRawValue(Character.toString(character).getBytes());
        generator.flush();

        byte[] output = new byte[6];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("\uD844\uDD1E\r\n", new String(output, 0, length));
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Int() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Long() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42L);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Double() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42.0);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_Float() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42.0f);
    }

    @Test(expected = IOException.class)
    public void testWriteNumber_BigDecimal() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        BigDecimal decimal = new BigDecimal("42");
        generator.writeNumber(decimal);
    }

    @Test(expected = IOException.class)
    public void testWriteBoolean() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeBoolean(true);
    }

    @Test(expected = IOException.class)
    public void testWriteNull() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNull();

        byte[] output = new byte[6];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("null\r\n", new String(output, 0, length));
    }

    @Test(expected = IOException.class)
    public void testWriteObject() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeObject(new Object());
    }

    @Test(expected = IOException.class)
    public void testWriteTree() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeTree(new TreeNode() {});
    }

    @Test
    public void testFlush() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("Test");
        generator.flush();

        byte[] output = new byte[5];
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
    }

    @Test
    public void testClose() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        byte[] output = new byte[10];
        generator.writeString("Test");
        int length = generator.getOutputBuffer().read(output, 0, output.length);
        assertEquals("Test\r\n", new String(output, 0, length));
        generator.close();

        length = generator.getOutputBuffer().read(output, 0, output.length);
        assertArrayEquals(new byte[0], output);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteValue_ClosedGenerator() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("Test");
        generator.close();
        generator.writeString("Test");
    }

    @Test
    public void testAsString() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null);
        BigDecimal decimal = new BigDecimal("42");
        String result = generator._asString(decimal);
        assertEquals("42", result);
    }

    
}