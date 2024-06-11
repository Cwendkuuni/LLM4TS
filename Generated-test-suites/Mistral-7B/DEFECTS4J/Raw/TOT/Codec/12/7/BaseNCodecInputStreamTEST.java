package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.apache.commons.codec.binary.BaseNCodec.ERROR_BAD_INPUT;
import static org.apache.commons.codec.binary.BaseNCodec.ERROR_ILLEGAL_ALPHABET;
import static org.apache.commons.codec.binary.BaseNCodec.ERROR_ILLEGAL_PADDING;
import static org.apache.commons.codec.binary.BaseNCodec.ERROR_TOO_LONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    /**
     * Test reading a single byte from the InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testReadByte() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x61 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        assertEquals(0x61, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test reading multiple bytes from the InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testReadBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x61, 0x62, 0x63 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        byte[] buffer = new byte[3];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(3, len);
        assertEquals(0x61, buffer[0]);
        assertEquals(0x62, buffer[1]);
        assertEquals(0x63, buffer[2]);
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test reading from a Base-N encoded InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseN() throws IOException {
        String encodedData = "235GXW6+8=";
        byte[] decodedData = new byte[6];
        ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(encodedData.getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        int len = inputStream.read(decodedData, 0, decodedData.length);
        assertEquals(len, decodedData.length);
        inputStream.close();
        assertEquals("AVc=", new String(decodedData, 0, len));
        assertEquals(decodedData, codec.decode(decodedStream.toByteArray()));
    }

    /**
     * Test encoding an InputStream into Base-N format.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testEncodeBaseN() throws IOException {
        byte[] inputData = { 0x61, 0x62, 0x63 };
        ByteArrayOutputStream encodedStream = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] buffer = new byte[3];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertEquals(235, buffer[0] & 0xFF);
        assertEquals('G', (char) (buffer[1] & 0xFF));
        assertEquals('X', (char) (buffer[2] & 0xFF));
        assertEquals(48, buffer[3] & 0xFF);
        assertEquals('W', (char) (buffer[4] & 0xFF));
        assertEquals('6', (char) (buffer[5] & 0xFF));
        assertEquals('+', (char) (buffer[6] & 0xFF));
        assertEquals('=', (char) (buffer[7] & 0xFF));
        assertEquals("235GXW6+8=", new String(buffer, 0, 8));
        assertEquals(new String(inputData), new String(codec.decode(encodedStream.toByteArray())));
    }

    /**
     * Test reading from an empty InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test reading from a Base-N encoded InputStream with invalid Base-N data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNInvalidData() throws IOException {
        String encodedData = "235GXW6++=";
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(encodedData.getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        byte[] buffer = new byte[3];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertEquals(235, buffer[0] & 0xFF);
        assertEquals('G', (char) (buffer[1] & 0xFF));
        assertEquals('X', (char) (buffer[2] & 0xFF));
        assertEquals(48, buffer[3] & 0xFF);
        assertEquals('W', (char) (buffer[4] & 0xFF));
        assertEquals('6', (char) (buffer[5] & 0xFF));
        assertEquals('+', (char) (buffer[6] & 0xFF)); // expected '+' instead of '='
        assertThrows(IllegalArgumentException.class, () -> codec.decode(buffer, 0, len));
    }

    /**
     * Test decoding an empty Base-N encoded InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNEmpty() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        assertEquals(EOF, inputStream.read());
        assertEquals(0, codec.decode(new byte[0], 0, 0).length);
    }

    /**
     * Test decoding a Base-N encoded InputStream with an error in the codec.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNError() throws IOException {
        BaseNCodec codec = new BaseNCodec(new byte[] { 'X' }, new byte[] { 'Y' });
        InputStream in = new ByteArrayInputStream(new byte[] { 0x61 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        assertEquals(EOF, inputStream.read());
        assertThrows(IllegalArgumentException.class, () -> codec.decode(new byte[0], 0, 0));
        assertEquals(ERROR_ILLEGAL_ALPHABET, codec.decodeError());
    }

    /**
     * Test encoding an InputStream with an error in the codec.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testEncodeBaseNError() throws IOException {
        BaseNCodec codec = new BaseNCodec(new byte[] { 'X' }, new byte[] { 'Y' });
        InputStream in = new ByteArrayInputStream(new byte[] { 0x61 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] buffer = new byte[3];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertEquals(ERROR_ILLEGAL_ALPHABET, codec.decodeError());
    }

    /**
     * Test reading from a Base-N encoded InputStream with an invalid padding character.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNPadding() throws IOException {
        String encodedData = "235GXW6+8z";
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(encodedData.getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        byte[] buffer = new byte[3];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertEquals(235, buffer[0] & 0xFF);
        assertEquals('G', (char) (buffer[1] & 0xFF));
        assertEquals('X', (char) (buffer[2] & 0xFF));
        assertEquals(48, buffer[3] & 0xFF);
        assertEquals('W', (char) (buffer[4] & 0xFF));
        assertEquals('6', (char) (buffer[5] & 0xFF));
        assertEquals('+', (char) (buffer[6] & 0xFF));
        assertEquals('z', (char) (buffer[7] & 0xFF));
        assertThrows(IllegalArgumentException.class, () -> codec.decode(buffer, 0, len));
        assertEquals(ERROR_ILLEGAL_PADDING, codec.decodeError());
    }

    /**
     * Test reading from a Base-N encoded InputStream with an invalid length.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNInvalidLength() throws IOException {
        String encodedData = "235GXW6+8=";
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(encodedData.getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        byte[] buffer = new byte[5];
        int len = inputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        assertEquals(235, buffer[0] & 0xFF);
        assertEquals('G', (char) (buffer[1] & 0xFF));
        assertEquals('X', (char) (buffer[2] & 0xFF));
        assertEquals('W', (char) (buffer[3] & 0xFF));
        assertEquals(48, buffer[4] & 0xFF);
        assertThrows(IllegalArgumentException.class, () -> codec.decode(buffer, 5, len));
        assertEquals(ERROR_TOO_LONG, codec.decodeError());
    }

    /**
     * Test marking and resetting a Base-N encoded InputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkAndReset() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        inputStream.mark(123);
        inputStream.reset();
    }
}