package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.read;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.read;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /** Test data for the {@link #testRead()} test method. */
    private static final byte[] TEST_DATA = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };

    /** Test data for the {@link #testDecodeBase64WithEof()} test method. */
    private static final byte[] TEST_DATA_BASE64 = new byte[] { 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00 };

    /** Test data for the {@link #testEncodeBase64WithEof()} test method. */
    private static final byte[] TEST_DATA_ENCODED_BASE64 = new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x0A };

    /** Test data for the {@link #testDecodeBaseNWithEof()} test method. */
    private static final byte[] TEST_DATA_BASE_N = new byte[] { 0x1B, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00 };

    /** Test data for the {@link #testEncodeBaseNWithEof()} test method. */
    private static final byte[] TEST_DATA_ENCODED_BASE_N = new byte[] { 0x04, 0x1C, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00 };

    /** Test data for the {@link #testMarkSupported()} test method. */
    private static final byte[] TEST_DATA_MARK_SUPPORTED = new byte[0];

    /** Test data for the {@link #testSkip()} test method. */
    private static final byte[] TEST_DATA_SKIP = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };

    /** Test data for the {@link #testAvailable()} test method. */
    private static final byte[] TEST_DATA_AVAILABLE = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };

    /**
     * Test {@link BaseNCodecInputStream#read()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testRead() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        for (int i = 0; i < TEST_DATA.length; i++) {
            assertEquals((int) TEST_DATA[i], baseNCodecInputStream.read());
        }
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} method with decoding Base64 data and encountering EOF.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBase64WithEof() throws IOException {
        final BaseNCodec baseNCodec = new Base64();
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BASE64);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[TEST_DATA_LENGTH + 1];

        int len;
        while ((len = baseNCodecInputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        assertEquals(TEST_DATA, outputStream.toByteArray());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} method with encoding Base64 data and encountering EOF.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testEncodeBase64WithEof() throws IOException {
        final BaseNCodec baseNCodec = new Base64();
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA_ENCODED_BASE64);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[TEST_DATA_LENGTH + 1];

        int len;
        while ((len = baseNCodecInputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        assertEquals(TEST_DATA, outputStream.toByteArray());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} method with decoding Base-N data and encountering EOF.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testDecodeBaseNWithEof() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec(16);
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BASE_N);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[TEST_DATA_LENGTH + 1];

        int len;
        while ((len = baseNCodecInputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        assertEquals(TEST_DATA, outputStream.toByteArray());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} method with encoding Base-N data and encountering EOF.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testEncodeBaseNWithEof() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec(16);
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA_ENCODED_BASE_N);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[TEST_DATA_LENGTH + 1];

        int len;
        while ((len = baseNCodecInputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        assertEquals(TEST_DATA, outputStream.toByteArray());
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA_MARK_SUPPORTED);
        new BaseNCodecInputStream(inputStream, new BaseNCodec(), false).markSupported();
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testSkip() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA_SKIP);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        baseNCodecInputStream.skip(5);

        int read;
        byte[] buffer = new byte[5];
        for (int i = 0; i < 5; i++) {
            read = baseNCodecInputStream.read(buffer, 0, buffer.length);
            assertEquals(5, read);
        }

        byte[] expected = new byte[] { 0x0A, 0x0B, 0x0C, 0x0D, 0x0E };
        assertArrayEquals(expected, TEST_DATA_SKIP, 5, 5);
    }

    /**
     * Test {@link BaseNCodecInputStream#available()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testAvailable() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA_AVAILABLE);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        assertEquals(TEST_DATA_LENGTH, baseNCodecInputStream.available());
    }

}