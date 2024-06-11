import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] data = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int readByte;
        
        // Typical Use Case: Read single byte from input stream
        readByte = base64InputStream.read();
        assertEquals('H', readByte);

        // Typical Use Case: Read multiple bytes from input stream
        byte[] readBytes = new byte[6];
        int bytesRead = base64InputStream.read(readBytes);
        assertEquals(6, bytesRead);
        assertArrayEquals("ello, ".getBytes(), readBytes);

        // Typical Use Case: Read bytes from input stream with offset and length parameters
        byte[] offsetBytes = new byte[5];
        bytesRead = base64InputStream.read(offsetBytes, 1, 4);
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{0, 'e', 'l', 'l', 'o'}, offsetBytes);

        // Edge Case: Read until EOF has been reached
        StringBuilder strBuilder = new StringBuilder();
        while ((readByte = base64InputStream.read()) != -1) {
            strBuilder.append((char) readByte);
        }
        assertEquals("Hello, World!", strBuilder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullPointerException() throws IOException {
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException1() throws IOException {
        byte[] readBytes = new byte[2];
        base64InputStream.read(readBytes, -1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException2() throws IOException {
        byte[] readBytes = new byte[2];
        base64InputStream.read(readBytes, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException3() throws IOException {
        byte[] readBytes = new byte[2];
        base64InputStream.read(readBytes, 1, 2);
    }

    @Test(expected = IOException.class)
    public void testReadIOException() throws IOException {
        InputStream invalidInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        Base64InputStream invalidStream = new Base64InputStream(invalidInputStream);
        invalidStream.read();
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

}