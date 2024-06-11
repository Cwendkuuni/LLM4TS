package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() throws Exception {
        byte[] data = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('o', base64InputStream.read());

        assertEquals(',', base64InputStream.read());
        assertEquals(' ', base64InputStream.read());
        assertEquals('W', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals('r', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('d', base64InputStream.read());

        assertEquals('!', base64InputStream.read());

        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[2];

        int bytesRead = base64InputStream.read(buffer);

        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals('l', buffer[0]);
        assertEquals('l', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals('o', buffer[0]);
        assertEquals(',', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals(' ', buffer[0]);
        assertEquals('W', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals('o', buffer[0]);
        assertEquals('r', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals('l', buffer[0]);
        assertEquals('d', buffer[1]);
        assertEquals(2, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals('!', buffer[0]);
        assertEquals(1, bytesRead);

        bytesRead = base64InputStream.read(buffer);

        assertEquals(-1, bytesRead); // EOF
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[10];

        int bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals('\0', buffer[0]);
        assertEquals('\0', buffer[1]);
        assertEquals('H', buffer[2]);
        assertEquals('e', buffer[3]);
        assertEquals('l', buffer[4]);
        assertEquals(3, bytesRead);

        bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals('l', buffer[2]);
        assertEquals('o', buffer[3]);
        assertEquals(',', buffer[4]);
        assertEquals(3, bytesRead);

        bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals(' ', buffer[2]);
        assertEquals('W', buffer[3]);
        assertEquals('o', buffer[4]);
        assertEquals(3, bytesRead);

        bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals('r', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('d', buffer[4]);
        assertEquals(3, bytesRead);

        bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals('!', buffer[2]);
        assertEquals('\0', buffer[3]);
        assertEquals('\0', buffer[4]);
        assertEquals(1, bytesRead);

        bytesRead = base64InputStream.read(buffer, 2, 3);

        assertEquals(-1, bytesRead); // EOF
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}