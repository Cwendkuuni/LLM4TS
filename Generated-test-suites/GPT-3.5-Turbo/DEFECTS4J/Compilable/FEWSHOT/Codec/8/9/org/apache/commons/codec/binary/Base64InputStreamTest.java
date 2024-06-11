
package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {
    // Test constructors

    @Test
    public void testConstructorWithInputStream() {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndDoEncode() {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndDoEncodeAndLineLength() {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, new byte[]{});
        assertNotNull(base64InputStream);
    }

    // Test read()

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3, 4};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int b1 = base64InputStream.read();
        assertEquals(0, b1);
        
        int b2 = base64InputStream.read();
        assertEquals(1, b2);
        
        int b3 = base64InputStream.read();
        assertEquals(2, b3);
        
        int b4 = base64InputStream.read();
        assertEquals(3, b4);
        
        int b5 = base64InputStream.read();
        assertEquals(4, b5);
        
        int b6 = base64InputStream.read();
        assertEquals(-1, b6);
    }

    // Test read(byte[] b, int offset, int len)

    @Test
    public void testReadWithValidData() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[3];
        int bytesRead = base64InputStream.read(buffer, 0, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2}, buffer);
    }

    @Test
    public void testReadWithBufferLargerThanData() throws IOException {
        byte[] data = new byte[]{0, 1, 2};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, 10);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0, 0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testReadWithOffsetAndLen() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testReadWithNegativeOffset() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, -1, 3);

        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testReadWithNegativeLen() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 1, -3);

        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testReadWithInvalidOffsetAndLen() throws IOException {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 10, 5);

        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        base64InputStream.read(null, 0, 1);
    }

    @Test
    public void testReadWithZeroLen() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 0);

        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[0];
        int bytesRead = base64InputStream.read(buffer, 0, 1);

        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[]{}, buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[0];
        base64InputStream.read(buffer, -1, 1);
    }

    // Test markSupported()

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        assertFalse(base64InputStream.markSupported());
    }
}
