import org.junit.Before;
import org.junit.Test;

import org.apache.commons.codec.binary.Base64InputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setup() {
        byte[] testData = "Hello, World!".getBytes();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        baseNCodecInputStream = new BaseNCodecInputStream(testInputStream, new BaseNCodec(), false);
    }

    @Test
    public void testRead() throws IOException {
        int expected = 'H'; // 'H' ascii value
        int actual = baseNCodecInputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadWithEOF() throws IOException {
        ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(emptyInputStream, new BaseNCodec(), false);
        int expected = -1; // EOF
        int actual = baseNCodecInputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expected = "Hello".getBytes();
        byte[] actual = new byte[5];
        int bytesRead = baseNCodecInputStream.read(actual, 0, 5);
        assertEquals(5, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 6, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 0, 6);
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] expected = new byte[0];
        byte[] actual = new byte[5];
        int bytesRead = baseNCodecInputStream.read(actual, 0, 0);
        assertEquals(0, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMarkSupported() {
        boolean markSupported = baseNCodecInputStream.markSupported();
        assertFalse(markSupported);
    }

    @Test
    public void testAvailable() throws IOException {
        int expected = 1; // we don't have enough information to return an accurate value, so just return 1 as a safe guess
        int actual = baseNCodecInputStream.available();
        assertEquals(expected, actual);
    }

}