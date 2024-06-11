package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        assertEquals(72, baseNCodecInputStream.read()); // First character H
        assertEquals(101, baseNCodecInputStream.read()); // Second character e
        assertEquals(108, baseNCodecInputStream.read()); // Third character l
        assertEquals(108, baseNCodecInputStream.read()); // Fourth character l
        assertEquals(111, baseNCodecInputStream.read()); // Fifth character o
        assertEquals(-1, baseNCodecInputStream.read()); // EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello" in ASCII
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        byte[] buffer = new byte[3];
        int bytesRead = baseNCodecInputStream.read(buffer, 0, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{72, 101, 108}, buffer); // First three characters
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        baseNCodecInputStream.read(new byte[3], 4, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new Base64(), false);

        baseNCodecInputStream.read(new byte[3], 0, 4);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), new Base64(), false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    // Additional edge cases and error scenarios can be added here
    
}