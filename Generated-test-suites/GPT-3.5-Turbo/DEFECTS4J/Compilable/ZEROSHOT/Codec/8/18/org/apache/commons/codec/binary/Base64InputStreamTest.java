package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        InputStream input = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
        };
        base64InputStream = new Base64InputStream(input);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 2, 5);
        assertEquals(0, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 2, 10);
        assertEquals(0, result);
    }

    @Test
    public void testReadWithNullBuffer() {
        try {
            base64InputStream.read(null, 0, 5);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected exception
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testReadWithNegativeOffset() {
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, -1, 5);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testReadWithNegativeLength() {
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() {
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, 12, 5);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}