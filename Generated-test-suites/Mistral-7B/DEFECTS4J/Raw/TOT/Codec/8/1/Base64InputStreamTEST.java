package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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

    /**
     * Test constructor Base64InputStream(InputStream).
     */
    @Test
    public void testBase64InputStreamConstructorInputStream() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test read operation
        byte[] result = new byte[3];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(read, 3);
        assertEquals(result[0], (byte) 'T'.charValue());
        assertEquals(result[1], (byte) 'e'.charValue());
        assertEquals(result[2], (byte) 's'.charValue());

        // Test EOF
        read = base64InputStream.read(result, 0, 3);
        assertEquals(read, -1);

        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream, boolean).
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamBoolean() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write("Test".getBytes());
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Test read operation
        byte[] result = new byte[3];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(read, 3);
        assertEquals(result[0], (byte) 'T'.charValue());
        assertEquals(result[1], (byte) 'e'.charValue());
        assertEquals(result[2], (byte) 's'.charValue());

        // Test EOF
        read = base64InputStream.read(result, 0, 3);
        assertEquals(read, -1);

        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream, boolean, int, byte[]).
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamBooleanIntByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write("Test".getBytes());
        base64OutputStream.write("\r\n".getBytes());
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, null);

        // Test read operation
        byte[] result = new byte[12];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(read, 11);
        assertEquals(result[0], (byte) 'T'.charValue());
        assertEquals(result[1], (byte) 'e'.charValue());
        assertEquals(result[2], (byte) 's'.charValue());
        assertEquals(result[3], (byte) 't'.charValue());
        assertEquals(result[4], (byte) ' '); // space
        assertEquals(result[5], (byte) 13); // CR
        assertEquals(result[6], (byte) 10); // LF
        assertEquals(result[7], (byte) '='.charValue());
        assertEquals(result[8], (byte) '='.charValue());
        assertEquals(result[9], (byte) '='.charValue());
        assertEquals(result[10], (byte) 0); // padding

        // Test EOF
        read = base64InputStream.read(result, 0, result.length);
        assertEquals(read, -1);

        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream, boolean, int, byte[]).
     */
    @Test
    public void testBase64InputStreamConstructorInputStreamBooleanIntByteArrayWithInvalidParameters() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, true, 0, null));
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, true, 1, null));
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, true, 5, null));
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, true, -1, null));
        assertThrows(NullPointerException.class, () -> new Base64InputStream(inputStream, true, 4, null));

        inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, false));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, false, 0, null));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true, 0, null));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true, 5, null));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true, -1, null));

        inputStream = new ByteArrayInputStream("Test\r\n".getBytes());
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true, 4, null));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true, -1, null));
    }

    /**
     * Test method markSupported.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    /**
     * Test method read.
     */
    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write("Test".getBytes());
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] result = new byte[3];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(read, 3);
        assertEquals(result[0], (byte) 'T'.charValue());
        assertEquals(result[1], (byte) 'e'.charValue());
        assertEquals(result[2], (byte) 's'.charValue());

        read = base64InputStream.read(new byte[3][], 0, 3 * 3);
        assertEquals(read, 11);
        assertEquals(new String(new byte[] { 'T', 'e', 's', 't', ' ', 13, 10, '=', '=', '=', 0 }),
                new String(new byte[] { result[0], result[1], result[2], result[3], result[4], result[5], result[6],
                        result[7], result[8], result[9], result[10] }));

        read = base64InputStream.read(new byte[3][], 0, 3 * 3);
        assertNull(read);

        base64InputStream.close();
    }
}