package org.apache.commons.codec.binary;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[] {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        InputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        boolean doEncode = true;
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
    }
    
    @Test
    public void testRead() throws IOException {
        int[] expected = new int[] {72, 101, 108, 108, 111};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], baseNCodecInputStream.read());
        }
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadWithBufferSize() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = baseNCodecInputStream.read(buffer);
        int[] expected = new int[] {72, 101, 108, 108, 111};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = baseNCodecInputStream.read(buffer, 2, 5);
        int[] expected = new int[] {0, 0, 72, 101, 108, 108, 111, 0, 0, 0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], buffer[i]);
        }
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, 2, 1);
    }

    @Test
    public void testMarkSupported() {
        assertEquals(false, baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testSkip() throws IOException {
        long skipLength = 3;
        long skipped = baseNCodecInputStream.skip(skipLength);
        assertEquals(skipLength, skipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() throws IOException {
        long skipLength = -1;
        baseNCodecInputStream.skip(skipLength);
    }

    @Test
    public void testAvailable() throws IOException {
        int available = baseNCodecInputStream.available();
        assertEquals(1, available);
    }
  
}