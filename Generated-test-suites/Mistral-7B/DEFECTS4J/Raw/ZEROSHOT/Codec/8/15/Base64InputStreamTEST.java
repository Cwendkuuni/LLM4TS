package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[4];
        int bytesRead = base64InputStream.read(buffer, 0, 4);
        assertEquals(bytesRead, 3);
        assertNull(base64InputStream.read(buffer, 0, 1));

        String base64Encoded = "RQ==";
        InputStream inputStreamEncoded = new ByteArrayInputStream(base64Encoded.getBytes());
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStreamEncoded);

        buffer = new byte[4];
        bytesRead = base64InputStreamEncoded.read(buffer, 0, 4);
        assertEquals(bytesRead, 3);
        assertEquals(buffer[0], (byte) 0x41);
        assertEquals(buffer[1], (byte) 0x42);
        assertEquals(buffer[2], (byte) 0x43);
        assertNull(base64InputStreamEncoded.read(buffer, 0, 1));
    }

    /**
     * Test constructor with custom encoding behavior.
     */
    @Test
    public void testCustomEncoder() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "RQ==".getBytes();
        Base64 base64 = new Base64(false);
        base64.encode(testData, 0, testData.length);
        byteArrayOutputStream.write(base64.getBufferedOutput(), 0, base64.getBufferedOutput().length);
        InputStream inputStreamEncoded = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStreamEncoded, true);

        byte[] buffer = new byte[4];
        int bytesRead = base64InputStreamEncoded.read(buffer, 0, 4);
        assertEquals(bytesRead, 3);
        assertEquals(buffer[0], (byte) 0x41);
        assertEquals(buffer[1], (byte) 0x42);
        assertEquals(buffer[2], (byte) 0x43);
        assertNull(base64InputStreamEncoded.read(buffer, 0, 1));
    }

    /**
     * Test constructor with custom line length and line separator.
     */
    @Test
    public void testCustomLineLengthAndSeparator() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "RQ==".getBytes();
        Base64 base64 = new Base64(10, new byte[] { 0x0D, 0x0A });
        base64.encode(testData, 0, testData.length);
        byteArrayOutputStream.write(base64.getBufferedOutput(), 0, base64.getBufferedOutput().length);
        InputStream inputStreamEncoded = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStreamEncoded, true, 10, new byte[] { 0x0D, 0x0A });

        byte[] buffer = new byte[11];
        int bytesRead = base64InputStreamEncoded.read(buffer, 0, 11);
        assertEquals(bytesRead, 10);
        assertEquals(buffer[0], (byte) 0x41);
        assertEquals(buffer[1], (byte) 0x42);
        assertEquals(buffer[2], (byte) 0x43);
        assertEquals(buffer[3], (byte) 0x44);
        assertNull(base64InputStreamEncoded.read(buffer, 0, 1));

        assertTrue(base64InputStreamEncoded.available() == 0);
    }

}