package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    // Test data
    private static final byte[] TEST_BUFFER = {49, 50, 51, 52, 53, 54, 55, 56, 57};
    private static final int TEST_OFFSET = 0;
    private static final int TEST_LENGTH = 9;
    
    @Before
    public void setUp() {
        // Set up any necessary test data
    }
    
    @Test
    public void testParseOctal() {
        long expected = 123456789;
        long result = TarUtils.parseOctal(TEST_BUFFER, TEST_OFFSET, TEST_LENGTH);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseName() {
        String expected = "123456789";
        String result = TarUtils.parseName(TEST_BUFFER, TEST_OFFSET, TEST_LENGTH);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "123456789";
        byte[] expected = {49, 50, 51, 52, 53, 54, 55, 56, 57};
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] expected = {48, 49, 53, 71, 53, 55, 13, 13, 13};
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertArrayEquals(expected, buf);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] expected = {48, 49, 53, 71, 53, 55, 32, 0};
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] expected = {48, 49, 53, 71, 53, 55, 32, 13, 13};
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] expected = {48, 49, 53, 71, 53, 55, 13, 32, 13};
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testComputeCheckSum() {
        long expected = 495;
        long result = TarUtils.computeCheckSum(TEST_BUFFER);
        assertEquals(expected, result);
    }
}