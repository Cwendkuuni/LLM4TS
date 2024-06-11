package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "Testing Base64InputStream.\nThis is a test.";

    private static final byte[] TEST_DATA_BYTES = TEST_DATA.getBytes();

    private static final String ENCODED_DATA = "VG8gZXhwYWNrZWQgdG8gcmVhbGx5IGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

    /**
     * Test reading data from a Base64InputStream that is decoding data.
     *
     * @throws IOException
     */
    @Test
    public void testDecodingData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] output = outputStream.toByteArray();
        Base64 decoder = new Base64();
        byte[] expected = decoder.decode(ENCODED_DATA);
        assertArrayEquals(expected, output);
    }

    /**
     * Test reading data from a Base64InputStream that is encoding data.
     *
     * @throws IOException
     */
    @Test
    public void testEncodingData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] output = outputStream.toByteArray();
        assertEquals(ENCODED_DATA, new String(output));
    }

    /**
     * Test reading data from a Base64InputStream that is decoding data with a custom line length and line separator.
     *
     * @throws IOException
     */
    @Test
    public void testDecodingDataWithCustomLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BYTES);
        Base64 decoder = new Base64(72, "\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 72, decoder.getLineSeparator());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] output = outputStream.toByteArray();
        assertArrayEquals(decoder.decode(TEST_DATA.getBytes()), output);
    }

    /**
     * Test reading data from a Base64InputStream that is encoding data with a custom line length and line separator.
     *
     * @throws IOException
     */
    @Test
    public void testEncodingDataWithCustomLineLengthAndLineSeparator() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write("Testing Base64InputStream.\r\nThis is a test.".getBytes());
        Base64 encoder = new Base64(64, "\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputStream.toByteArray()), true, 64, "\r\n".getBytes());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] output = outputStream.toByteArray();
        assertEquals(encoder.encode(TEST_DATA_BYTES), new String(output));
    }

    /**
     * Test reading data from a Base64InputStream that is decoding data when the input is empty.
     *
     * @throws IOException
     */
    @Test
    public void testDecodingDataWhenInputIsEmpty() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][0]));
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test reading data from a Base64InputStream that is decoding data when an error occurs within the input stream.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testDecodingDataWhenErrorOccurs() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xDE });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read();
    }
}