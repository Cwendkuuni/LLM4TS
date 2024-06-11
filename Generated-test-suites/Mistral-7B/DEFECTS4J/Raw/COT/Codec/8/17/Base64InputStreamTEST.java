package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
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
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64Stream = new Base64InputStream(input);
        byte[] result = new byte[3];
        assertEquals(3, base64Stream.read(result));
        assertEquals(new String(Base64.decodeBase64("VGpX")), new String(new String(result).trim()));
        base64Stream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64Stream = new Base64InputStream(input, true);
        byte[] result = new byte[4];
        assertEquals(3, base64Stream.read(result, 0, 3));
        assertEquals(new String(Base64.encodeBase64(new byte[] { 0x41, 0x42, 0x43 })), new String(new String(result).trim()));
        base64Stream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamCustom() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64Stream = new Base64InputStream(input, true, 5, new byte[] { 0x0D, 0x0A });
        byte[] result = new byte[12];
        assertEquals(3, base64Stream.read(result, 0, 3));
        assertEquals(new String(Base64.encodeBase64(new byte[] { 0x41, 0x42, 0x43 }, 5, new byte[] { 0x0D, 0x0A })),
                new String(new String(result).trim()));
        base64Stream.close();
    }

    /**
     * Test method read()
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64Stream = new Base64InputStream(input);
        byte[] result = new byte[3];
        assertEquals(3, base64Stream.read(result));
        assertEquals(new String(Base64.decodeBase64("VGpX")), new String(new String(result).trim()));
        base64Stream.close();
    }

    /**
     * Test method read(byte[] b, int off, int len)
     *
     * @throws IOException
     */
    @Test
    public void testReadBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64Stream = new Base64InputStream(input);
        byte[] result = new byte[4];
        assertEquals(4, base64Stream.read(result, 0, 4));
        assertEquals(new String(Base64.decodeBase64("VGpXVGpX")), new String(new String(result).trim()));
        base64Stream.close();
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        Base64InputStream base64Stream = new Base64InputStream(input);
        assertThrows(UnsupportedOperationException.class, base64Stream::markSupported);
        base64Stream.close();
    }
}