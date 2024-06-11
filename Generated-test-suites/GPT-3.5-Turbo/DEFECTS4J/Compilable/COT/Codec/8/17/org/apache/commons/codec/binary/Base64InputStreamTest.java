package org.apache.commons.codec.binary;

import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        assertEquals(72, base64InputStream.read());
        assertEquals(101, base64InputStream.read());
        assertEquals(108, base64InputStream.read());
        assertEquals(108, base64InputStream.read());
        assertEquals(111, base64InputStream.read());
        assertEquals(32, base64InputStream.read());
        assertEquals(87, base64InputStream.read());
        assertEquals(111, base64InputStream.read());
        assertEquals(114, base64InputStream.read());
        assertEquals(108, base64InputStream.read());
        assertEquals(100, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] byteArray = new byte[6];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQ=".getBytes()));
        assertEquals(6, base64InputStream.read(byteArray, 0, 6));
        assertEquals("SGVsbG", new String(byteArray));
        assertEquals(2, base64InputStream.read(byteArray, 0, 6));
        assertEquals("sbG8g", new String(byteArray));
        assertEquals(2, base64InputStream.read(byteArray, 0, 6));
        assertEquals("V29yb", new String(byteArray));
        assertEquals(0, base64InputStream.read(byteArray, 0, 6));
        base64InputStream.close();
    }
}
