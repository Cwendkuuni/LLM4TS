package org.apache.commons.codec.binary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private BaseNCodec baseNCodec;
    private InputStream inputStream;
    private boolean doEncode;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        doEncode = false;
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(1, result);

        result = baseNCodecInputStream.read();
        assertEquals(2, result);

        result = baseNCodecInputStream.read();
        assertEquals(3, result);

        result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithZeroLengthArray() throws IOException {
        byte[] buffer = new byte[0];
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        byte[] buffer = null;
        baseNCodecInputStream.read(buffer, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, 5);
    }

    @Test
    public void testReadWithEncode() throws IOException {
        doEncode = true;
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        byte[] buffer = new byte[3];
        int result = baseNCodecInputStream.read(buffer, 0, 3);

        assertEquals(3, result);
        assertArrayEquals(new byte[]{65, 66, 67}, buffer);
    }

    @Test
    public void testReadWithDecode() throws IOException {
        byte[] encodedData = new byte[]{65, 66, 67};
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(encodedData), baseNCodec, false);

        byte[] buffer = new byte[3];
        int result = baseNCodecInputStream.read(buffer, 0, 3);

        assertEquals(3, result);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        assertFalse(result);
    }

    @Test
    public void testAvailable() throws IOException {
        int result = baseNCodecInputStream.available();
        assertEquals(1, result);
    }

}