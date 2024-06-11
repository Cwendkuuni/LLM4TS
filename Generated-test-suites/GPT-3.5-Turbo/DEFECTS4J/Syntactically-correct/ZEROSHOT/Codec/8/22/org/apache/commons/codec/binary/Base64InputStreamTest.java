import org.junit.Test;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[11];
        int read = inputStream.read(buffer, 0, buffer.length);

        assertEquals(11, read);
        assertArrayEquals(data, buffer); // Ensure the bytes read match the original data
    }

    @Test
    public void testRead_WithOffset() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[13];

        int read = inputStream.read(buffer, 2, 11);

        assertEquals(11, read);
        assertArrayEquals(new byte[]{0, 0, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}, buffer);
    }

    @Test
    public void testRead_EmptyData() throws Exception {
        byte[] data = new byte[0];
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[5];
        int read = inputStream.read(buffer, 0, buffer.length);

        assertEquals(-1, read); // Expect -1 indicating end of file
    }

    @Test
    public void testRead_LessThanRequested() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[20]; // Larger buffer than available data

        int read = inputStream.read(buffer, 0, buffer.length);

        assertEquals(11, read); // Expect only "Hello World" to be read
        assertArrayEquals(data, buffer); // Ensure the bytes read match the original data
        assertArrayEquals(new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buffer); // Ensure remaining buffer is unchanged
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullBuffer() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        inputStream.read(null, 0, 5); // Expect NullPointerException as buffer is null
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffset() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[5];

        inputStream.read(buffer, -1, buffer.length); // Expect IndexOutOfBoundsException as offset is negative
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[5];

        inputStream.read(buffer, 0, -1); // Expect IndexOutOfBoundsException as length is negative
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidBufferSize() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        Base64InputStream inputStream = new Base64InputStream(data, false);

        byte[] buffer = new byte[5];

        inputStream.read(buffer, 0, buffer.length + 1); // Expect IndexOutOfBoundsException as buffer size exceeds available space
    }

    @Test(expected = IOException.class)
    public void testRead_IOError() throws Exception {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class))).thenThrow(new IOException());
        Base64InputStream inputStream = new Base64InputStream(mockInputStream, false);

        byte[] buffer = new byte[5];
        inputStream.read(buffer, 0, buffer.length); // Expect IOException to be thrown
    }

    @Test
    public void testMarkSupported() throws Exception {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);

        assertFalse(inputStream.markSupported()); // Expect markSupported to return false
    }
}