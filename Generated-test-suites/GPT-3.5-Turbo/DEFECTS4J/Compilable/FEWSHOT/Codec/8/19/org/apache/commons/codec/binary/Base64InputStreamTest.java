package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        byte[] data = new byte[]{0x61, 0x62, 0x63, 0x64}; // "abcd"

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Base64InputStream bis = new Base64InputStream(bais);
        try {
            assertEquals(0x41, bis.read()); // 'A'
            assertEquals(0x51, bis.read()); // 'Q'
            assertEquals(0x3D, bis.read()); // '='

            assertEquals(-1, bis.read()); // EOF
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadWithOffsetAndLength() {
        byte[] data = new byte[]{0x61, 0x62, 0x63, 0x64}; // "abcd"

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Base64InputStream bis = new Base64InputStream(bais);
        try {
            byte[] buffer = new byte[3];
            int read = bis.read(buffer, 0, buffer.length);
            assertEquals(2, read); // "ab" (encoded as "YQ==")
            assertEquals(0x59, buffer[0]); // 'Y'
            assertEquals(0x51, buffer[1]); // 'Q'

            read = bis.read(buffer, 0, buffer.length);
            assertEquals(1, read); // "c" (encoded as "Yw==")
            assertEquals(0x59, buffer[0]); // 'Y'

            read = bis.read(buffer, 0, buffer.length);
            assertEquals(-1, read); // EOF
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        Base64InputStream bis = new Base64InputStream(bais);
        bis.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        Base64InputStream bis = new Base64InputStream(bais);
        bis.read(new byte[10], -1, 10);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        Base64InputStream bis = new Base64InputStream(bais);
        assertFalse(bis.markSupported());
    }
}