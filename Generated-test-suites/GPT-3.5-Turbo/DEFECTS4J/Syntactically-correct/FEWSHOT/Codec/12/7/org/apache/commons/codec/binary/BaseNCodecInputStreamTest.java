package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private boolean doEncode;

    /**
     * Utility method to create a BaseNCodecInputStream for testing.
     * 
     * @param baseNCodec
     *            the BaseNCodec to use
     * @param doEncode
     *            true to encode, false to decode
     * @param input
     *            the input bytes
     * @return the BaseNCodecInputStream
     */
    private BaseNCodecInputStream createInputStream(BaseNCodec baseNCodec, boolean doEncode, byte[] input) {
        InputStream in = new ByteArrayInputStream(input);
        return new BaseNCodecInputStream(in, baseNCodec, doEncode);
    }

    /**
     * Test case for read() method.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testRead() throws IOException {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        byte[] input = { 0x66, 0x6f, 0x6f }; // ASCII values for 'f', 'o', 'o'
        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, input);

        assertEquals(102, inputStream.read()); // ASCII value for 'f'
        assertEquals(111, inputStream.read()); // ASCII value for 'o'
        assertEquals(111, inputStream.read()); // ASCII value for 'o'
        assertEquals(-1, inputStream.read()); // EOF
    }

    /**
     * Test case for read(byte[], int, int) method with valid parameters.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadValidParams() throws IOException {
        byte[] input = { 0x66, 0x6f, 0x6f }; // ASCII values for 'f', 'o', 'o'
        byte[] output = new byte[3];

        // Test with doEncode = true
        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, input);

        int bytesRead = inputStream.read(output, 0, 3);
        assertEquals(3, bytesRead);
        assertEquals(102, output[0]); // ASCII value for 'f'
        assertEquals(111, output[1]); // ASCII value for 'o'
        assertEquals(111, output[2]); // ASCII value for 'o'

        // Test with doEncode = false
        baseNCodec = new BaseNCodec();
        doEncode = false;

        inputStream = createInputStream(baseNCodec, doEncode, input);

        bytesRead = inputStream.read(output, 0, 3);
        assertEquals(3, bytesRead);
        assertEquals(0, output[0]); // ASCII value for 'f'
        assertEquals(0, output[1]); // ASCII value for 'o'
        assertEquals(0, output[2]); // ASCII value for 'o'
    }

    /**
     * Test case for read(byte[], int, int) method with invalid parameters.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidParams() throws IOException {
        byte[] input = { 0x66, 0x6f, 0x6f }; // ASCII values for 'f', 'o', 'o'
        byte[] output = new byte[3];

        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, input);

        inputStream.read(output, -1, 3); // throws IndexOutOfBoundsException
    }

    /**
     * Test case for markSupported() method.
     */
    @Test
    public void testMarkSupported() {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, new byte[0]);

        assertFalse(inputStream.markSupported());
    }

    /**
     * Test case for mark() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMark() {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, new byte[0]);

        inputStream.mark(10); // throws UnsupportedOperationException
    }

    /**
     * Test case for reset() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testReset() {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, new byte[0]);

        inputStream.reset(); // throws UnsupportedOperationException
    }

    /**
     * Test case for skip() method with valid parameters.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testSkipValidParams() throws IOException {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        byte[] input = { 0x66, 0x6f, 0x6f, 0x62, 0x61, 0x72 }; // ASCII values for 'f', 'o', 'o', 'b', 'a', 'r'
        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, input);

        long bytesSkipped = inputStream.skip(3);
        assertEquals(3, bytesSkipped);
        assertEquals(98, inputStream.read()); // ASCII value for 'b'
        assertEquals(97, inputStream.read()); // ASCII value for 'a'
        assertEquals(114, inputStream.read()); // ASCII value for 'r'
        assertEquals(-1, inputStream.read());
    }

    /**
     * Test case for skip() method with invalid parameters.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipInvalidParams() throws IOException {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, new byte[0]);

        inputStream.skip(-10); // throws IllegalArgumentException
    }

    /**
     * Test case for available() method.
     * 
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testAvailable() throws IOException {
        baseNCodec = new BaseNCodec();
        doEncode = true;

        byte[] input = { 0x66, 0x6f, 0x6f };
        BaseNCodecInputStream inputStream = createInputStream(baseNCodec, doEncode, input);

        assertEquals(3, inputStream.available());

        inputStream.read();
        assertEquals(2, inputStream.available());

        inputStream.read();
        assertEquals(1, inputStream.available());

        inputStream.read();
        assertEquals(0, inputStream.available());
    }
}