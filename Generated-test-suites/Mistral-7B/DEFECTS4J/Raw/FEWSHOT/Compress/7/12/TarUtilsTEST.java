package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testData = new byte[] { 0x30, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37 };
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        assertEquals(017564, result);

        byte[] testDataInvalid = new byte[] { 0x38, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37 };
        try {
            TarUtils.parseOctal(testDataInvalid, 0, testDataInvalid.length);
            assert false; // Should throw an exception
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] testDataTrailing = new byte[] { 0x30, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37, 0x00 };
        long resultTrailing = TarUtils.parseOctal(testDataTrailing, 0, testDataTrailing.length);
        assertEquals(017564, resultTrailing);

        byte[] testDataLeading = new byte[] { 0x00, 0x30, 0x31, 0x33, 0x34, 0x35, 0x36, 0x37 };
        long resultLeading = TarUtils.parseOctal(testDataLeading, 0, testDataLeading.length);
        assertEquals(017564, resultLeading);
    }

    @Test
    public void testParseName() {
        byte[] testData = new byte[] { 0x54, 0x65, 0x73, 0x74, 0x52, 0x61, 0x6d, 0x79, 0x00 };
        String result = TarUtils.parseName(testData, 0, testData.length);
        assertEquals("TestFile", result);

        byte[] testDataEmpty = new byte[] { 0x00 };
        String resultEmpty = TarUtils.parseName(testDataEmpty, 0, testDataEmpty.length);
        assertNull(resultEmpty);

        byte[] testDataNull = null;
        String resultNull = TarUtils.parseName(testDataNull, 0, 0);
        assertNull(resultNull);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TestFile";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        assertEquals(10, offset);
        assertArrayEquals(new byte[] { 0x54, 0x65, 0x73, 0x74, 0x52, 0x61, 0x6d, 0x79, 0x00 }, buffer);

        String nameShort = "Test";
        byte[] bufferShort = new byte[5];
        int offsetShort = TarUtils.formatNameBytes(nameShort, bufferShort, 0, bufferShort.length);

        assertEquals(5, offsetShort);
        assertArrayEquals(new byte[] { 0x54, 0x65, 0x73, 0x74, 0x00 }, bufferShort);

        String nameLong = "ThisIsATestFile";
        byte[] bufferLong = new byte[20];
        int offsetLong = TarUtils.formatNameBytes(nameLong, bufferLong, 0, bufferLong.length);

        assertEquals(20, offsetLong);
        assertArrayEquals(new byte[] { 0x54, 0x68, 0x69, 0x73, 0x73, 0x61, 0x61, 0x74, 0x65, 0x73, 0x74, 0x61, 0x72, 0x6c, 0x65, 0x61, 0x74, 0x65, 0x00 }, bufferLong);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertEquals(10, offset);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringTooSmall() {
        long value = 256;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertEquals(12, offset);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesTooSmall() {
        long value = 256;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 456;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        assertEquals(12, offset);
        assertArrayEquals(new byte[] { 0x64, 0x65, 0x36, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x20 }, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesTooSmall() {
        long value = 256;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        long result = TarUtils.computeCheckSum(testData);
        long computedChecksum = 0;

        for (int i = 0; i < testData.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & testData[i];
        }

        assertEquals(computedChecksum, result);
    }
}