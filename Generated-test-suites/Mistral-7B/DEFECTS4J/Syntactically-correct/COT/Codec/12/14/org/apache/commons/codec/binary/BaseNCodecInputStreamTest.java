package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * JUnit test class for {@link BaseNCodecInputStream}
 *
 * @author <a href="mailto:jianjun.li@qosina.com">Jianjun Li</a>
 * @version 1.0
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor with valid parameters.
     */
    @org.junit.Test
    public void testConstructorValid() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    /**
     * Test constructor with null inputStream.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructorNullInputStream() {
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        new BaseNCodecInputStream(null, baseNCodec, false);
    }

    /**
     * Test constructor with null baseNCodec.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructorNullBaseNCodec() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B' });
        new BaseNCodecInputStream(inputStream, null, false);
    }

    /**
     * Test constructor with doEncode as true.
     */
    @org.junit.Test
    public void testConstructorEncodeTrue() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    /**
     * Test read method.
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(97, result); // 'a' value in ASCII table
    }

    /**
     * Test read method with EOF.
     */
    @org.junit.Test
    public void testReadEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    /**
     * Test read method with zero-length input stream.
     */
    @org.junit.Test
    public void testReadZeroLengthInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    /**
     * Test read method with null byte array.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int[] results = new int[1];
        baseNCodecInputStream.read(results);
    }

    /**
     * Test read method with invalid offset.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] b = new byte[1];
        int offset = -1;
        int len = 1;
        baseNCodecInputStream.read(b, offset, len);
    }

    /**
     * Test read method with invalid length.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] b = new byte[1];
        int offset = 0;
        int len = -1;
        baseNCodecInputStream.read(b, offset, len);
    }

    /**
     * Test read method with buffer size larger than the remaining bytes.
     */
    @org.junit.Test
    public void testReadBufferSizeLargerThanRemainingBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] b = new byte[10];
        int offset = 0;
        int len = 1;
        int readLen = baseNCodecInputStream.read(b, offset, len);
        assertEquals(1, readLen);
    }

    /**
     * Test read method with valid input.
     */
    @org.junit.Test
    public void testReadValidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] b = new byte[2];
        int offset = 0;
        int len = 2;
        int readLen = baseNCodecInputStream.read(b, offset, len);

        assertEquals(2, readLen);
        assertEquals('A', b[0]);
        assertEquals('B', b[1]);
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test close method.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.close();
        assertNull(baseNCodecInputStream.getInputStream());
    }

    /**
     * Test read method with non-base decoding.
     */
    @org.junit.Test
    public void testReadNonBaseDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] b = new byte[1];
        int offset = 0;
        int len = 1;
        int readLen = baseNCodecInputStream.read(b, offset, len);

        assertEquals(1, readLen);
        assertEquals(0x80, b[0]);
    }

    /**
     * Test read method with base encoding.
     */
    @org.junit.Test
    public void testReadBaseEncoding() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), baseNCodec, true);

        byte[] result = new byte[10];
        int offset = 0;
        int len = 10;
        int readLen = baseNCodecInputStream.read(result, offset, len);

        assertEquals(3, readLen);
        assertEquals("S", new String(result, 0, readLen));
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read method with base encoding larger than available bytes.
     */
    @org.junit.Test
    public void testReadBaseEncodingLargerThanAvailableBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1 }), baseNCodec, true);

        byte[] result = new byte[10];
        int offset = 0;
        int len = 10;
        int readLen = baseNCodecInputStream.read(result, offset, len);

        assertEquals(1, readLen);
        assertEquals("A", new String(result, 0, readLen));
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read method with base encoding smaller than available bytes.
     */
    @org.junit.Test
    public void testReadBaseEncodingSmallerThanAvailableBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), baseNCodec, true);

        byte[] result = new byte[3];
        int offset = 0;
        int len = 3;
        int readLen = baseNCodecInputStream.read(result, offset, len);

        assertEquals(3, readLen);
        assertEquals("S", new String(result, 0, readLen));
    }

    /**
     * Test read method with base encoding and invalid buffer size.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadBaseEncodingInvalidBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), baseNCodec, true);

        byte[] result = new byte[2];
        int offset = 0;
        int len = 2;
        int readLen = baseNCodecInputStream.read(result, offset, len);
    }

    /**
     * Test read method with base encoding and invalid input stream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBaseEncodingInvalidInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(null, baseNCodec, true);

        byte[] result = new byte[1];
        int offset = 0;
        int len = 1;
        int readLen = baseNCodecInputStream.read(result, offset, len);
    }

    /**
     * Test read method with base encoding and null baseNCodec.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReadBaseEncodingNullBaseNCodec() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), null, true);

        byte[] result = new byte[1];
        int offset = 0;
        int len = 1;
        int readLen = baseNCodecInputStream.read(result, offset, len);
    }

    /**
     * Test read method with base encoding and null result byte array.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReadBaseEncodingNullResultByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), baseNCodec, true);

        int[] results = new int[1];
        int offset = 0;
        int len = 1;
        int readLen = baseNCodecInputStream.read(results);
    }
}