package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    /**
     * Test Base64InputStream decoding.
     */
    @Test
    public void testDecode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());

        byte[] buffer = new byte[10];
        int len;
        while ((len = new Base64InputStream(input, false).read(buffer, 0, buffer.length)) > 0) {
            // no need to check for EOF, it will be detected when read returns -1
            assertArrayEquals(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 }, buffer, 0, len);
        }
    }

    /**
     * Test Base64InputStream encoding.
     */
    @Test
    public void testEncode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] input = "hello world".getBytes();

        new Base64OutputStream(output, true).write(input, 0, input.length);
        new Base64OutputStream(output, true).flush();

        byte[] expected = "SGVsbG8sIFdvcmxkIQ==".getBytes();
        byte[] result = new Base64InputStream(new ByteArrayInputStream(output.toByteArray()), true).readAllBytes();

        assertArrayEquals(expected, result);
    }

    /**
     * Test Base64InputStream with custom line length and line separator.
     */
    @Test
    public void testCustomLineLengthAndSeparator() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] input = "hello world".getBytes();

        new Base64OutputStream(output, 5, new byte[] { 0x0D, 0x0A }).write(input, 0, input.length);
        new Base64OutputStream(output, 5, new byte[] { 0x0D, 0x0A }).flush();

        byte[] result = new Base64InputStream(new ByteArrayInputStream(output.toByteArray()), true, 5, new byte[] { 0x0D, 0x0A }).readAllBytes();

        byte[] expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgPGh0bWw+CiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes();
        assertArrayEquals(expected, result);
    }

    /**
     * Test Base64InputStream with empty input.
     */
    @Test
    public void testEmptyInput() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream test = new Base64InputStream(input, false);
        assertFalse(test.hasNext());
        assertNull(test.read());
    }

    /**
     * Test Base64InputStream with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new Base64InputStream(null, false);
    }
}