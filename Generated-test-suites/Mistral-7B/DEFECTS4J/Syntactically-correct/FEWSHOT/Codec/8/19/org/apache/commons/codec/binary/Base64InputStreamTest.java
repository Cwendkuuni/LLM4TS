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

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testDefaultConstruction() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testEncodeConstruction() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), true);
        assertEquals("VGpzYw==", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testEncodeConstructionWithLineLength() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), true, 48, null);
        assertEquals("VGpz", bis.readLine());
        assertEquals("Yw==", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testEncodeConstructionWithLineSeparator() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), true, 48, "\r\n".getBytes());
        assertEquals("VGpz\r\n", bis.readLine());
        assertEquals("Yw==\r\n", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testDecodeConstruction() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpzYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false);
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testDecodeConstructionWithLineLength() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpz\r\nYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false, 48, null);
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testDecodeConstructionWithLineSeparator() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpz\r\n".getBytes());
        baos.write("Yw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false, 48, "\r\n".getBytes());
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testHasData() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertTrue(bis.hasData());
        assertEquals(1, bis.read());
        assertFalse(bis.hasData());
    }

    @Test
    public void testHasDataWhenEnded() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
        assertFalse(bis.hasData());
    }

    @Test
    public void testHasDataWhenNotEnded() {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2 }));
        assertTrue(bis.hasData());
        assertEquals(1, bis.read());
        assertTrue(bis.hasData());
        assertEquals(2, bis.read());
        assertFalse(bis.hasData());
    }

    @Test(expected = IOException.class)
    public void testReadWhenEnded() throws IOException {
        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertEquals(1, bis.read());
        assertEquals(2, bis.read());
        assertEquals(3, bis.read());
        assertEquals(-1, bis.read());
        bis.read(); // should throw IOException
    }

    @Test(expected = IOException.class)
    public void testReadWhenNotEncoded() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpzYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false);
        bis.read(); // should throw IOException
    }

    @Test(expected = IOException.class)
    public void testReadWhenNotDecoded() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("1\r\n2\r\n3\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, true);
        bis.read(); // should throw IOException
    }

    @Test
    public void testReadLineWhenEncoded() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpzYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, true);
        assertEquals("VGpzYw==", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testReadLineWhenEncodedWithLineLength() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpzYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, true, 48, null);
        assertEquals("VGpz", bis.readLine());
        assertEquals("Yw==", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testReadLineWhenEncodedWithLineSeparator() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("VGpzYw==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, true, 48, "\r\n".getBytes());
        assertEquals("VGpz\r\n", bis.readLine());
        assertEquals("Yw==\r\n", bis.readLine());
        assertEquals(-1, bis.read());
    }

    @Test
    public void testReadLineWhenDecoded() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("SGVsbG8sIFdvcmxkIQ==\r\nSGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false);
        assertArrayEquals(new byte[] { 32, 101, 115, 116, 32, 105, 110, 32, 116, 111, 117, 108, 101, 32, 103, 111, 111 }, bis.readAllBytes());
    }

    @Test
    public void testReadLineWhenDecodedWithLineLength() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("SGVsbG8sIFdvcmxkIQ==\r\nSGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false, 48, null);
        assertArrayEquals(new byte[] { 32, 101, 115 }, bis.readLineAsBytes());
        assertArrayEquals(new byte[] { 116, 32, 105, 110, 32, 116, 111, 117, 108, 101 }, bis.readLineAsBytes());
        assertNull(bis.readLineAsBytes());
    }

    @Test
    public void testReadLineWhenDecodedWithLineSeparator() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("SGVsbG8sIFdvcmxkIQ==\r\nSGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false, 48, "\r\n".getBytes());
        assertEquals("SGVsbG8sIFdvcmxkIQ==\r\n", bis.readLine());
        assertEquals("SGVsbG8sIFdvcmxkIQ==\r\n", bis.readLine());
        assertNull(bis.readLine());
    }

    @Test(expected = IOException.class)
    public void testReadLineWhenEnded() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("SGVsbG8sIFdvcmxkIQ==\r\nSGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Base64InputStream bis = new Base64InputStream(is, false);
        assertEquals("SGVsbG8sIFdvcmxkIQ==\r\n", bis.readLine());
        assertEquals("SGVsbG8sIFdvcmxkIQ==\r\n", bis.readLine());
        assertNull(bis.readLine());
        bis.readLine(); // should throw IOException
    }

    @Test
    public void testReadLineWhenEmptyInput() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Base64InputStream bis = new Base64InputStream(is, false);
        assertNull(bis.readLine());
    }
}