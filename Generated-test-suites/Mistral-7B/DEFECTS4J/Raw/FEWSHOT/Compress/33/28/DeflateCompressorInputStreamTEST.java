package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamTestCase;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;

/**
 * Test cases for {@link DeflateCompressorInputStream}.
 */
public class DeflateCompressorInputStreamTest extends CompressorStreamTestCase {

    /**
     * Test constructor with no parameters.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testConstructorWithNoParameters() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00 });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            byte[] buffer = new byte[10];
            int len = dis.read(buffer, 0, buffer.length);
            assertEquals(len, 10);
            assertEquals(buffer[0], 0x00);
            assertEquals(buffer[1], 0x3C);
            // ... other assertions for the rest of the bytes
        }
    }

    /**
     * Test constructor with parameters.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testConstructorWithParameters() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        DeflateParameters parameters = new DeflateParameters();
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is, parameters)) {
            byte[] buffer = new byte[10];
            int len = dis.read(buffer, 0, buffer.length);
            assertEquals(len, 10);
            // ... other assertions for the rest of the bytes
        }
    }

    /**
     * Test {@link #read()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadMethod() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00 });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            byte[] buffer = new byte[5];
            int len = dis.read(buffer, 0, buffer.length);
            assertEquals(len, 5);
            assertEquals(buffer[0], 0x00);
            assertEquals(buffer[1], 0x3C);
            assertEquals(buffer[2], 0x06);
            assertEquals(buffer[3], 0x01);
            assertEquals(buffer[4], 0x02);
            // ... other assertions for the rest of the bytes
        }
    }

    /**
     * Test {@link #read(byte[], int, int)} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadMethodWithOffsetAndLength() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            byte[] buffer = new byte[10];
            int len = dis.read(buffer, 5, 5);
            assertEquals(len, 5);
            assertEquals(buffer[5], 0x06);
            assertEquals(buffer[6], 0x01);
            assertEquals(buffer[7], 0x02);
            assertEquals(buffer[8], 0x03);
            assertEquals(buffer[9], 0x04);
            // ... other assertions for the rest of the bytes
        }
    }

    /**
     * Test {@link #skip(long)} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSkipMethod() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            long skipped = dis.skip(5L);
            assertEquals(skipped, 5);
            // ... other assertions
        }
    }

    /**
     * Test {@link #available()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testAvailableMethod() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            int available = dis.available();
            assertEquals(available, 10);
            // ... other assertions
        }
    }

    /**
     * Test {@link #close()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCloseMethod() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            dis.close();
            // ... other assertions
        }
    }

    /**
     * Test signature checking.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSignatureChecking() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xED });
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            byte[] signature = new byte[2];
            int len = dis.read(signature, 0, signature.length);
            assertEquals(len, 2);
            assertTrue(dis.isSignatureValid(signature));
        }
    }

    /**
     * Test signature checking with invalid signature.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSignatureCheckingWithInvalidSignature() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 0x78, 0xDA, 0x1A, 0x1B, 0x08, 0x00, 0x00, 0x00, 0xFF, 0xFD }); // invalid last byte
        try (DeflateCompressorInputStream dis = new DeflateCompressorInputStream(is)) {
            byte[] signature = new byte[2];
            int len = dis.read(signature, 0, signature.length);
            assertEquals(len, 2);
            assertFalse(dis.isSignatureValid(signature));
        }
    }
}