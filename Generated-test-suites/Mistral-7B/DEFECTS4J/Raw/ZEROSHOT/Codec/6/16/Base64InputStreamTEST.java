package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    /**
     * Tests the default constructor
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}));
        assertNotNull(base64is);
        assertEquals(Base64InputStream.class, base64is.getClass());
        assertFalse(base64is.doEncode);
        assertNotNull(base64is.base64);
    }

    /**
     * Tests the constructor with doEncode = false
     */
    @org.junit.Test
    public void testConstructorWithDecode() {
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), false);
        assertNotNull(base64is);
        assertEquals(Base64InputStream.class, base64is.getClass());
        assertFalse(base64is.doEncode);
        assertNotNull(base64is.base64);
    }

    /**
     * Tests the constructor with doEncode = true
     */
    @org.junit.Test
    public void testConstructorWithEncode() {
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), true);
        assertNotNull(base64is);
        assertEquals(Base64InputStream.class, base64is.getClass());
        assertTrue(base64is.doEncode);
        assertNotNull(base64is.base64);
    }

    /**
     * Tests the constructor with doEncode = true, lineLength = 5, lineSeparator = null
     */
    @org.junit.Test
    public void testConstructorWithEncodeAndLineLengthAndNoLineSeparator() {
        Base64 base64 = new Base64(5);
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), true, 5, null);
        assertNotNull(base64is);
        assertEquals(Base64InputStream.class, base64is.getClass());
        assertTrue(base64is.doEncode);
        assertNotNull(base64is.base64);
        assertEquals(base64, base64is.base64);
    }

    /**
     * Tests the constructor with doEncode = true, lineLength = 5, lineSeparator = "\r\n"
     */
    @org.junit.Test
    public void testConstructorWithEncodeAndLineLengthAndLineSeparator() {
        Base64 base64 = new Base64(5, new byte[] { '\r', '\n' });
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), true, 5, new byte[] { '\r', '\n' });
        assertNotNull(base64is);
        assertEquals(Base64InputStream.class, base64is.getClass());
        assertTrue(base64is.doEncode);
        assertNotNull(base64is.base64);
        assertEquals(base64, base64is.base64);
    }

    /**
     * Tests the read() method with doEncode = false
     */
    @org.junit.Test
    public void testReadWithDecode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] input = "Hello World!".getBytes();
        Base64OutputStream bos = new Base64OutputStream(baos);
        bos.write(input, 0, input.length);
        bos.finish();
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Base64InputStream base64is = new Base64InputStream(inputStream, false);
        byte[] output = new byte[input.length];
        int len = base64is.read(output, 0, output.length);
        assertArrayEquals(input, output);
        assertEquals(input.length, len);
    }

    /**
     * Tests the read() method with doEncode = true
     */
    @org.junit.Test
    public void testReadWithEncode() throws IOException {
        byte[] input = "Hello World!".getBytes();
        byte[] output = new byte[input.length * 4 / 3];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(input), true);
        int len = base64is.read(output, 0, output.length);
        assertArrayEquals(output, Base64.decodeBase64(Base64.encodeBase64String(input)));
        assertEquals(input.length * 4 / 3, len);
    }

    /**
     * Tests the markSupported() method
     */
    @org.junit.Test
    public void testMarkSupported() {
        Base64InputStream base64is = new Base64InputStream(new ByteArrayInputStream(new byte[] {}));
        assertFalse(base64is.markSupported());
    }

}