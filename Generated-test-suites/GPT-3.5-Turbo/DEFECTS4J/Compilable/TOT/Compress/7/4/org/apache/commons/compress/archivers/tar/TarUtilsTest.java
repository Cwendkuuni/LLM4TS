package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_withValidInput_returnExpectedResult() {
        byte[] buffer = {49, 52, 55, 0, 50, 51, 52};
        int offset = 0;
        int length = buffer.length;
        long expected = 147;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_withLeadingSpace_returnExpectedResult() {
        byte[] buffer = {32, 49, 52, 55, 0, 50, 51, 52};
        int offset = 0;
        int length = buffer.length;
        long expected = 147;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_withTrailingSpace_returnExpectedResult() {
        byte[] buffer = {49, 52, 55, 32, 50, 51, 52};
        int offset = 0;
        int length = buffer.length;
        long expected = 147;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_withInvalidDigit_throwIllegalArgumentException() {
        byte[] buffer = {49, 52, 55, 0, 50, 51, 52, 56};
        int offset = 0;
        int length = buffer.length;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_withValidInput_returnExpectedResult() {
        byte[] buffer = {102, 111, 111, 98, 97, 114, 0, 0, 0};
        int offset = 0;
        int length = buffer.length;
        String expected = "foobar";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName_withTrailingNull_returnExpectedResult() {
        byte[] buffer = {102, 111, 111, 98, 97, 114, 0, 0, 0};
        int offset = 0;
        int length = 6;
        String expected = "fooba";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_withValidInput_fillBufferWithExpectedValues() {
        String name = "foobar";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = name.length();
        int expectedOffset = length;
        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        for(int i = 0; i < length; i++) {
            assertEquals((byte) name.charAt(i), buf[i]);
        }
        for(int i = length; i < buf.length; i++) {
            assertEquals((byte) 0, buf[i]);
        }
    }

    @Test
    public void testFormatNameBytes_withBufferLongerThanName_fillBufferWithExpectedValues() {
        String name = "foobar";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        int expectedOffset = length;
        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        for(int i = 0; i < name.length(); i++) {
            assertEquals((byte) name.charAt(i), buf[i]);
        }
        for(int i = name.length(); i < buf.length; i++) {
            assertEquals((byte) 0, buf[i]);
        }
    }

    @Test
    public void testFormatOctalBytes_withValidInput_fillBufferWithExpectedValues() {
        long value = 147;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = buf.length;
        int expectedOffset = length;
        int resultOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        for(int i = 0; i < length-2; i++) {
            assertEquals((byte) '0', buf[i]);
        }
        assertEquals((byte) ' ', buf[length-2]);
        assertEquals((byte) 0, buf[length-1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_withValueTooLarge_throwIllegalArgumentException() {
        long value = 1234567890123456789L;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = buf.length;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum_withValidInput_returnExpectedResult() {
        byte[] buf = {49, 52, 55, 0, 50, 51, 52};
        long expected = 587;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}