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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link Base64InputStream} class.
 *
 * @author John Reese
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64 base64;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream("VG8gZXhpZG5faGFzZXM=".getBytes());
        base64 = new Base64();
        base64InputStream = new Base64InputStream(inputStream, false);
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
        if (base64InputStream != null) {
            base64InputStream.close();
        }
    }

    /**
     * Test the constructor that decodes the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecode() throws IOException {
        byte[] decoded = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(decoded, 0, decoded.length);
        assertEquals(decoded.length, bytesRead);
        assertArrayEquals(new byte[] { 'V', 'G', ' ', 'G', 'z', 'H', 'p', 'Z', 'G', '5', 'f', 'a', 'G', 'z', 'Z', 'X', 'M', '=' }, decoded);
    }

    /**
     * Test the constructor that decodes the input stream with custom line length and line separator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testDecodeWithCustomLineLengthAndLineSeparator() throws IOException {
        byte[] lineSeparators = new byte[] { 13, 10 };
        byte[] decoded = new byte[inputStream.available()];
        int bytesRead = base64InputStream.newInstance(inputStream, false, 76, lineSeparators).read(decoded, 0, decoded.length);
        assertEquals(decoded.length, bytesRead);
        assertArrayEquals(new byte[] { 'V', 'G', ' ', 'G', 'z', 'H', 'p', 'Z', 'G', '5', 'f', 'a', 'G', 'z', 'Z', 'X', 'M', '=' }, decoded);
        assertEquals(lineSeparators[0], base64InputStream.getLineSeparator()[0]);
        assertEquals(lineSeparators[1], base64InputStream.getLineSeparator()[1]);
    }

    /**
     * Test the constructor that encodes the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64 = new Base64(64, null);
        base64InputStream = new Base64InputStream(inputStream, true);
        byte[] encoded = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(encoded, 0, encoded.length);
        assertEquals(encoded.length, bytesRead);
        byte[] expected = base64.encode(new byte[] { 'V', 'G' });
        assertArrayEquals(expected, encoded);
        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    /**
     * Test the constructor that encodes the input stream with custom line length and line separator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testEncodeWithCustomLineLengthAndLineSeparator() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64 = new Base64(76, new byte[] { 13, 10 });
        base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[] { 13, 10 });
        byte[] encoded = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(encoded, 0, encoded.length);
        assertEquals(encoded.length, bytesRead);
        byte[] expected = base64.encode(new byte[] { 'V', 'G' });
        assertArrayEquals(expected, encoded);
        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
        assertEquals(base64.getLineLength(), base64InputStream.getLineLength());
        assertEquals(base64.getLineSeparator()[0], base64InputStream.getLineSeparator()[0]);
        assertEquals(base64.getLineSeparator()[1], base64InputStream.getLineSeparator()[1]);
    }

    /**
     * Test the constructor that does not decode or encode.
     */
    @Test
    public void testIdentity() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VG8gZXhpZG5faGFzZXM=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        assertArrayEquals("VG8gZXhpZG5faGFzZXM=".getBytes(), outputStream.toByteArray());
    }

    /**
     * Test that an IOException is thrown when markSupported() is called.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        base64InputStream.markSupported();
    }

    /**
     * Test that the line length is set correctly in the constructor.
     */
    @Test
    public void testLineLength() throws IOException {
        int lineLength = 48;
        inputStream = new ByteArrayInputStream("VG8gZXhpZG5faGFzZXM=".getBytes());
        base64InputStream = new Base64InputStream(inputStream, false, lineLength, null);
        assertEquals(lineLength, base64InputStream.getLineLength());
    }

    /**
     * Test that the line separator is set correctly in the constructor.
     */
    @Test
    public void testLineSeparator() throws IOException {
        byte[] lineSeparators = new byte[] { 13, 10 };
        inputStream = new ByteArrayInputStream("VG8gZXhpZG5faGFzZXM=".getBytes());
        base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparators);
        assertEquals(lineSeparators[0], base64InputStream.getLineSeparator()[0]);
        assertEquals(lineSeparators[1], base64InputStream.getLineSeparator()[1]);
    }

    /**
     * Test that the doEncode property is set correctly in the constructor.
     */
    @Test
    public void testDoEncode() throws IOException {
        boolean doEncode = true;
        inputStream = new ByteArrayInputStream("VG8gZXhpZG5faGFzZXM=".getBytes());
        base64InputStream = new Base64InputStream(inputStream, doEncode);
        assertEquals(doEncode, base64InputStream.isDecode());
    }
}