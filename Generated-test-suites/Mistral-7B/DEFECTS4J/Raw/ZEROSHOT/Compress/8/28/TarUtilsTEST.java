package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ByteArrayOutputStream;
import java.util.ByteArrayInputStream;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    private static final int BUFFER_SIZE = 12;

    private byte[] inputBuffer;
    private byte[] outputBuffer;
    private ByteArrayOutputStream byteStream;
    private ByteArrayInputStream byteInputStream;

    /**
     * Initialize test data.
     */
    @Before
    public void setUp() {
        inputBuffer = new byte[BUFFER_SIZE];
        outputBuffer = new byte[BUFFER_SIZE];
        byteStream = new ByteArrayOutputStream(BUFFER_SIZE);
        byteInputStream = new ByteArrayInputStream(new byte[0]);
    }

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        // Test with valid octal strings
        for (int i = 0; i < 10; ++i) {
            long expected = (long) i;
            long actual = TarUtils.parseOctal(inputBuffer, 0, BUFFER_SIZE);
            assertEquals(expected, actual);
        }

        // Test with empty buffer
        long actual = TarUtils.parseOctal(inputBuffer, 0, 0);
        assertEquals(0, actual);

        // Test with invalid octal string
        inputBuffer[0] = (byte) 'A';
        inputBuffer[1] = (byte) '1';
        try {
            TarUtils.parseOctal(inputBuffer, 0, BUFFER_SIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        // Test with valid name strings
        for (String name : new String[]{"name1", "name2", "name3"}) {
            String expected = name;
            int actualLength = TarUtils.parseName(inputBuffer, 0, BUFFER_SIZE).length();
            String actual = new String(inputBuffer, 0, actualLength);
            assertEquals(expected, actual);
        }

        // Test with empty buffer
        assertEquals("", TarUtils.parseName(inputBuffer, 0, BUFFER_SIZE));

        // Test with invalid name string
        inputBuffer[0] = (byte) 'A';
        inputBuffer[1] = (byte) '1';
        try {
            TarUtils.parseName(inputBuffer, 0, BUFFER_SIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        // Test with valid name strings
        for (String name : new String[]{"name1", "name2", "name3"}) {
            ByteArrayOutputStream tempByteStream = new ByteArrayOutputStream();
            String formattedName = TarUtils.formatNameBytes(name, outputBuffer, 0, BUFFER_SIZE).toString();
            TarUtils.formatNameBytes(name, inputBuffer, 0, BUFFER_SIZE);
            assertEquals(formattedName, new String(inputBuffer, 0, BUFFER_SIZE));
            assertEquals(formattedName.length(), TarUtils.parseName(inputBuffer, 0, BUFFER_SIZE).length());
        }

        // Test with empty name
        TarUtils.formatNameBytes("", outputBuffer, 0, BUFFER_SIZE);
        assertEquals("", new String(outputBuffer, 0, BUFFER_SIZE));

        // Test with name longer than buffer
        String name = "name123456789012345678901234567890";
        byte[] formattedNameBytes = new byte[BUFFER_SIZE];
        TarUtils.formatNameBytes(name, inputBuffer, 0, BUFFER_SIZE);
        String formattedName = new String(inputBuffer, 0, BUFFER_SIZE);
        assertEquals(formattedName.substring(0, BUFFER_SIZE), formattedName);
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        // Test with valid values
        for (long value : new long[]{0, 1, 127, 1234567, 1234567890}) {
            ByteArrayOutputStream tempByteStream = new ByteArrayOutputStream();
            TarUtils.formatUnsignedOctalString(value, outputBuffer, 0, BUFFER_SIZE);
            assertEquals(Long.toOctalString(value), new String(outputBuffer, 0, BUFFER_SIZE));
        }

        // Test with value longer than buffer
        long value = 123456789012345678901234567890L;
        byte[] formattedValueBytes = new byte[BUFFER_SIZE];
        TarUtils.formatUnsignedOctalString(value, inputBuffer, 0, BUFFER_SIZE);
        String formattedValue = new String(inputBuffer, 0, BUFFER_SIZE);
        assertEquals(formattedValue.substring(0, BUFFER_SIZE), formattedValue);
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        // Test with valid values
        for (long value : new long[]{0, 1, 127, 1234567, 1234567890}) {
            ByteArrayOutputStream tempByteStream = new ByteArrayOutputStream();
            TarUtils.formatOctalBytes(value, outputBuffer, 0, BUFFER_SIZE);
            byte[] formattedValueBytes = new byte[BUFFER_SIZE];
            TarUtils.formatUnsignedOctalString(value, formattedValueBytes, 0, BUFFER_SIZE);
            assertEquals(formattedValueBytes, outputBuffer);
        }

        // Test with value longer than buffer
        long value = 123456789012345678901234567890L;
        byte[] formattedValueBytes = new byte[BUFFER_SIZE];
        TarUtils.formatUnsignedOctalString(value, formattedValueBytes, 0, BUFFER_SIZE);
        byte[] formattedValueBytes2 = new byte[BUFFER_SIZE];
        TarUtils.formatOctalBytes(value, formattedValueBytes2, 0, BUFFER_SIZE);
        assertEquals(formattedValueBytes, formattedValueBytes2);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        // Test with valid input
        byte[] input = "header".getBytes();
        long expected = 0x7b32f5e7;
        long actual = TarUtils.computeCheckSum(input);
        assertEquals(expected, actual);

        // Test with empty input
        long actual2 = TarUtils.computeCheckSum(new byte[0]);
        assertEquals(0, actual2);
    }
}