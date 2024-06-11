package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctalSuccess() {
        byte[] buffer =new byte[]{(byte)0x27,(byte)0x21,(byte)0x20,(byte)0x20,(byte)0x75};
        long expected =01212075L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected,actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalFailureInvalidTrailingByte() {
        byte[] buffer =new byte[]{(byte)0x27,(byte)0x21,(byte)0x20,(byte)0x75,(byte)0x23};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalFailureInvalidLength() {
        byte[] buffer =new byte[]{(byte)0x27};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalFailureMissingTrailingByteOrSpace() {
        byte[] buffer =new byte[]{(byte)0x27,(byte)0x21,(byte)0x20};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalFailureLeadingNUL() {
        byte[] buffer =new byte[]{(byte)0x00,(byte)0x27,(byte)0x21,(byte)0x20,(byte)0x75,(byte)0x20};
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0,actual);
    }

    @Test
    public void testParseOctalOrBinarySuccessBinary() {
        byte[] buffer =new byte[]{(byte)0x41,(byte)0x30};
        long expected =29L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected,actual);
    }

    @Test
    public void testParseOctalOrBinarySuccessOctal() {
        byte[] buffer =new byte[]{(byte)0x31,(byte)0x20,(byte)0x21,(byte)0x20,(byte)0x75};
        long expected =01212075L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected,actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryFailureInvalidLength() {
        byte[] buffer =new byte[]{(byte)0x27};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryFailureInvalidTrailingByte() {
        byte[] buffer =new byte[]{(byte)0x27,(byte)0x21,(byte)0x20,(byte)0x75,(byte)0x23};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryFailureBinaryNumberExceedsMaxLong() {
        byte[] buffer =new byte[]{0x40, (byte)0x80, 0xFF};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBooleanSuccess() {
        byte[] buffer =new byte[]{0x01};
        boolean expected =true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected,actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanFailureInvalidByte() {
        byte[] buffer =new byte[]{0x02};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseNameSuccess() {
        byte[] buffer =new byte[]{(byte)0x41,(byte)0x62,(byte)0x72,(byte)0x69,(byte)0x6e,(byte)0x67,(byte)0x20,
                (byte)0x42,(byte)0x61,(byte)0x73,(byte)0x74,(byte)0x65};
        String expected ="ABC fileName";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected,actual);
    }

    @Test
    public void testFormatNameBytesSuccess() {
        String name ="myName";
        byte[] buffer =new byte[10];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String parsedName = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(name,parsedName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesFailureInvalidLengthName() {
        String name ="myName";
        byte[] buffer =new byte[2];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatNameBytesFailureBufferTooSmall() {
        String name ="myName";
        byte[] buffer =new byte[5];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String parsedName = TarUtils.parseName(buffer, 0, buffer.length);
        fail("Expected IllegalArgumentException, but got :"+parsedName);
    }

    @Test
    public void testFormatUnsignedOctalStringSuccess() {
        long value =12345L;
        byte[] buffer =new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String parsedValue = new String(buffer,0,buffer.length-2);
        assertEquals("012345",parsedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringFailureInvalidLength() {
        long value =12345L;
        byte[] buffer =new byte[2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytesSuccess() {
        long value =12345L;
        byte[] buffer =new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String parsedValue = new String(buffer,0,buffer.length-2);
        assertEquals("012345  ",parsedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesFailureInvalidLength() {
        long value =12345L;
        byte[] buffer =new byte[2];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSumSuccess() {
        byte[] buffer =new byte[]{0x01, 0x23, 0x34, 0x45, 0x56};
        long expected =(0x01+0x23+0x34+0x45+0x56)& TarUtils.BYTE_MASK;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected,actual);
    }

    @Test
    public void testComputeCheckSumFailureBufferTooSmall() {
        byte[] buffer =new byte[]{0x01};
        long actual = TarUtils.computeCheckSum(buffer);
        fail("Expected IllegalArgumentException, but got :"+actual);
    }
}