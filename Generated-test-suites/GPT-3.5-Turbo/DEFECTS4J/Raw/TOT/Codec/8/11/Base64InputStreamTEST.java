import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64("Test String".getBytes()));
        base64InputStream = new Base64InputStream(inputStream, false);
    }

    @Test
    public void testRead() throws IOException {
        // Typical Use Case
        assertEquals((int) 'T', base64InputStream.read());
        assertEquals((int) 'e', base64InputStream.read());
        assertEquals((int) 's', base64InputStream.read());
        assertEquals((int) 't', base64InputStream.read());
        assertEquals((int) ' ', base64InputStream.read());
        assertEquals((int) 'S', base64InputStream.read());
        assertEquals((int) 't', base64InputStream.read());
        assertEquals((int) 'r', base64InputStream.read());
        assertEquals((int) 'i', base64InputStream.read());
        assertEquals((int) 'n', base64InputStream.read());
        assertEquals((int) 'g', base64InputStream.read());

        // Edge Case: End of File
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];

        // Typical Use Case
        assertEquals(10, base64InputStream.read(buffer));
        assertArrayEquals("Test Strin".getBytes(), buffer);

        // Typical Use Case - Read Remaining Bytes
        assertEquals(1, base64InputStream.read(buffer));
        assertArrayEquals(new byte[]{103, 10, 10, 10, 10, 10, 10, 10, 10, 10}, buffer);

        // Edge Case: End of File
        assertEquals(-1, base64InputStream.read(buffer));
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBuffer() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

}