package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test for {@link Base64InputStream}.
 *
 * @author John Reese
 */
public class Base64InputStreamTest {

    /** The input byte array. */
    private static final byte[] INPUT = { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88 };

    /** The expected output byte array. */
    private static final byte[] OUTPUT = { 0x61, 0x62, 0x63, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38 };

    /** The expected output base64 string. */
    private static final String OUTPUT_BASE64 = "AQIDBA==";

    /** The expected input base64 string. */
    private static final String INPUT_BASE64 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gdGhpbmdzIHVwIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYWxzIG11dGBmaWNlcyBmZWF0b3JuYWwxLCAkIEluYyB4CiAgICAtLVBhdGggdG8gYWxzIHdpdGggZmlsbD4KCiAgICAtLVBhdGggdG8gYWxzIHNlbGVzIG9uIHRoZSBzZXBhcmF0b3IgYW55IGNvbnRlbnQudmFsdWUiCiAg";

    /** The test base64. */
    private Base64 base64;

    /**
     * Initialize the test.
     */
    @Before
    public void setUp() {
        base64 = new Base64();
    }

    /**
     * Test the constructor {@link Base64InputStream#Base64InputStream(InputStream)} with decode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test reading the bytes.
        byte[] output = new byte[OUTPUT.length];
        int read = base64InputStream.read(output, 0, OUTPUT.length);
        assertEquals(OUTPUT.length, read);
        assertSame(OUTPUT, output);

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the constructor {@link Base64InputStream#Base64InputStream(InputStream)} with encode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[INPUT.length];
        int read;

        // Read the bytes.
        do {
            read = base64InputStream.read(buffer, 0, buffer.length);
            if (read > 0) {
                outputStream.write(buffer, 0, read);
            }
        } while (read > 0);

        // Test the output base64 string.
        byte[] output = outputStream.toByteArray();
        assertEquals(OUTPUT_BASE64.length(), output.length);
        assertSame(INPUT_BASE64, new String(output, "US-ASCII"));

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the constructor {@link Base64InputStream#Base64InputStream(InputStream, boolean)} with decode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamBooleanDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Test reading the bytes.
        byte[] output = new byte[OUTPUT.length];
        int read = base64InputStream.read(output, 0, OUTPUT.length);
        assertEquals(OUTPUT.length, read);
        assertSame(OUTPUT, output);

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the constructor {@link Base64InputStream#Base64InputStream(InputStream, boolean)} with encode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamBooleanEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[INPUT.length];
        int read;

        // Read the bytes.
        do {
            read = base64InputStream.read(buffer, 0, buffer.length);
            if (read > 0) {
                outputStream.write(buffer, 0, read);
            }
        } while (read > 0);

        // Test the output base64 string.
        byte[] output = outputStream.toByteArray();
        assertEquals(OUTPUT_BASE64.length(), output.length);
        assertSame(INPUT_BASE64, new String(output, "US-ASCII"));

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the method {@link Base64InputStream#read()} with decode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testReadDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte singleByte = new byte[1];

        // Test the first byte.
        int read = base64InputStream.read();
        assertEquals(INPUT[0], read);
        assertEquals(INPUT[0], singleByte[0]);
        assertSame(INPUT[0], singleByte);

        // Test EOF.
        read = base64InputStream.read();
        assertEquals(-1, read);
        assertEquals(-1, singleByte[0]);
        assertNull(singleByte);

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the method {@link Base64InputStream#read(byte[], int, int)} with decode.
     *
     * @throws IOException
     *             the exception
     */
    @Test
    public void testReadDecodeByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[OUTPUT.length];
        int read = base64InputStream.read(output, 0, OUTPUT.length);
        assertEquals(OUTPUT.length, read);
        assertSame(OUTPUT, output);

        // Test closing.
        base64InputStream.close();
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test the method {@link Base64InputStream#markSupported()} with decode.
     *
     * @throws IOException
     *             the exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupportedDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.mark(0);
    }

}