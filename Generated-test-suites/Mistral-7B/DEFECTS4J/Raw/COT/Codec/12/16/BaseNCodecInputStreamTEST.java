package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /** Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}  */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstruction() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x2B, 0x41 }); // Base-N encoded "AB"
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(inputStream.doEncode, false);
        assertEquals(inputStream.baseNCodec, baseNCodec);
    }

    /** Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with encoding flag true  */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstructionEncodeMode() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x2B, 0x41 }); // Base-N encoded "AB"
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(inputStream.doEncode, true);
        assertEquals(inputStream.baseNCodec, baseNCodec);
    }

    /** Test method for {@link BaseNCodecInputStream#read()}  */
    @org.junit.Test
    public void testReadMethod() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0x42 }; // Base-N encoded "ABCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final int readByte;
        final int expectedByte;

        // read single byte
        readByte = inputStream.read();
        expectedByte = 0x41; // 'A'
        assertEquals(expectedByte, readByte);

        // read remaining bytes
        readByte = inputStream.read();
        expectedByte = 0x42; // 'B'
        assertEquals(expectedByte, readByte);

        readByte = inputStream.read();
        assertEquals(EOF, readByte);

        // test that all bytes have been read
        assertEquals(-1, inputStream.read());
    }

    /** Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}  */
    @org.junit.Test
    public void testReadMethodArray() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0x42, 0x2B, 0x43, 0x2B, 0x44 }; // Base-N encoded "ABCDABCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final byte[] expectedData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x41, 0x42, 0x43, 0x44 };

        // read all data into an array
        final int length = inputStream.read(expectedData, 0, expectedData.length);
        assertEquals(expectedData.length, length);
        assertArrayEquals(expectedData, expectedData);

        // test that all bytes have been read
        assertEquals(-1, inputStream.read());
    }

    /** Test method for {@link BaseNCodecInputStream#markSupported()}  */
    @org.junit.Test
    public void testMarkSupportedMethod() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x2B, 0x41 }); // Base-N encoded "AB"
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(inputStream.markSupported());
    }

    /** Test method for {@link BaseNCodecInputStream#skip(long)}  */
    @org.junit.Test
    public void testSkipMethod() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0x42, 0x2B, 0x43, 0x2B, 0x44 }; // Base-N encoded "ABCDABCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        // test skipping 0 bytes
        inputStream.skip(0);

        // test skipping 1 byte
        inputStream.skip(1);
        // test that the first byte has been read
        assertEquals(0x41, inputStream.read());

        // test skipping multiple bytes
        inputStream.skip(3);
        // test that the fourth byte has been read
        assertEquals(0x43, inputStream.read());

        // test skipping beyond available bytes
        inputStream.skip(2);
        assertEquals(EOF, inputStream.read());
    }

    /** Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} with decoding error in input stream  */
    @org.junit.Test
    public void testReadMethodDecodingError() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0x42, 0x2B, 0x43, 0x2B, 0xFF, 0x2B, 0x44 }; // Base-N encoded "ABCDFFCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final byte[] expectedData = new byte[] { 0x41, 0x42, 0x43, 0x44 };

        // test reading valid data
        final int length = inputStream.read(expectedData, 0, expectedData.length);
        assertEquals(expectedData.length, length);
        assertArrayEquals(expectedData, expectedData);

        // test reading invalid data
        byte[] readData = new byte[expectedData.length];
        length = inputStream.read(readData, 0, expectedData.length);
        assertEquals(-1, length);
        assertArrayEquals(new byte[] { EOF }, readData);
    }

    /** Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} with encoding error in input stream  */
    @org.junit.Test
    public void testReadMethodEncodingError() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0xFF, 0x2B, 0x43, 0x2B, 0x44 }; // Base-N encoded "AFFCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        final byte[] expectedData = new byte[] { 0 };

        // test reading valid data
        final int length = inputStream.read(expectedData, 0, expectedData.length);
        assertEquals(0, length);

        // test reading invalid data
        final byte[] readData = new byte[expectedData.length];
        length = inputStream.read(readData, 0, expectedData.length);
        assertEquals(-1, length);
        assertArrayEquals(new byte[] { EOF }, readData);
    }

    /** Test method for {@link BaseNCodecInputStream#read(byte[], int, int)} with EOF in input stream  */
    @org.junit.Test
    public void testReadMethodEndOfFile() throws IOException {
        final byte[] encodedData = new byte[] { 0x2B, 0x41, 0x2B, 0x42, 0x2B, 0x43, 0x2B, 0x44 }; // Base-N encoded "ABCDABCD"
        final InputStream input = new ByteArrayInputStream(encodedData);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        final byte[] expectedData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x41, 0x42, 0x43, 0x44 };

        // test reading valid data
        final int length = inputStream.read(expectedData, 0, expectedData.length * 2);
        assertEquals(expectedData.length * 2, length);
        assertArrayEquals(expectedData, expectedData);

        // test reaching EOF
        length = inputStream.read(expectedData, 0, expectedData.length);
        assertEquals(-1, length);
    }

    // Additional test methods for different edge cases and exception handling scenarios can be added here if needed

    /**
     * Helper method to assert that two byte arrays are equal.
     *
     * @param expected
     *            expected byte array
     * @param actual
     *            actual byte array
     */
    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }
}