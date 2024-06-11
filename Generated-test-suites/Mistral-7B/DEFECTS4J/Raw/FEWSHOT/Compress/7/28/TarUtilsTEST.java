package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ByteArrayOutputStream;
import java.util.Random;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{'0', '7', '7', '7', '4', '5', '6', '1', '0', '1', '0', '1', '1', '1', '0', '0', '0'};
        assertEquals(01777456111L, TarUtils.parseOctal(testData, 0, testData.length));

        byte[] testDataInvalidLength = new byte[]{'0', '7', '7', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testDataInvalidLength, 0, testDataInvalidLength.length));

        byte[] testDataInvalidInput = new byte[]{'0', 'a', '7', '7', '4', '5', '6', '1', '0', '1', '0', '1', '1', '1', '0', '0', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testDataInvalidInput, 0, testDataInvalidInput.length));

        byte[] testDataNegativeOffset = new byte[]{'0', '7', '7', '7', '4', '5', '6', '1', '0', '1', '0', '1', '1', '1', '0', '0', '0'};
        assertThrows(IndexOutOfBoundsException.class, () -> TarUtils.parseOctal(testDataNegativeOffset, -1, testDataNegativeOffset.length));

        byte[] testDataNull = null;
        assertNull(TarUtils.parseOctal(testDataNull, 0, 0));
    }

    @Test
    public void testParseName() {
        byte[] testData = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 'p', 'a', 'c', 'k', 'g', 'z', '\u0000'};
        assertEquals("entry.tar.gz.pack.gz", TarUtils.parseName(testData, 0, testData.length));

        byte[] testDataNull = null;
        assertNull(TarUtils.parseName(testDataNull, 0, 0));

        byte[] testDataInvalidLength = new byte[]{'t', 'a', 'r', ' '};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(testDataInvalidLength, 0, testDataInvalidLength.length));

        byte[] testDataNegativeOffset = new byte[]{'t', 'a', 'r', ' '};
        assertThrows(IndexOutOfBoundsException.class, () -> TarUtils.parseName(testDataNegativeOffset, -1, testDataNegativeOffset.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "entry.tar.gz.pack.gz";
        byte[] testData = new byte[name.length() + 1];
        int result = TarUtils.formatNameBytes(name, testData, 0, testData.length);
        assertArrayEquals(new byte[]{'e', 'n', 't', 'r', 'y', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 'p', 'a', 'c', 'k', 'g', 'z'}, testData, 0, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long testValue = 12345;
        byte[] testData = new byte[10];
        int result = TarUtils.formatUnsignedOctalString(testValue, testData, 0, testData.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '0', '0', '0', '0'}, testData, 0, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long testValue = 12345;
        byte[] testData = new byte[12];
        int result = TarUtils.formatOctalBytes(testValue, testData, 0, testData.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', '0', '0', '0', '0'}, testData, 0, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long testValue = 123456789012345L;
        byte[] testData = new byte[12];
        int result = TarUtils.formatLongOctalBytes(testValue, testData, 0, testData.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'}, testData, 0, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long testValue = 123456789012345L;
        byte[] testData = new byte[13];
        int result = TarUtils.formatCheckSumOctalBytes(testValue, testData, 0, testData.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', ' '}, testData, 0, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testData = new byte[]{0x75, 0x72, 0x69, 0x63, 0x74, 0x65, 0x64, 0x00, 0x14};
        long expectedCheckSum = 0x51F5A760C76214L;
        assertEquals(expectedCheckSum, TarUtils.computeCheckSum(testData));

        byte[] testDataNull = null;
        assertThrows(NullPointerException.class, () -> TarUtils.computeCheckSum(testDataNull));
    }
}

class RandomDataGenerator {
    private final Random random;

    public RandomDataGenerator() {
        random = new Random();
    }

    public byte[] generateRandomData(int length) {
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
}