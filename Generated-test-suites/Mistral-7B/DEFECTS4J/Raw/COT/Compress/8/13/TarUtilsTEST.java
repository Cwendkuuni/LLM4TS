package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long expectedResult = 0175441L;
        int offset = 0;
        int length = buffer.length;

        long actualResult = parseOctal(buffer, offset, length);
        TestUtils.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        int offset = 0;
        int length = buffer.length;

        parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j', (byte) 'e', (byte) 'ct', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'd', (byte) 'e', (byte) 'm', (byte) 'o', (byte) 'n', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.'};
        String expectedResult = "tar.obj.format.text.tar.gzip.demontar.gzip";
        int offset = 0;
        int length = buffer.length;

        String actualResult = parseName(buffer, offset, length);
        TestUtils.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.obj.format.text.tar.gzip.demontar.gzip";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        offset = formatNameBytes(name, buffer, offset, length);

        byte[] expectedResult = new byte[]{
            (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't',
            (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'd',
            (byte) 'e', (byte) 'm', (byte) 'o', (byte) 'n', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z',
            (byte) 'i', (byte) 'p'
        };

        TestUtils.assertArrayEquals(expectedResult, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        formatUnsignedOctalString(value, buffer, offset, length);

        byte[] expectedResult = new byte[]{
            (byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1'
        };

        TestUtils.assertArrayEquals(expectedResult, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;

        offset = formatOctalBytes(value, buffer, offset, length);

        byte[] expectedResult = new byte[]{
            (byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) ' '
        };

        TestUtils.assertArrayEquals(expectedResult, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        offset = formatLongOctalBytes(value, buffer, offset, length);

        byte[] expectedResult = new byte[]{
            (byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) ' '
        };

        TestUtils.assertArrayEquals(expectedResult, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;

        offset = formatCheckSumOctalBytes(value, buffer, offset, length);

        byte[] expectedResult = new byte[]{
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '0', (byte) ' '
        };

        TestUtils.assertArrayEquals(expectedResult, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) ' ', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        long expectedResult = 2540531442L;

        long actualResult = computeCheckSum(buffer);
        TestUtils.assertEquals(expectedResult, actualResult);
    }
}