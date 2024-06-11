package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding.
     *
     * @throws IOException
     */
    @Test
    public void testConstructorDefaultDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64InputStream = new Base64InputStream(input);

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test constructor with encoding.
     *
     * @throws IOException
     */
    @Test
    public void testConstructorWithEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64 base64 = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output);

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true);

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test constructor with encoding, custom line length, and line separator.
     *
     * @throws IOException
     */
    @Test
    public void testConstructorWithEncodingCustomLineLengthAndLineSeparator() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64 base64 = new Base64(72, new byte[] { 0x0D, 0x0A });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, 72, new byte[] { 0x0D, 0x0A });

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true, 72, new byte[] { 0x0D, 0x0A });

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test read method with default decoding and EOF.
     *
     * @throws IOException
     */
    @Test
    public void testReadDefaultDecodingWithEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64InputStream = new Base64InputStream(input);

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test read method with encoding and EOF.
     *
     * @throws IOException
     */
    @Test
    public void testReadWithEncodingAndEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64 base64 = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output);

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true);

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test read method with encoding, custom line length, and line separator, and EOF.
     *
     * @throws IOException
     */
    @Test
    public void testReadWithEncodingCustomLineLengthAndLineSeparatorAndEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64 base64 = new Base64(72, new byte[] { 0x0D, 0x0A });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, 72, new byte[] { 0x0D, 0x0A });

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true, 72, new byte[] { 0x0D, 0x0A });

        assertEquals(0x41, base64InputStream.read());
        assertEquals(0x42, base64InputStream.read());
        assertEquals(0x43, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test read method with invalid input.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadWithInvalidInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read();
    }

    /**
     * Test read method with invalid input and encoding.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadWithInvalidInputEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        Base64 base64 = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output);

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true);
        base64InputStream.read();
    }

    /**
     * Test read method with invalid input, encoding, custom line length, and line separator.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadWithInvalidInputEncodingCustomLineLengthAndLineSeparator() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        Base64 base64 = new Base64(72, new byte[] { 0x0D, 0x0A });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, 72, new byte[] { 0x0D, 0x0A });

        int read = -1;
        while ((read = input.read()) != -1) {
            base64OutputStream.write(read);
        }
        base64OutputStream.finish();
        byte[] encodedData = output.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true, 72, new byte[] { 0x0D, 0x0A });
        base64InputStream.read();
    }
}