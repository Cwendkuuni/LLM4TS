package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor with valid arguments
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testValidConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 }); // "AB" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertNotNull(baseNCodecInputStream);
    }

    /**
     * Test constructor with null BaseNCodec
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullBaseNCodec() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 }); // "AB" in Base64
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);
    }

    /**
     * Test constructor with null InputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(null, baseNCodec, false);
    }

    /**
     * Test read() method with valid arguments
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadValidArguments() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(259, baseNCodecInputStream.read()); // 'A' is 65, 'B' is 66, BaseNCodec converts 65 to 255 + 65 = 259
        assertEquals(257, baseNCodecInputStream.read()); // 'B' is 66, BaseNCodec converts 66 to 255 + 66 = 257
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read() method with empty input
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read() method with EOF
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        baseNCodecInputStream.read(result, 0, 3);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read(byte[], int, int) method with valid arguments
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadValidArgumentsArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 }); // "ABCD" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 0, 3);
        assertEquals(3, readLen);
        assertEquals('C', result[0]);
        assertEquals('D', result[1]);
        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 2));
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test read(byte[], int, int) method with empty input
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadEmptyInputArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 0, 3);
        assertEquals(0, readLen);
    }

    /**
     * Test read(byte[], int, int) method with negative length
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 3, 3);
    }

    /**
     * Test read(byte[], int, int) method with null byte array
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.read(null, 0, 3);
    }

    /**
     * Test read(byte[], int, int) method with offset out of bounds
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadOffsetOutOfBounds() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 3, 3);
    }

    /**
     * Test markSupported() method
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip() method
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 }); // "ABCD" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.skip(5);
        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 0, 3);
        assertEquals(EOF, readLen);
    }

    /**
     * Test skip() method with negative length
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 }); // "ABCD" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.skip(-1);
    }

    /**
     * Test encode and decode BaseN codecs
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        byte[] sourceData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] encodedData = new byte[inputStream.available()];
        int readLen = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        outputStream.write(encodedData, 0, readLen);

        byte[] decodedData = new byte[outputStream.size()];
        outputStream.reset();
        outputStream.read(decodedData, 0, decodedData.length);

        assertEquals(sourceData, decodedData);
    }

    /**
     * Test encode and decode BaseN codecs with different alphabets
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeDecodeDifferentAlphabets() throws IOException {
        byte[] sourceData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        BaseNCodec baseNCodec = new BaseNCodec("US-ASCII", BaseNCodec.ALPHABET_LOWER | BaseNCodec.ALPHABET_UPPER | BaseNCodec.ALPHABET_DIGITS | BaseNCodec.ALPHABET_HEX_LOWER);
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] encodedData = new byte[inputStream.available()];
        int readLen = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        outputStream.write(encodedData, 0, readLen);

        byte[] decodedData = new byte[outputStream.size()];
        outputStream.reset();
        outputStream.read(decodedData, 0, decodedData.length);

        assertEquals(sourceData, decodedData);

        String encodedBase64 = new String(Base64.encodeBase64(encodedData), Base64.STANDARD);
        String decodedBase64 = new String(Base64.decodeBase64(encodedBase64), Base64.STANDARD);
        assertEquals(new String(sourceData), decodedBase64);
    }

    /**
     * Test reading past EOF
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testReadPastEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int readLen = baseNCodecInputStream.read(result, 0, 3);
        assertEquals(3, readLen);

        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1));
        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1));
        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1));

        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1)); // this should not throw an exception
        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1)); // this should not throw an exception
        assertEquals(EOF, baseNCodecInputStream.read(result, 0, 1)); // this should not throw an exception
    }

    /**
     * Test incorrect BaseN decode
     *
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIncorrectBaseNDecode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] incorrectData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x01 }; // "ABCDEF\0" in Base64
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(incorrectData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[incorrectData.length];
        int readLen = baseNCodecInputStream.read(result, 0, result.length);
    }

}