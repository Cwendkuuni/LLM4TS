package org.apache.commons.codec.binary;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {

    private Base64InputStream stream;

    @Before
    public void setup() {
        byte[] buffer = new byte[]{65, 66, 67, 68}; // "ABCD" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        stream = new Base64InputStream(inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int result = stream.read();
        assertEquals(65, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = stream.read(buffer);
        assertEquals(4, bytesRead);
        assertEquals(65, buffer[0]);
        assertEquals(66, buffer[1]);
        assertEquals(67, buffer[2]);
        assertEquals(68, buffer[3]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        stream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        stream.read(buffer, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        byte[] buffer = new byte[4];
        stream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[3];
        stream.read(buffer, 0, 4);
    }

    @Test
    public void testReadByteArrayWithZeroLength() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = stream.read(buffer, 0, 0);
        assertEquals(0, bytesRead);
    }
}
