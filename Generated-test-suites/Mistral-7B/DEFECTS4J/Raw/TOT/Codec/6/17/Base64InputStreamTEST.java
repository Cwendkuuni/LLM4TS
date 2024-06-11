package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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

    private static final byte[] TEST_DATA = "This is a test string for Base64 encoding and decoding.".getBytes();
    private static final String TEST_DATA_BASE64 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5Lg==";

    /**
     * Test decoding a Base64 stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeBase64Stream() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA_BASE64.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] result = new byte[TEST_DATA.length];
        int len;
        while ((len = base64is.read(result, 0, result.length)) != -1) {
            out.write(result, 0, len);
        }
        assertEquals(TEST_DATA, out.toByteArray());
    }

    /**
     * Test encoding a Base64 stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncodeBase64Stream() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Base64InputStream base64is = new Base64InputStream(in, true);
        Base64OutputStream base64os = new Base64OutputStream(out);
        int len;
        while ((len = base64is.read(singleByte, 0, 1)) != -1) {
            base64os.write(singleByte, 0, len);
        }
        base64os.close();
        base64is.close();
        assertEquals(TEST_DATA_BASE64, out.toString());
    }

    /**
     * Test decoding an empty Base64 stream.
     */
    @Test
    public void testDecodeEmptyBase64Stream() {
        InputStream in = new ByteArrayInputStream(new byte[0][0]);
        Base64InputStream base64is = new Base64InputStream(in);
        assertNull(base64is.read(singleByte, 0, 1));
        base64is.close();
    }

    /**
     * Test decoding a Base64 stream with invalid data.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeInvalidBase64Stream() throws IOException {
        String invalidBase64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5Lg==X";
        InputStream in = new ByteArrayInputStream(invalidBase64Data.getBytes());
        assertThrows(IOException.class, () -> new Base64InputStream(in));
    }

    /**
     * Test decoding a Base64 stream with a line length of 0 and CRLF line separator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeBase64StreamWithLineLength0() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA_BASE64.getBytes());
        Base64InputStream base64is = new Base64InputStream(in, 0, new byte[] { '\r', '\n' });
        byte[] result = new byte[TEST_DATA.length];
        int len;
        while ((len = base64is.read(result, 0, result.length)) != -1) {
            // do nothing
        }
        assertEquals(TEST_DATA, result);
        base64is.close();
    }

    /**
     * Test encoding a Base64 stream with a line length of 0 and no line separator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncodeBase64StreamWithLineLength0() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Base64InputStream base64is = new Base64InputStream(in, true, 0, null);
        Base64OutputStream base64os = new Base64OutputStream(out);
        int len;
        while ((len = base64is.read(singleByte, 0, 1)) != -1) {
            base64os.write(singleByte, 0, len);
        }
        base64os.close();
        base64is.close();
        assertFalse(out.toString().contains("\r\n"));
    }
}