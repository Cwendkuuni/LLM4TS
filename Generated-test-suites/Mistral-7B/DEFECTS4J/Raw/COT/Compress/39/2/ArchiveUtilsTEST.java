package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    private ArchiveEntry archiveEntryMock;

    @Before
    public void setUp() {
        archiveEntryMock = mock(ArchiveEntry.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for toString method
     */
    @Test
    public void testToString() {
        final ArchiveEntry entry = new ArchiveEntry("testfile.txt");
        final String expected = "-        0 testfile.txt";
        assertTrue(ArchiveUtils.toString(entry).equals(expected));
    }

    /**
     * Test for matchAsciiBuffer method with expected string and buffer
     */
    @Test
    public void testMatchAsciiBufferStringAndBuffer() {
        final String expected = "teststring";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test for matchAsciiBuffer method with expected string, buffer, offset and length
     */
    @Test
    public void testMatchAsciiBufferStringBufferOffsetLength() {
        final String expected = "teststring";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test for isEqual method with boolean ignoreTrailingNulls set to false
     */
    @Test
    public void testIsEqualFalse() {
        final byte[] buffer1 = toAsciiBytes("teststring1");
        final byte[] buffer2 = toAsciiBytes("teststring2");
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test for isEqual method with boolean ignoreTrailingNulls set to true
     */
    @Test
    public void testIsEqualTrue() {
        final byte[] buffer1 = toAsciiBytes("teststring1");
        final byte[] buffer2 = toAsciiBytes("teststring1");
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, true));
    }

    /**
     * Test for isEqual method with buffer1 and buffer2 having same contents
     */
    @Test
    public void testIsEqualSameContents() {
        final byte[] buffer1 = toAsciiBytes("teststring1");
        final byte[] buffer2 = new byte[buffer1.length];
        System.arraycopy(buffer1, 0, buffer2, 0, buffer1.length);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test for isEqualWithNull method
     */
    @Test
    public void testIsEqualWithNull() {
        final byte[] buffer1 = toAsciiBytes("teststring1");
        final byte[] buffer2 = new byte[buffer1.length + 1];
        System.arraycopy(buffer1, 0, buffer2, 0, buffer1.length);
        buffer2[buffer1.length] = (byte) 0;
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test for isEqual method with empty buffers
     */
    @Test
    public void testIsEqualEmptyBuffers() {
        final byte[] buffer1 = new byte[0];
        final byte[] buffer2 = new byte[0];
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test for isEqual method with different buffer lengths
     */
    @Test
    public void testIsEqualDifferentBufferLengths() {
        final byte[] buffer1 = toAsciiBytes("teststring1");
        final byte[] buffer2 = toAsciiBytes("teststring1");
        final byte[] buffer3 = new byte[buffer1.length + 1];
        System.arraycopy(buffer1, 0, buffer3, 0, buffer1.length);
        buffer3[buffer1.length] = (byte) 0;
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer3, 0, buffer3.length, false));
    }

    /**
     * Test for isArrayZero method with zero length array
     */
    @Test
    public void testIsArrayZeroZeroLengthArray() {
        final byte[] array = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(array, 0));
    }

    /**
     * Test for isArrayZero method with non-zero array
     */
    @Test
    public void testIsArrayZeroNonZeroArray() {
        final byte[] array = new byte[] {1, 2, 3};
        assertFalse(ArchiveUtils.isArrayZero(array, 0));
    }

    /**
     * Test for isArrayZero method with zero value array
     */
    @Test
    public void testIsArrayZeroZeroValueArray() {
        final byte[] array = new byte[3];
        Arrays.fill(array, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(array, 0));
    }

    /**
     * Test for sanitize method
     */
    @Test
    public void testSanitize() {
        final String input = "test\u0001string\u0002with\u001Fspecial\u007Fcharacters.";
        final String expected = "test?string?with??special??characters.";
        assertTrue(ArchiveUtils.sanitize(input).equals(expected));
    }

    /**
     * Test for toString method with null ArchiveEntry
     */
    @Test(expected = ArchiveException.class)
    public void testToStringNullArchiveEntry() {
        ArchiveUtils.toString(null);
    }
}